package com.devessentials.roomdemo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

public class ProductRepository {

    // SOS: mAllProducts starts out as a LiveData, ie that's what the db returns (see ProductDao) and
    // we pass that ref all the way to MainViewModel. When the db detects a change in the table, it
    // will change the LiveData it returns and therefore, MainViewModel's LiveData will change.
    // mSearchResults is completely different. The db returns a plain list and it's the repo that
    // wraps this inside a MutableLiveData (it's mutable cause it's changed by the repo when a user
    // queries the db). MainViewModel has a LiveData ref to this MutableLiveData so it'll get the
    // result when mSearchResults changes in here
    private final LiveData<List<Product>> mAllProducts;
    private final MutableLiveData<List<Product>> mSearchResults = new MutableLiveData<>();
    private final ProductDao mProductDao;

    public ProductRepository(Application application) {
        ProductRoomDatabase db = ProductRoomDatabase.getDatabase(application);
        mProductDao = db.productDao();
        // SOS: only queries that return LiveData can be called in the main/UI thread, cause they're
        // actually run in a background thread. All other queries must be run w AsyncTasks
        mAllProducts = mProductDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
    }

    public LiveData<List<Product>> getSearchResults() {
        return mSearchResults;
    }

    public void findProduct(String name) {
        new QueryAsyncTask(mProductDao, this).execute(name);
    }

    // SOS: called from QueryAsyncTask in its onPostExecute to update mSearchResults
    private void queryFinished(List<Product> results) {
        mSearchResults.setValue(results);
    }

    public void insertProduct(Product product) {
        new InsertAsyncTask(mProductDao).execute(product);
    }

    public void deleteProduct(String name) {
        new DeleteAsyncTask(mProductDao).execute(name);
    }

    // Here start the AsyncTask definitions
    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Product>> {

        private final ProductDao mDao;
        private final ProductRepository mRepository;

        QueryAsyncTask(ProductDao dao, ProductRepository repository) {
            mDao = dao;
            mRepository = repository;
        }

        @Override
        protected List<Product> doInBackground(String... params) {
            return mDao.findProduct(params[0]);
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            mRepository.queryFinished(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Product, Void, Void> {

        private final ProductDao mDao;

        InsertAsyncTask(ProductDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Product... params) {
            mDao.insertProduct(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {

        private final ProductDao mDao;

        DeleteAsyncTask(ProductDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mDao.deleteProduct(params[0]);
            return null;
        }
    }
}

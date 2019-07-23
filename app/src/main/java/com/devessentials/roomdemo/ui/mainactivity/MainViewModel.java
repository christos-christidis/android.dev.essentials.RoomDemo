package com.devessentials.roomdemo.ui.mainactivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.devessentials.roomdemo.Product;
import com.devessentials.roomdemo.ProductRepository;

import java.util.List;

// SOS: the viewModel talks to the repo. The difference of AndroidViewModel from ViewModel is that the
// former stores an Application context in its constructor, which I can get w getApplication() later.
public class MainViewModel extends AndroidViewModel {

    private final ProductRepository mRepository;
    private final LiveData<List<Product>> mAllProducts;
    private final LiveData<List<Product>> mSearchResults;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ProductRepository(application);
        mAllProducts = mRepository.getAllProducts();
        mSearchResults = mRepository.getSearchResults();
    }

    LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
    }

    LiveData<List<Product>> getSearchResults() {
        return mSearchResults;
    }

    // SOS: when this method is called, the repo will change its own mSearchResults. And since we
    // have a ref to that LiveData (see constructor), mSearchResults in here will also be refreshed.
    // That explains why this method has no return
    void findProduct(String name) {
        mRepository.findProduct(name);
    }

    void insertProduct(Product product) {
        mRepository.insertProduct(product);
    }

    void deleteProduct(String name) {
        mRepository.deleteProduct(name);
    }
}

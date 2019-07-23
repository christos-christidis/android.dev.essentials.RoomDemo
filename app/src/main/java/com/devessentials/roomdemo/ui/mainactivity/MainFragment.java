package com.devessentials.roomdemo.ui.mainactivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devessentials.roomdemo.Product;
import com.devessentials.roomdemo.R;

import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private ProductListAdapter mAdapter;

    private TextView mProductId;
    private EditText mProductName;
    private EditText mProductQuantity;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        View view = getView();
        assert view != null;

        mProductId = view.findViewById(R.id.productId);
        mProductName = view.findViewById(R.id.productName);
        mProductQuantity = view.findViewById(R.id.productQuantity);

        setUpButtons();
        setUpObservers();
        setUpRecyclerView();
    }

    // SOS: ALL these methods can be excised almost wholesale if I also implement 2-way data-binding
    // instead of just LiveData!
    private void clearFields() {
        mProductId.setText("");
        mProductName.setText("");
        mProductQuantity.setText("");
    }

    private void setUpButtons() {
        View view = getView();
        assert view != null;

        Button addButton = view.findViewById(R.id.addButton);
        Button findButton = view.findViewById(R.id.findButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mProductName.getText().toString();
                String quantity = mProductQuantity.getText().toString();

                if (!name.equals("") && !quantity.equals("")) {
                    Product product = new Product(name, Integer.parseInt(quantity));
                    mViewModel.insertProduct(product);
                    clearFields();
                } else {
                    mProductId.setText(R.string.incomplete_information);
                }
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.findProduct(mProductName.getText().toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteProduct(mProductName.getText().toString());
                clearFields();
            }
        });
    }

    private void setUpObservers() {
        mViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                mAdapter.setProducts(products);
            }
        });

        mViewModel.getSearchResults().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products == null || products.size() == 0) {
                    mProductId.setText(R.string.no_match);
                } else {
                    Product product = products.get(0);
                    mProductId.setText(String.format(Locale.US, "%d", product.getId()));
                    mProductName.setText(product.getName());
                    mProductQuantity.setText(String.format(Locale.US, "%d", product.getQuantity()));
                }
            }
        });
    }

    private void setUpRecyclerView() {
        View view = getView();
        assert view != null;

        RecyclerView recyclerView = view.findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProductListAdapter(R.layout.product_list_item);
        recyclerView.setAdapter(mAdapter);
    }
}

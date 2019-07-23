package com.devessentials.roomdemo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

// SOS: Essentially, this interface maps the methods to low-level SQL queries.
@Dao
interface ProductDao {

    // SOS: If a query returns LiveData, Room runs it automatically every time it detects a change
    // to the tables involved. Imagine that a Service of ours periodically checks for new products and
    // updates the db. Then, the user's UI will get these new products automatically.
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

    // SOS: We don't use LiveData here, cause findProduct is only called when the user presses a button.
    @Query("SELECT * FROM products WHERE productName = :name")
    List<Product> findProduct(String name);

    @Insert
    void insertProduct(Product product);

    @Query("DELETE FROM products WHERE productName = :name")
    void deleteProduct(String name);
}

package com.devessentials.roomdemo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "productId")
    private int mId;

    @ColumnInfo(name="productName")
    private String mName;

    // SOS: this is also stored, but it's not referenced in queries so I don't annotate it w @ColumnInfo.
    // If I do NOT want to store a field, I must annotate it w @Ignore
    private int mQuantity;

    public Product(String name, int quantity) {
        mName = name;
        mQuantity = quantity;
    }

    public int getId() {
        return mId;
    }

    void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    void setName(String name) {
        mName = name;
    }

    public int getQuantity() {
        return mQuantity;
    }

    void setQuantity(int quantity) {
        mQuantity = quantity;
    }
}

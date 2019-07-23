package com.devessentials.roomdemo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

// SOS: we will hold a concrete db through this abstract ref. The same holds for ProductDao (interface)
@Database(entities = {Product.class}, version = 1, exportSchema = false)
abstract class ProductRoomDatabase extends RoomDatabase {

    abstract ProductDao productDao();
    private static ProductRoomDatabase INSTANCE;

    static ProductRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (ProductRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductRoomDatabase.class, "product_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}

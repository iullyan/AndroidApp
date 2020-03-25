package com.store.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.store.model.DatabaseOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }


    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public ArrayList<Product> getProducts(Integer categoryId) {
        Cursor cursor;
        if (categoryId == null)
            cursor =  database.rawQuery("SELECT id, product_name, price, description, currency" +
                    " FROM products", null);
        else
            cursor = database.rawQuery("SELECT id, product_name, price, description, currency " +
                    "FROM products where category_id = ?", new String[] {String.valueOf(categoryId)});
        ArrayList<Product> productList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            productList.add(new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4)));
            cursor.moveToNext();
        }
        cursor.close();
        return productList;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categoriesList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT id, name FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            categoriesList.add(new Category( cursor.getInt(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return categoriesList;

    }
}
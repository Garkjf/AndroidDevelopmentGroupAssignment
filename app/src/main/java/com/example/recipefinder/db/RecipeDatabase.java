package com.example.recipefinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDatabase extends SQLiteOpenHelper {
    // Database version and name
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "recipes.db";

    // Table name
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FAVOURITES = "favourites";
    private static final String TABLE_INGREDIENTS = "ingredients";

    public RecipeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_name TEXT, user_email TEXT, user_password TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_FAVOURITES +
                " (recipe_id INTEGER PRIMARY KEY AUTOINCREMENT, api_id INTEGER, " +
                "recipe_name TEXT, recipe_image_url TEXT, " +
                "recipe_description TEXT, recipe_instructions TEXT, user_id INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_INGREDIENTS +
                " (ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER, " +
                "ingredient_name TEXT, ingredient_measure TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }
}

package com.example.recipefinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.recipefinder.model.Ingredient;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RecipeDatabase extends SQLiteOpenHelper  {
    // Database version and name
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "recipes.db";

    // Table name
    private static final String TABLE_FAVOURITES = "favourites";

    // Column names
    private final String COLUMN_ID = "id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_IMAGE_URL = "image_url";
    private final String COLUMN_DESCRIPTION = "description";
    private final String COLUMN_INGREDIENTS = "ingredients";
    private final String COLUMN_INSTRUCTIONS = "instructions";

    public RecipeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, "+
                COLUMN_DESCRIPTION + " TEXT, "+
                COLUMN_INGREDIENTS + " TEXT, "+
                COLUMN_INSTRUCTIONS + " TEXT " + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(db);
    }

    private String listToString(List<Ingredient> list) {
        StringBuilder sb = new StringBuilder();
        for (Ingredient item : list) {
            sb.append(item.toArrayString()).append(",");
        }
        return sb.toString();
    }

    // Method to add a recipe to the database
    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, recipe.recipeID);
        values.put(COLUMN_NAME, recipe.recipeName);
        values.put(COLUMN_IMAGE_URL, recipe.imgURL);
        values.put(COLUMN_DESCRIPTION, recipe.description);
        values.put(COLUMN_INGREDIENTS, listToString(recipe.ingredients));
        values.put(COLUMN_INSTRUCTIONS, recipe.instruction);

        db.insert(TABLE_FAVOURITES, null, values);
        db.close();
    }

    private Recipe getRecipeFromCursor(Cursor cursor) {
        RecipePreview preview = new RecipePreview(
                cursor.getInt(0),   // Recipe ID
                cursor.getString(1), // Recipe Name
                cursor.getString(2)  // Recipe Image URL
        );

        Recipe currRecipe = new Recipe(
                preview,
                cursor.getString(3), // Recipe Description
                cursor.getString(5)  // Recipe Instructions
        );

        for (String ingredient : cursor.getString(4).split(",")) {
            String[] parts = ingredient.split("-");
            currRecipe.addIngredient(parts[1], parts[0]);
        }

        return currRecipe;
    }

    public List<RecipePreview> getRecipePreviews() {
        List<RecipePreview> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+TABLE_FAVOURITES;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(queryString, null)) {
            while (cursor.moveToNext()) {
                RecipePreview preview = new RecipePreview(
                        cursor.getInt(0),   // Recipe ID
                        cursor.getString(1), // Recipe Name
                        cursor.getString(2)  // Recipe Image URL
                );
                returnList.add(preview);
            }
        }

        db.close();
        return returnList;
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Recipe recipe = null;

        // Query to find the recipe by ID
        Cursor cursor = db.query(TABLE_FAVOURITES,
                null, // All columns
                COLUMN_ID + "=?", // WHERE clause
                new String[]{String.valueOf(id)}, // WHERE args
                null, // Group by
                null, // Having
                null); // Order by

        // If a result is found, create a RecipePreview object
        if (cursor != null && cursor.moveToFirst()) {
            recipe = getRecipeFromCursor(cursor);
            cursor.close();
        }
        db.close();
        return recipe;
    }

    public HashSet<Integer> getAllRecipesID() {
        HashSet<Integer> returnSet = new HashSet<>();
        String queryString = "SELECT * FROM "+TABLE_FAVOURITES;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(queryString, null)) {
            while (cursor.moveToNext()) {
                returnSet.add(cursor.getInt(0));
            }
        }

        db.close();
        return returnSet;
    }

    public void deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}

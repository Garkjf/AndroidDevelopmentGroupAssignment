package com.example.recipefinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.recipefinder.model.Ingredient;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RecipeDatabase extends SQLiteOpenHelper  {
    // Database version and name
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "recipes.db";

    // Table name
    private static final String TABLE_FAVOURITES = "favourites";
    private static final String TABLE_INGREDIENTS = "ingredients";

    // Column names
    private final String RECIPE_ID = "recipe_id";
    private final String RECIPE_NAME = "recipe_name";
    private final String RECIPE_IMAGE_URL = "recipe_image_url";
    private final String RECIPE_DESCRIPTION = "recipe_description";
    private final String RECIPE_INSTRUCTIONS = "recipe_instructions";

    private final String INGREDIENT_NAME = "ingredient_name";
    private final String INGREDIENT_MEASURE = "ingredient_measure";

    public RecipeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFavouritesTableQuery = "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                RECIPE_ID + " INTEGER PRIMARY KEY, " +
                RECIPE_NAME + " TEXT, " +
                RECIPE_IMAGE_URL + " TEXT, "+
                RECIPE_DESCRIPTION + " TEXT, "+
                RECIPE_INSTRUCTIONS + " TEXT " + ")";

        String INGREDIENT_ID = "ingredient_id";
        String createIngredientsTableQuery = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPE_ID + " INTEGER, " +
                INGREDIENT_NAME + " TEXT, " +
                INGREDIENT_MEASURE + " TEXT " + ")";


        db.execSQL(createFavouritesTableQuery);
        db.execSQL(createIngredientsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    // Method to add a recipe to the database
    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RECIPE_ID, recipe.getRecipeID());
        recipeValues.put(RECIPE_NAME, recipe.getRecipeName());
        recipeValues.put(RECIPE_IMAGE_URL, recipe.getImgURL());
        recipeValues.put(RECIPE_DESCRIPTION, recipe.getDescription());
        recipeValues.put(RECIPE_INSTRUCTIONS, recipe.getInstruction());
        db.insert(TABLE_FAVOURITES, null, recipeValues);

        ContentValues ingredientValues = new ContentValues();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientValues.put(RECIPE_ID, recipe.getRecipeID());
            ingredientValues.put(INGREDIENT_NAME, ingredient.getName());
            ingredientValues.put(INGREDIENT_MEASURE, ingredient.getMeasure());
            db.insert(TABLE_INGREDIENTS, null, ingredientValues);
        }

        db.close();
    }

    private Recipe getRecipeFromCursor(Cursor cursor) {
        RecipePreview preview = new RecipePreview(
                cursor.getInt(0),   // Recipe ID
                cursor.getString(1), // Recipe Name
                cursor.getString(2)  // Recipe Image URL
        );

        return new Recipe(
                preview,
                cursor.getString(3), // Recipe Description
                cursor.getString(4)  // Recipe Instructions
        );
    }

    public List<RecipePreview> getRecipePreviews() {
        List<RecipePreview> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String favouritesQuery = "SELECT * FROM "+TABLE_FAVOURITES;

        try (Cursor cursor = db.rawQuery(favouritesQuery, null)) {
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
        Cursor cursor = null;

        try {
            String recipeQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                    TABLE_FAVOURITES, RECIPE_ID);
            cursor = db.rawQuery(recipeQuery, new String[]{String.valueOf(id)});

            if (cursor != null && cursor.moveToFirst()) {
                recipe = getRecipeFromCursor(cursor);
                Log.d("RECIPE_ACTIVITY", recipe.getRecipeID()+" "+recipe.getRecipeName());

                cursor.close();

                // Query to get the ingredients for the recipe
                String ingredientsQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                        TABLE_INGREDIENTS, RECIPE_ID);
                cursor = db.rawQuery(ingredientsQuery, new String[]{String.valueOf(id)});

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        // Adjust the indices based on your table structure
                        String ingredientName = cursor.getString(2);
                        String ingredientQuantity = cursor.getString(3);
                        Log.d("RECIPE_ACTIVITY", "Ingredient: " + ingredientName + " Quantity: " + ingredientQuantity);
                        recipe.addIngredient(ingredientName, ingredientQuantity);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            if (cursor != null) cursor.close(); // Always close cursor if it's not null
            db.close(); // Close database connection
        }

        return recipe;

    }

    public HashSet<Integer> getAllRecipesID() {
        HashSet<Integer> returnSet = new HashSet<>();
        String queryString = String.format("SELECT %s FROM %s", RECIPE_ID, TABLE_FAVOURITES);
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
        db.delete(TABLE_FAVOURITES, RECIPE_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_INGREDIENTS, RECIPE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}

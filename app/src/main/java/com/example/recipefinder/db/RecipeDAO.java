package com.example.recipefinder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.recipefinder.model.Ingredient;
import com.example.recipefinder.model.Recipe;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.UserSession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RecipeDAO {
    private final String TABLE_FAVOURITES = "favourites";
    private final String TABLE_INGREDIENTS = "ingredients";

    private final String USER_ID = "user_id";

    private final String RECIPE_ID = "recipe_id";
    private final String API_ID = "api_id";
    private final String RECIPE_NAME = "recipe_name";
    private final String RECIPE_IMAGE_URL = "recipe_image_url";
    private final String RECIPE_DESCRIPTION = "recipe_description";
    private final String RECIPE_INSTRUCTIONS = "recipe_instructions";

    private final String INGREDIENT_NAME = "ingredient_name";
    private final String INGREDIENT_MEASURE = "ingredient_measure";

    private final RecipeDatabase dbHelper;
    private final int userID;

    public RecipeDAO(Context context) {
        dbHelper = new RecipeDatabase(context);
        userID = UserSession.getInstance().getCurrentUser().getId();
    }

    // Add a recipe to the database
    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(API_ID, recipe.getApiID());
        recipeValues.put(RECIPE_NAME, recipe.getRecipeName());
        recipeValues.put(RECIPE_IMAGE_URL, recipe.getImgURL());
        recipeValues.put(RECIPE_DESCRIPTION, recipe.getDescription());
        recipeValues.put(RECIPE_INSTRUCTIONS, recipe.getInstruction());
        recipeValues.put(USER_ID, userID);
        int recipeId = (int) db.insert(TABLE_FAVOURITES, null, recipeValues);

        ContentValues ingredientValues = new ContentValues();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientValues.put(RECIPE_ID, recipeId);
            ingredientValues.put(INGREDIENT_NAME, ingredient.getName());
            ingredientValues.put(INGREDIENT_MEASURE, ingredient.getMeasure());
            db.insert(TABLE_INGREDIENTS, null, ingredientValues);
        }

        db.close();
    }

    // Get list of RecipePreviews from the database
    public List<RecipePreview> getRecipePreviews() {
        List<RecipePreview> returnList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String favouritesQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                TABLE_FAVOURITES, USER_ID);
        Cursor cursor = db.rawQuery(favouritesQuery, new String[]{String.valueOf(userID)});
        while (cursor.moveToNext()) {
            returnList.add(new RecipePreview(
                    cursor.getInt(cursor.getColumnIndexOrThrow(API_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_IMAGE_URL))
            ));
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public Recipe getRecipe(int apiId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Recipe recipe;
        Cursor cursor = null;

        try {
            // Query for recipe and ingredients in a single JOIN query
            String query = String.format("SELECT f.*, i.%s, i.%s FROM %s f LEFT JOIN %s i " +
                                         "ON f.%s = i.%s WHERE f.%s = ?",
                    INGREDIENT_NAME, INGREDIENT_MEASURE, TABLE_FAVOURITES, TABLE_INGREDIENTS,
                    RECIPE_ID, RECIPE_ID, API_ID);

            cursor = db.rawQuery(query, new String[]{String.valueOf(apiId)});
            if (!cursor.moveToFirst()) return null;

            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(API_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INSTRUCTIONS))
            );

            do {
                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow(INGREDIENT_NAME));
                String ingredientMeasure = cursor.getString(
                        cursor.getColumnIndexOrThrow(INGREDIENT_MEASURE));

                if (ingredientName != null && ingredientMeasure != null) {
                    recipe.addIngredient(ingredientName, ingredientMeasure);
                }
            } while (cursor.moveToNext());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return recipe;
    }

    // Get a list of recipe IDs from the database
    public HashSet<Integer> getAllRecipesID() {
        HashSet<Integer> returnSet = new HashSet<>();
        String queryString = String.format("SELECT %s FROM %s WHERE %s = ?",
                API_ID, TABLE_FAVOURITES, USER_ID);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(userID)});
        while (cursor.moveToNext()) {
            returnSet.add(cursor.getInt(cursor.getColumnIndexOrThrow("api_id")));
        }
        cursor.close();

        db.close();
        return returnSet;
    }

    // Delete a recipe from the database
    public void deleteRecipe(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(TABLE_FAVOURITES, RECIPE_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_INGREDIENTS, RECIPE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Ingredient> getIngredients(int apiId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Ingredient> ingredients = new ArrayList<>();

        // Query for recipe and ingredients in a single JOIN query
        String query = String.format("SELECT %s, %s FROM %s WHERE %s IN " +
                                     "(SELECT %s FROM %s WHERE %s = ?)",
                INGREDIENT_MEASURE, INGREDIENT_NAME, TABLE_INGREDIENTS, RECIPE_ID,
                RECIPE_ID, TABLE_FAVOURITES, API_ID);

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(apiId)})) {
            if (cursor.moveToFirst()) {
                do {
                    String ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(
                            INGREDIENT_NAME));
                    String ingredientMeasure = cursor.getString(cursor.getColumnIndexOrThrow(
                            INGREDIENT_MEASURE));

                    // Add to the list if both values are non-null
                    if (ingredientName != null && ingredientMeasure != null) {
                        ingredients.add(new Ingredient(ingredientName, ingredientMeasure));
                    }
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return ingredients;
    }
}

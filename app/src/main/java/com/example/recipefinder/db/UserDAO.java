package com.example.recipefinder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.recipefinder.model.User;

public class UserDAO {
    private final String TABLE_USERS = "users";

    private final String USER_ID = "user_id";
    private final String USER_NAME = "user_name";
    private final String USER_EMAIL = "user_email";
    private final String USER_PASSWORD = "user_password";

    private final RecipeDatabase dbHelper;

    public UserDAO(Context context) {
        dbHelper = new RecipeDatabase(context);
    }

    public void addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues userValues = new ContentValues();
        userValues.put(USER_NAME, user.getUsername());
        userValues.put(USER_EMAIL, user.getEmail());
        userValues.put(USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, userValues);

        db.close();
    }

    public User getUser(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userQuery = "SELECT * FROM " + TABLE_USERS + " WHERE user_name = ?";
        Cursor cursor = db.rawQuery(userQuery, new String[]{String.valueOf(username)});
        cursor.moveToFirst();
        User user;
        if (cursor.getCount() == 0) return null;

        user = new User(
            cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD))
        );

        cursor.close();
        return user;
    }

    public void editUser(int id, User newUser) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues userValues = new ContentValues();
        userValues.put(USER_NAME, newUser.getUsername());
        userValues.put(USER_EMAIL, newUser.getEmail());
        userValues.put(USER_PASSWORD, newUser.getPassword());

        db.update(TABLE_USERS, userValues, USER_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public void deleteUser(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] userID = new String[]{String.valueOf(id)};
        final String RECIPE_ID = "recipe_id";

        String ingredientCriteria = String.format("%s IN (SELECT %s FROM recipes WHERE %s = ?)",
                                    RECIPE_ID, RECIPE_ID, USER_ID);

        db.delete("ingredients", ingredientCriteria, userID);
        db.delete("recipes", USER_ID + " = ?", userID);
        db.delete(TABLE_USERS, USER_ID + " = ?", userID);

        db.close();
    }
}

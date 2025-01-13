package com.example.formPatient.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.formPatient.Entity.User;
import com.example.formPatient.MyDatabaseHelper;
public class UserService  implements UserDao{
    private MyDatabaseHelper dbHelper;
    public UserService(Context context) {
        this.dbHelper =new MyDatabaseHelper(context);
    }
    public int updatePassword(String email, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pwd", newPassword);
        return db.update("users", values, "email = ?", new String[]{email});
    }
    @Override
    public void updateUser(User user) {
    }
    @Override
    public void insertUser(User user) {
    }
}

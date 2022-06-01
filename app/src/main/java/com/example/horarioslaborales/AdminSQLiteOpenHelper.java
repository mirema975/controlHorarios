package com.example.horarioslaborales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDatos) {
        baseDeDatos.execSQL("create table horarios(anio int, mes int, dia int, ingreso1_HH int, ingreso1_MM int, salida1_HH int, salida1_MM int, ingreso2_HH int , ingreso2_MM int, salida2_HH int, salida2_MM int, salida1Marcada boolean, salida2Marcada boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}

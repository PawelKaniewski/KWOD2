package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine.CustomizedMedicine;
import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine.Medicine;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "healthCare.db";
    private static final String MEDICINES_TABLE_NAME = "MEDICINES";
    private static final String CUSTOM_MEDICINES_TABLE_NAME = "CUSTOM_MEDICINES";
    private static final String[] CUSTOM_MEDICINES_COLUMNS = {"MEDICINE_NAME","MEDICINE_DESCRIPTION","FREQUENCY","PORTION","UNIT"};
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "DESCRIPTION";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMedicinesTable = "CREATE TABLE " + MEDICINES_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " NAME TEXT, DESCRIPTION TEXT)";
        db.execSQL(createMedicinesTable);

        String createCustomMedicinesTable = "CREATE TABLE " + CUSTOM_MEDICINES_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " MEDICINE_NAME TEXT, MEDICINE_DESCRIPTION TEXT, FREQUENCY TEXT, PORTION TEXT, UNIT TEXT)";
        db.execSQL(createCustomMedicinesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEDICINES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOM_MEDICINES_TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, medicine.getName());
        contentValues.put(COL3, medicine.getDescription());

        long result = db.insert(MEDICINES_TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addNewCustomMedicineToDB(CustomizedMedicine customizedMedicine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[0], customizedMedicine.getMedicine().getName());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[1], customizedMedicine.getMedicine().getDescription());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[2], customizedMedicine.getFrequency());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[3], customizedMedicine.getPortion());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[4], customizedMedicine.getUnit());
        long result = db.insert(CUSTOM_MEDICINES_TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + MEDICINES_TABLE_NAME, null);
        return data;
    }

    public Cursor getCustomMedicinesListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + CUSTOM_MEDICINES_TABLE_NAME, null);
        return data;
    }


}

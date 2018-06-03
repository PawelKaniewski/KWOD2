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
    private static final String KEY_ROWID = "_ROWID_";
    private static final String MEDICINES_TABLE_NAME = "MEDICINES";
    private static final String CUSTOM_MEDICINES_TABLE_NAME = "CUSTOM_MEDICINES";
    private static final String APPOINTMENTS_TABLE_NAME = "APPOINTMENTS";
    private static final String[] CUSTOM_MEDICINES_COLUMNS = {"MEDICINE_NAME","MEDICINE_DESCRIPTION","FREQUENCY","PORTION","UNIT"};
    private static final String[] APPOINTMENTS_COLUMNS = {"DATE","TIME","DOCTOR","LOCATION","INFO","REMIND", "REMIND_TIME"};
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

        String createAppointmentsTable = "CREATE TABLE " + APPOINTMENTS_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " DATE TEXT, TIME TEXT, DOCTOR TEXT, LOCATION TEXT, INFO TEXT, REMIND TEXT, REMIND_TIME INT)";
        db.execSQL(createAppointmentsTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEDICINES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOM_MEDICINES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + APPOINTMENTS_TABLE_NAME);
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

    public long addNewCustomMedicineToDB(CustomizedMedicine customizedMedicine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[0], customizedMedicine.getMedicine().getName());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[1], customizedMedicine.getMedicine().getDescription());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[2], customizedMedicine.getFrequency());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[3], customizedMedicine.getPortion());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[4], customizedMedicine.getUnit());
        long result = db.insert(CUSTOM_MEDICINES_TABLE_NAME, null, contentValues);

        return result;

    }

    public boolean addAppointmentToDB(String date, String time, String doctor, String place,
                                      String info, boolean remind, int beforeTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(APPOINTMENTS_COLUMNS[0], date);
        contentValues.put(APPOINTMENTS_COLUMNS[1], time);
        contentValues.put(APPOINTMENTS_COLUMNS[2], doctor);
        contentValues.put(APPOINTMENTS_COLUMNS[3], place);
        contentValues.put(APPOINTMENTS_COLUMNS[4], info);
        contentValues.put(APPOINTMENTS_COLUMNS[5], String.valueOf(remind));
        contentValues.put(APPOINTMENTS_COLUMNS[6], beforeTime);
        long result = db.insert(APPOINTMENTS_TABLE_NAME, null, contentValues);

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
        System.out.println("Nazwa kolumny: " + data.getColumnName(0)  );
        return data;
    }

    public Cursor getAppointmentsListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + APPOINTMENTS_TABLE_NAME, null);
    }

    public Cursor getAppointmentWhereDate(String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+APPOINTMENTS_TABLE_NAME+" WHERE DATE = ? AND TIME = ?", new String[] {date, time});
    }

    public boolean deleteCustomMedicine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CUSTOM_MEDICINES_TABLE_NAME, KEY_ROWID +  "=" + id, null) > 0;
    }

    public boolean updateCustomMedicine(CustomizedMedicine customizedMedicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[0], customizedMedicine.getMedicine().getName());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[1], customizedMedicine.getMedicine().getDescription());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[2], customizedMedicine.getFrequency());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[3], customizedMedicine.getPortion());
        contentValues.put(CUSTOM_MEDICINES_COLUMNS[4], customizedMedicine.getUnit());
        return db.update(CUSTOM_MEDICINES_TABLE_NAME, contentValues, KEY_ROWID + "=" + customizedMedicine.getId(), null) > 0;
    }

}

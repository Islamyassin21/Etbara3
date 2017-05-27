package com.Arzaq.Arzaq.etbara3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Arzaq.Arzaq.etbara3.Model.Model;

import java.util.ArrayList;

/**
 * Created by islam on 20/02/2017.
 */

public class Database extends SQLiteOpenHelper {

    public String CREATE_FAVE_TABLE;
    public String CREATE_ORGANIZATION_TABLE;
    public String CREATE_ORGANIZATION_PHOTO;
    private final ArrayList<Model> dataListModel = new ArrayList<>();
    private final ArrayList<Model> dataListFav = new ArrayList<>();
    private byte[] photo;

    public Database(Context context) {
        super(context, Conestant.DATABASE_NAME, null, Conestant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        CREATE_ORGANIZATION_TABLE = "CREATE TABLE " + Conestant.TABLE_Organization +
                "(" +
                Conestant.ORGANIZATION_ID + " INTEGER PRIMARY KEY," +
                Conestant.ORGANIZATION_ACCOUNT_NO + " TEXT," +
                Conestant.ORGANIZATION_INFO + " TEXT," +
                Conestant.ORGANIZATION_MOUNY + " TEXT," +
                Conestant.ORGANIZATION_NAME + " TEXT," +
                Conestant.ORGANIZATION_PHONE + " TEXT," +
                Conestant.ORGANIZATION_PHOTO + " TEXT," +
                Conestant.ORGANIZATION_SERVICE + " TEXT," +
                Conestant.ORGANIZATION_SMS + " TEXT," +
                Conestant.ORGANIZATION_YOUTUBE_LINK + " TEXT," +
                Conestant.ORGANIZATION_YOUTUBE_NAME + " TEXT," +
                Conestant.ORGANIZATION_SMS_CONTENT + " TEXT);";
        db.execSQL(CREATE_ORGANIZATION_TABLE);

        CREATE_FAVE_TABLE = "CREATE TABLE " + Conestant.TABLE_FAV +
                "(" +
                Conestant.ORGANIZATION_ID + " INTEGER," +
                Conestant.ORGANIZATION_ACCOUNT_NO + " TEXT," +
                Conestant.ORGANIZATION_INFO + " TEXT," +
                Conestant.ORGANIZATION_MOUNY + " TEXT," +
                Conestant.ORGANIZATION_NAME + " TEXT," +
                Conestant.ORGANIZATION_PHONE + " TEXT," +
                Conestant.ORGANIZATION_PHOTO + " BLOB," +
                Conestant.ORGANIZATION_SERVICE + " TEXT," +
                Conestant.ORGANIZATION_SMS + " TEXT," +
                Conestant.ORGANIZATION_YOUTUBE_LINK + " TEXT," +
                Conestant.ORGANIZATION_YOUTUBE_NAME + " TEXT," +
                Conestant.ORGANIZATION_SMS_CONTENT + " TEXT);";
        db.execSQL(CREATE_FAVE_TABLE);

        CREATE_ORGANIZATION_PHOTO = "CREATE TABLE " + Conestant.TABLE_Photo +
                "(" +
                Conestant.ORGANIZATION_ID + " INTEGER," +
                Conestant.ORGANIZATION_PHOTO + " BLOB);";
        db.execSQL(CREATE_ORGANIZATION_PHOTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void AddOrganizationPhoto(int id, byte[] photo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Conestant.ORGANIZATION_ID, id);
        values.put(Conestant.ORGANIZATION_PHOTO, photo);
        db.insert(Conestant.TABLE_Photo, null, values);
        db.close();

    }

    public byte[] getOrganizationPhoto(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Conestant.TABLE_Photo, null, Conestant.ORGANIZATION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            photo = cursor.getBlob(cursor.getColumnIndex(Conestant.ORGANIZATION_PHOTO));
        }
        return photo;
    }

    public void deleteOrganizationPhoto() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Conestant.TABLE_Photo, null, null);
        db.close();
    }

    public void deleteOrganizationFavPhoto(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Conestant.TABLE_Photo, Conestant.ORGANIZATION_ID + "=" + model.getOrganizationID(), null);
        db.close();
    }

    public boolean OrganizationExistInFav(int organizationID) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(Conestant.TABLE_FAV, null, Conestant.ORGANIZATION_ID + "=?", new String[]{String.valueOf(organizationID)}, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public boolean OrganizationExist(int organizationID) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(Conestant.TABLE_Organization, null, Conestant.ORGANIZATION_ID + "=?", new String[]{String.valueOf(organizationID)}, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public void AddOrganization(Model model) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Conestant.ORGANIZATION_ID, model.getOrganizationID());
        values.put(Conestant.ORGANIZATION_ACCOUNT_NO, model.getOrganizationID());
        values.put(Conestant.ORGANIZATION_ACCOUNT_NO, model.getOrganizationAccountNo());
        values.put(Conestant.ORGANIZATION_INFO, model.getOrganizationInfo());
        values.put(Conestant.ORGANIZATION_MOUNY, model.getOrganizationMouny());
        values.put(Conestant.ORGANIZATION_NAME, model.getOrganizationName());
        values.put(Conestant.ORGANIZATION_PHONE, model.getOrganizationPhone());
        values.put(Conestant.ORGANIZATION_PHOTO, model.getOrganizationPhoto());
        values.put(Conestant.ORGANIZATION_SERVICE, model.getOrganozationService());
        values.put(Conestant.ORGANIZATION_SMS, model.getOrganizationSMS());
        values.put(Conestant.ORGANIZATION_SMS_CONTENT, model.getOrganizationSMSContent());
        values.put(Conestant.ORGANIZATION_YOUTUBE_LINK, model.getOrganizationYoutubeLink());
        values.put(Conestant.ORGANIZATION_YOUTUBE_NAME, model.getOrganizationYoutubeName());

        db.insert(Conestant.TABLE_Organization, null, values);
        db.close();
    }

    public int getOrganizationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Conestant.TABLE_Organization, null, null, null, null, null, Conestant.ORGANIZATION_ID);

        return cursor.getCount();
    }

    public int getOrganizationCountFav() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Conestant.TABLE_FAV, null, null, null, null, null, Conestant.ORGANIZATION_ID);

        return cursor.getCount();
    }

    public void AddOrganizationFavorite(Model model) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Conestant.ORGANIZATION_ID, model.getOrganizationID());
        values.put(Conestant.ORGANIZATION_ACCOUNT_NO, model.getOrganizationID());
        values.put(Conestant.ORGANIZATION_ACCOUNT_NO, model.getOrganizationAccountNo());
        values.put(Conestant.ORGANIZATION_INFO, model.getOrganizationInfo());
        values.put(Conestant.ORGANIZATION_MOUNY, model.getOrganizationMouny());
        values.put(Conestant.ORGANIZATION_NAME, model.getOrganizationName());
        values.put(Conestant.ORGANIZATION_PHONE, model.getOrganizationPhone());
        values.put(Conestant.ORGANIZATION_PHOTO, model.getOrganizationPhoto());
        values.put(Conestant.ORGANIZATION_SERVICE, model.getOrganozationService());
        values.put(Conestant.ORGANIZATION_SMS, model.getOrganizationSMS());
        values.put(Conestant.ORGANIZATION_SMS_CONTENT, model.getOrganizationSMSContent());
        values.put(Conestant.ORGANIZATION_YOUTUBE_LINK, model.getOrganizationYoutubeLink());
        values.put(Conestant.ORGANIZATION_YOUTUBE_NAME, model.getOrganizationYoutubeName());

        db.insert(Conestant.TABLE_FAV, null, values);
        db.close();
    }

    public void deleteOrganizationFav(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Conestant.TABLE_FAV, Conestant.ORGANIZATION_ID + "=" + model.getOrganizationID(), null);
        db.close();
    }

    public void deleteOrganization() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Conestant.TABLE_Organization, null, null);
        db.close();
    }

    public ArrayList<Model> getOrganization() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Conestant.TABLE_Organization, null, null, null, null, null, Conestant.ORGANIZATION_ID);
        if (cursor.moveToFirst()) {
            do {
                Model shop = new Model();

                shop.setOrganizationID(cursor.getInt(cursor.getColumnIndex(Conestant.ORGANIZATION_ID)));
                shop.setOrganizationAccountNo(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_ACCOUNT_NO)));
                shop.setOrganizationName(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_NAME)));
                shop.setOrganizationYoutubeName(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_YOUTUBE_NAME)));
                shop.setOrganizationInfo(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_INFO)));
                shop.setOrganizationMouny(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_MOUNY)));
                shop.setOrganizationPhoto(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_PHOTO)));
                shop.setOrganizationPhone(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_PHONE)));
                shop.setOrganozationService(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_SERVICE)));
                shop.setOrganizationSMS(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_SMS)));
                shop.setOrganizationSMSContent(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_SMS_CONTENT)));
                shop.setOrganizationYoutubeLink(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_YOUTUBE_LINK)));

                dataListModel.add(shop);

            } while ((cursor.moveToNext()));
        }
        db.close();
        return dataListModel;
    }

    public ArrayList<Model> getFavourite() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Conestant.TABLE_FAV, null, null, null, null, null, Conestant.ORGANIZATION_ID);
        if (cursor.moveToFirst()) {
            do {
                Model shop = new Model();

                shop.setOrganizationID(cursor.getInt(cursor.getColumnIndex(Conestant.ORGANIZATION_ID)));
                shop.setOrganizationAccountNo(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_ACCOUNT_NO)));
                shop.setOrganizationName(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_NAME)));
                shop.setOrganizationYoutubeName(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_YOUTUBE_NAME)));
                shop.setOrganizationInfo(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_INFO)));
                shop.setOrganizationMouny(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_MOUNY)));
                shop.setOrganizationPhoto(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_PHOTO)));
                shop.setOrganizationPhone(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_PHONE)));
                shop.setOrganozationService(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_SERVICE)));
                shop.setOrganizationSMS(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_SMS)));
                shop.setOrganizationSMSContent(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_SMS_CONTENT)));
                shop.setOrganizationYoutubeLink(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_YOUTUBE_LINK)));

                dataListFav.add(shop);

            } while ((cursor.moveToNext()));
        }
        db.close();
        return dataListFav;
    }

}

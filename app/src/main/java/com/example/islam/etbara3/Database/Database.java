package com.example.islam.etbara3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.islam.etbara3.Model.Model;

import java.util.ArrayList;

/**
 * Created by islam on 20/02/2017.
 */

public class Database extends SQLiteOpenHelper {

    public String CREATE_FAVE_TABLE;
    private final ArrayList<Model> dataListModel = new ArrayList<>();

    public Database(Context context) {
        super(context, Conestant.DATABASE_NAME, null, Conestant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean OrganizationExist(int organizationID) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(Conestant.TABLE_FAV, null, Conestant.ORGANIZATION_ID + "=?", new String[]{String.valueOf(organizationID)}, null, null, null);

        cursor.moveToFirst();

        Log.v("data888", String.valueOf(cursor.getCount()));
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

        db.insert(Conestant.TABLE_FAV, null, values);
        db.close();
    }

    public void deleteOrganization(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Conestant.TABLE_FAV, Conestant.ORGANIZATION_ID + "=" + model.getOrganizationID(), null);
        db.close();
    }

    public ArrayList<Model> getFavourite() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Conestant.TABLE_FAV, null, null, null, null, null, null);
        Log.v("cursorData", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do {
                Model shop = new Model();

                shop.setOrganizationID(cursor.getInt(cursor.getColumnIndex(Conestant.ORGANIZATION_ID)));
                shop.setOrganizationAccountNo(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_ACCOUNT_NO)));
                shop.setOrganizationName(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_NAME)));
                shop.setOrganizationYoutubeName(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_YOUTUBE_NAME)));
                shop.setOrganizationInfo(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_INFO)));
                shop.setOrganizationMouny(cursor.getString(cursor.getColumnIndex(Conestant.ORGANIZATION_MOUNY)));
                shop.setOrganizationPhoto(cursor.getBlob(cursor.getColumnIndex(Conestant.ORGANIZATION_PHOTO)));
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

}

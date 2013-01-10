/**
 * 
 */
package com.android.pos;

import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author DANILKO
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    
    public static final String DB_NAME = "posdatabase.db";
    public static final int DB_VERSION = 1;
    
    public static final String TABLE_PRODUCT = "products";
    public static final String TABLE_PRODUCT_NAME = "product_name";
    public static final String TABLE_PRODUCT_IMAGE_URL = "product_image_url";
    public static final String TABLE_PRODUCT_DESCRIPTION = "product_description";
    public static final String TABLE_PRODUCT_PRICE = "product_price";
    
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_CUSTOMER_NAME = "order_customer_name";
    public static final String TABLE_ORDER_CUSTOMER_TELEPHONE = "order_customer_phone";
    public static final String TABLE_ORDER_STORE_NAME = "order_store_name";
    public static final String TABLE_ORDER_TOTAL_PRICE = "order_total_price";
    public static final String TABLE_ORDER_CREATED_AT = "order_create_at";
    public static final String TABLE_ORDER_STATUSES = "order_statuses";
    public static final String TABLE_ORDER_FINISH_AT = "order_finish_at";
    
    public static final String TABLE_ORDER_PRODUCT = "order_product";
    public static final String TABLE_ORDER_PRODUCT_ORDER_ID = "order_id";
    public static final String TABLE_ORDER_PRODUCT_PRODUCT_ID = "product_id";
    public static final String TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY = "order_product_quantity";
    public static final String TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL = "order_product_subtotal";
    
    public static final String TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT = "current";
    
    public static final String TABLE_ID = "_id";
    
    public static final String TABLE_STATUS = "table_status";
    public static final String TABLE_RESULT = "table_result";
    
    public DatabaseHelper(Context pContext)
    {
        super(pContext, DB_NAME, null, DB_VERSION);
    }  // DatabaseHelper

    
    
    @Override
    public void onCreate(SQLiteDatabase pDatabase)
    {
        pDatabase.setLocale(Locale.TAIWAN);
        
        String lSQLCommand = String.format(
                "create table if not exists %s (%s TEXT primary key, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);", 
                TABLE_PRODUCT,
                TABLE_ID, 
                TABLE_PRODUCT_NAME, 
                TABLE_PRODUCT_IMAGE_URL, 
                TABLE_PRODUCT_DESCRIPTION,
                TABLE_PRODUCT_PRICE,
                TABLE_STATUS,
                TABLE_RESULT
                );
        
        Log.d(TAG, ": SQL Command: " + lSQLCommand);
        
        pDatabase.execSQL(lSQLCommand);
        
        lSQLCommand = String.format(
                "create table if not exists %s (%s TEXT primary key, %s TEXT, %s TEXT, %s TEXT, %s FLOAT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);", 
                TABLE_ORDER, 
                TABLE_ID, 
                TABLE_ORDER_CUSTOMER_NAME, 
                TABLE_ORDER_CUSTOMER_TELEPHONE, 
                TABLE_ORDER_STORE_NAME,
                TABLE_ORDER_TOTAL_PRICE,
                TABLE_ORDER_CREATED_AT,
                TABLE_ORDER_STATUSES,
                TABLE_ORDER_FINISH_AT,
                TABLE_STATUS,
                TABLE_RESULT
                );
        
        Log.d(TAG, ": SQL Command: " + lSQLCommand);
        
        pDatabase.execSQL(lSQLCommand);
        
        lSQLCommand = String.format(
                "create table if not exists %s (%s TEXT primary key, %s TEXT, %s TEXT, %s INTERGER, %s FLOAT);", 
                TABLE_ORDER_PRODUCT, 
                TABLE_ID, 
                TABLE_ORDER_PRODUCT_ORDER_ID,
                TABLE_ORDER_PRODUCT_PRODUCT_ID,
                TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY,
                TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL
                );
        
        Log.d(TAG, ": SQL Command: " + lSQLCommand);
        
        pDatabase.execSQL(lSQLCommand);
    }  // void onCreate

    public void onDelete(SQLiteDatabase pDatabase)
    {
        String lSQLCommand = "drop table if exists " + TABLE_ORDER;
        
        pDatabase.execSQL(lSQLCommand);
        
        Log.d(TAG, ": onUpgrade drop table " + TABLE_ORDER);
        
        lSQLCommand = "drop table if exists " + TABLE_PRODUCT;
        
        pDatabase.execSQL(lSQLCommand);
        
        Log.d(TAG, ": onUpgrade drop table " + TABLE_PRODUCT);
        
        lSQLCommand = "drop table if exists " + TABLE_ORDER_PRODUCT;
        
        pDatabase.execSQL(lSQLCommand);

        Log.d(TAG, ": onUpgrade drop table " + TABLE_ORDER_PRODUCT);
        
        this.onCreate(pDatabase);
   
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion)
    {
        String lSQLCommand = "drop table if exists " + TABLE_ORDER + "s";
        
        pDatabase.execSQL(lSQLCommand);
        
        Log.d(TAG, ": onUpgrade drop table " + TABLE_ORDER);
        
        lSQLCommand = "drop table if exists " + TABLE_PRODUCT + "s";
        
        pDatabase.execSQL(lSQLCommand);
        
        Log.d(TAG, ": onUpgrade drop table " + TABLE_PRODUCT);
        
        lSQLCommand = "drop table if exists " + TABLE_ORDER_PRODUCT;
        
        pDatabase.execSQL(lSQLCommand);

        Log.d(TAG, ": onUpgrade drop table " + TABLE_ORDER_PRODUCT);
        
        this.onCreate(pDatabase);
    }  // void onUpgrade

}  // class DatabaseAdapter

/**
 * 
 */
package com.android.pos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Kai - Ting (Danil) Ko
 *
 */
public class LineItemOpenHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "LineItemDatabase";
    private static final String LINEITEM_TABLE_NAME = "LineItem";
    private static final String LINEITEM_ID = "id";
    private static final String LINEITEM_NAME = "name";
    private static final String LINEITEM_AMOUNT = "amount";
    private static final String LINEITEM_DESCRIPTION = "description";
    private static final String LINEITEM_STATUS = "status";
    private static final String LINEITEM_RESULT = "results";
    
    private static final String LINEITEM_TABLE_CREATE = 
            "CREATE TABLE " + LINEITEM_TABLE_NAME
            + " (" + LINEITEM_ID + " TEXT, " 
                   + LINEITEM_NAME + " TEXT, "
                   + LINEITEM_AMOUNT + " INTEGER, "
                   + LINEITEM_DESCRIPTION + " TEXT, "
                   + LINEITEM_STATUS + " TEXT, "
                   + LINEITEM_RESULT + " TEXT"
            + ");";
    
    public LineItemOpenHelper(Context pContext)
    {
        super(pContext, DATABASE_NAME, null, DATABASE_VERSION);
    }  // LineItemOpenHelper
    
    @Override
    public void onCreate(SQLiteDatabase pDB)
    {
        pDB.execSQL(LINEITEM_TABLE_CREATE);
    }  // void onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   
    }  // void onUpgrade  
}  // class LineItemOpenHelper

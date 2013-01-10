/**
 * 
 */
package com.android.pos;

import java.io.File;

import com.android.pos.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author DANILKO
 *
 */
public class ProductGridViewActivity extends Activity
{
    
    private DataConnectionService mService;

    ProductGridViewAdapter mGridViewAdapter;

    boolean lConfirm = false;

    LineItemInventory mInventory;
    
    String mKeyWordText = "";

    boolean mSearch = false;
    
    SOAPMessageComposer mMessage;
    
    public final static String[] FIELDS = {"_id", DatabaseHelper.TABLE_ID, DatabaseHelper.TABLE_PRODUCT_NAME, DatabaseHelper.TABLE_PRODUCT_DESCRIPTION, DatabaseHelper.TABLE_PRODUCT_PRICE, DatabaseHelper.TABLE_PRODUCT_IMAGE_URL};
    
    static Cursor mCursor = null;
    
    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mCursorbase;
    
    double lProductPrice = 0;
    int lProductQuantity = 0;
    double lProductSubtotal = 0;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doBindService();

        mDatabaseHelper = new DatabaseHelper(this);
        
        mCursorbase = mDatabaseHelper.getWritableDatabase();
        
        mCursor = mCursorbase.query(DatabaseHelper.TABLE_PRODUCT, FIELDS, null, null, null, null, DatabaseHelper.TABLE_ID);
        mCursor.moveToFirst();
        
        setContentView(R.layout.main_gridview);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        
        mGridViewAdapter = new ProductGridViewAdapter(this, mCursor);
        gridview.setAdapter(mGridViewAdapter);
        
        EditText lEditText = (EditText)findViewById(R.id.main_gridview_edittext_search);
        lEditText.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable arg0)
            {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3)
            {
                
            }

            public void onTextChanged(CharSequence pSearchKeyword, int arg1, int arg2,
                    int arg3)
            {
                mGridViewAdapter.getFilter().filter(pSearchKeyword.toString().trim());
            }
            
        });
        
        mGridViewAdapter.setFilterQueryProvider(new FilterQueryProvider()
        {
            public Cursor runQuery(CharSequence pSearchKey)
            {
                if(!pSearchKey.toString().equalsIgnoreCase(""))
                {
                mCursor = mCursorbase.query(DatabaseHelper.TABLE_PRODUCT, FIELDS, 
                        "UPPER(" + DatabaseHelper.TABLE_ID + ")" + " LIKE UPPER('%" + pSearchKey +"%') OR " +
                        "UPPER(" + DatabaseHelper.TABLE_PRODUCT_NAME + ")" + " LIKE UPPER('%" + pSearchKey +"%') OR " +
                        "UPPER(" + DatabaseHelper.TABLE_PRODUCT_DESCRIPTION + ")" + " LIKE UPPER('%" + pSearchKey +"%')",
                        null, null, null, DatabaseHelper.TABLE_ID);
                }  // if
                else
                {
                    mCursor = mCursorbase.query(DatabaseHelper.TABLE_PRODUCT, FIELDS, null, null, null, null, DatabaseHelper.TABLE_ID);
                }  // else
                
                mCursor.moveToFirst();
                return mCursor;
            }  // Cursor runQuery
        }  // new FilterQueryProvider
        );  // setFilterQueryProvider
       
        
        gridview.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                Log.e("On Click",":Click Test");

                lProductPrice = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PRICE)));
                lProductQuantity = 0;
                lProductSubtotal = 0;
                
                ContextThemeWrapper ctw = new ContextThemeWrapper(view.getContext(), R.style.theme_dialog_white);
                
                final View lDialogView = View.inflate(ctw, R.layout.info, null);
   
                AlertDialog.Builder lDialog = new AlertDialog.Builder(ctw);
                lDialog.setTitle(R.string.product_description_label);
                lDialog.setView(lDialogView);

                if(!mCursor.moveToPosition(position))
                {
                    return;
                }

                TextView lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_id);
                lTempText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ID)));

                lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_name);
                lTempText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME)));

                lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_description);
                lTempText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_DESCRIPTION)));

                lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_price);
                lTempText.setText("NT. " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PRICE)));

                File lImage = view.getContext().getFileStreamPath(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL)));

                if(lImage.exists())
                {
                    ImageView lImageView = (ImageView) lDialogView
                            .findViewById(R.id.info_imageview_product_image);
                    Bitmap lTemp = BitmapFactory.decodeFile(lImage.getAbsolutePath());
                    lImageView.setImageBitmap(lTemp);
                }  // if
                
                EditText lEditText = (EditText)lDialogView
                        .findViewById(R.id.info_edittext_product_quantity);
                lEditText.setText("0");
                lEditText.addTextChangedListener(
                                new TextWatcher()
                                {

                                    public void afterTextChanged(Editable arg0)
                                    {
                                    }

                                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                            int arg2, int arg3)
                                    {
                                        
                                    }

                                    public void onTextChanged(CharSequence pQuantity, int arg1, int arg2,
                                            int arg3)
                                    {
                                        String lQuantity = pQuantity.toString().trim();
                                        if(!lQuantity.equalsIgnoreCase(""))
                                        {
                                            lProductQuantity = Integer.parseInt(lQuantity);
                                            
                                        }  // if
                                        else
                                        {
                                            lProductQuantity = 0;
                                        }  // else
                                        
                                        lProductSubtotal = lProductQuantity * lProductPrice;
                                        
                                        ((TextView) lDialogView
                                                .findViewById(R.id.info_textview_product_subtotal)).setText("NT. " + lProductSubtotal);
                                    }  // void onTextChanged
                                }  // new TextWatcher
                                );  // lEditText.addTextChangedListener
                
                lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_subtotal);
                lTempText.setText("NT. " + lProductSubtotal);

                
                lDialog.setPositiveButton(R.string.confirm,
                        new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                if(lProductQuantity == 0)
                                {
                                    return;
                                }  // if

                                String lCurrentProductID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ID));
                                
                                ContentValues lValues = new ContentValues();
                                
                                Log.d("Add Item to Current Order", ": Success");
                                
                                StringBuilder lSQLCommand = new StringBuilder();
                                lSQLCommand.append("SELECT ");
                                lSQLCommand.append(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY + " ");
                                
                                lSQLCommand.append(" FROM ");
                                lSQLCommand.append(DatabaseHelper.TABLE_ORDER_PRODUCT);
                                lSQLCommand.append(" WHERE ");
                                
                                StringBuilder lWhereClause = new StringBuilder();
                                lWhereClause.append(DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID + " = '" + DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT + "'");
                                lWhereClause.append(" AND ");
                                lWhereClause.append(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID + " = '" + lCurrentProductID + "'");
                                
                                lSQLCommand.append(lWhereClause);
                                lSQLCommand.append(";");
                                
                                Cursor lCursor = mCursorbase.rawQuery(lSQLCommand.toString(), null);
                                
                                
                               if(lCursor.getCount() == 0)
                               {
                                lValues.put(DatabaseHelper.TABLE_ID, System.currentTimeMillis());
                                lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID, DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT);
                                lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID, lCurrentProductID);
                                lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY, lProductQuantity);
                                lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL, lProductSubtotal);
                                
                                mCursorbase.insertWithOnConflict(DatabaseHelper.TABLE_ORDER_PRODUCT, null, lValues, SQLiteDatabase.CONFLICT_REPLACE);
                               }  // if
                               else
                               {
                                   lCursor.moveToFirst();
                                   
                                   int lCurrentProductQuantity = lCursor.getInt(lCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY));
                                   
                                   lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY, lProductQuantity + lCurrentProductQuantity);
                                   lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL, lProductSubtotal);
                                   
                                mCursorbase.update(DatabaseHelper.TABLE_ORDER_PRODUCT, lValues, lWhereClause.toString(), null);
                               }  // else
                               
                               lCursor.close();
                               
                               Toast.makeText(getApplicationContext() ,R.string.message_add_to_order, Toast.LENGTH_SHORT).show();
                            }
                        });

                lDialog.setNegativeButton(R.string.cancel,  new DialogInterface.OnClickListener()
                {

                    public void onClick(DialogInterface lDialog, int which)
                    {
                        lDialog.cancel();
                        
                    }
                   
                });
                
                lDialog.show();
            }
            
             
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu)
    {
        MenuInflater lInflater = getMenuInflater();
        lInflater.inflate(R.menu.context_menu, pMenu);

        return true;
    } // boolean onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem pItem)
    {
        switch (pItem.getItemId())
        {
            case R.id.menu_item_changedisplay:
                Intent lIntentGridView = new Intent(this, ProductListViewActivity.class);
                startActivity(lIntentGridView);
                finish();
                
                return true;
            case R.id.menu_item_home:
                Intent lIntentHome = new Intent(this, MainActivity.class);
                startActivity(lIntentHome);
                finish();
                
                return true;
            case R.id.menu_item_investigate_current_order:
                Intent lCurrentOrderListViewIntent = new Intent(this, CurrentOrderListViewActivity.class);
                startActivity(lCurrentOrderListViewIntent);
                
                return true;
            case R.id.menu_item_investigate_completed_order:
                
                return true;
            case R.id.menu_item_preference:
                return true;
            default:
                return super.onOptionsItemSelected(pItem);
        } // switch
    } // boolean onOptionsItemSelected
    
    private boolean mIsBound = false;
    
        private ServiceConnection mConnection = new ServiceConnection()
        {

            public void onServiceConnected(ComponentName pComponentName, IBinder pBinder)
            {
                mService = ((DataConnectionService.POSDataConnectionBinder)pBinder).getService();
                
                
                if(mService != null)
                {
                    Log.d("Bind","BindService");
                }  // if
                else
                {
                    Log.d("Bind","not bind");
                }
            }  // void onServiceConnected

            public void onServiceDisconnected(ComponentName pComponentName)
            {
                mService = null;
            }  // void onServiceDisconnected
        };  // ServiceConnection
        
        private void doBindService()
        {
            startService(new Intent(this, DataConnectionService.class));
            bindService(new Intent(this, DataConnectionService.class), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
            Log.d("Bind","finish bind");
            mInventory = new LineItemInventory();
        }  // void doBindService
        
        private void doUnbindService()
        {
            if(mIsBound)
            {
                unbindService(mConnection);
                stopService(new Intent(this, DataConnectionService.class));
                mIsBound = false;
            }  // if
        }  // void doUnbindService
        
        
        @Override
        public void onDestroy()
        {
            super.onDestroy();
            
            mCursor.close();
            mCursorbase.close();
            mDatabaseHelper.close();
            
            doUnbindService();
            //stopService(new Intent(this, POSDataConnectionService.class));
        }

        public static void startGridViewActivity(Context pContext) {  
            Intent intent = new Intent(pContext, ProductGridViewActivity.class);  
            pContext.startActivity(intent);  
        }  
        
}

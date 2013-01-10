/**
 * 
 */
package com.android.pos;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.support.v4.app.*;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author DANILKO
 *
 */
public class CurrentOrderListViewActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    
    static Cursor mCursor = null;
    
    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mDatabase;
    
    double lProductPrice = 0;
    int lProductQuantity = 0;
    double lProductSubtotal = 0;
    
    TextView lOrderTotal;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle pSavedInstanceState)
    { 
        setContentView(R.layout.current_order_product);
        
        super.onCreate(pSavedInstanceState);
        
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabase = mDatabaseHelper.getReadableDatabase();
        
        String lProductTable = "p";
        String lOrderProductTable = "op";
        
        StringBuilder lSQLStringBuilder = new StringBuilder();
        lSQLStringBuilder.append("SELECT ");
        
        lSQLStringBuilder.append(lProductTable + "." + DatabaseHelper.TABLE_ID + ", ");
        lSQLStringBuilder.append(lProductTable + "." + DatabaseHelper.TABLE_PRODUCT_NAME + ", ");
        lSQLStringBuilder.append(lProductTable + "." + DatabaseHelper.TABLE_PRODUCT_PRICE + ", ");
        lSQLStringBuilder.append(lProductTable + "." + DatabaseHelper.TABLE_PRODUCT_IMAGE_URL + ", ");
        
        lSQLStringBuilder.append(lOrderProductTable + "." + DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY + ", ");
        lSQLStringBuilder.append(lOrderProductTable + "." + DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL);
        
        lSQLStringBuilder.append(" FROM ");
        lSQLStringBuilder.append(DatabaseHelper.TABLE_PRODUCT + " " + lProductTable + ", ");
        lSQLStringBuilder.append(DatabaseHelper.TABLE_ORDER_PRODUCT + " " + lOrderProductTable);

        lSQLStringBuilder.append(" WHERE ");
        lSQLStringBuilder.append(lProductTable + "." + DatabaseHelper.TABLE_ID + " = ");
        lSQLStringBuilder.append(lOrderProductTable + "." + DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID);
        lSQLStringBuilder.append(" AND ");
        lSQLStringBuilder.append(lOrderProductTable + "." + DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID + " = '"+ DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT +"'");
        
        lSQLStringBuilder.append(" ORDER BY ");
        lSQLStringBuilder.append(lProductTable + "." + DatabaseHelper.TABLE_ID + ";");
        
        mCursor = mDatabase.rawQuery(lSQLStringBuilder.toString(), null);
        
        CurrentOrderProductListViewAdapter lAdapter = new CurrentOrderProductListViewAdapter(this, mCursor);
        
        int lSumColumnIndex = 0;
        
        StringBuilder lSQLCommand = new StringBuilder();
        lSQLCommand.append("SELECT SUM(" + DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL + ")");
        lSQLCommand.append(" FROM " + DatabaseHelper.TABLE_ORDER_PRODUCT);
        lSQLCommand.append(" WHERE ");
        lSQLCommand.append(DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID + " = '"+ DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT +"'");
        
        Cursor lCursor = mDatabase.rawQuery(lSQLCommand.toString(), null);
        lCursor.moveToFirst();
        
        lOrderTotal = (TextView)findViewById(R.id.current_order_textview_order_total);
        
        String lTotal = lCursor.getString(lSumColumnIndex);
        
        if(lTotal == null)
        {
            lTotal = "0";
            
            Toast.makeText(getApplicationContext(), R.string.message_no_entry_order, Toast.LENGTH_SHORT).show();
        }  // if
        
        lOrderTotal.setText(getString(R.string.order_total_label) + " NT." + lTotal);
        
        lCursor.close();
        
        ListView lOrderProductListView = (ListView) findViewById(R.id.current_order_listview);
        lOrderProductListView.setAdapter(lAdapter);
        
        lOrderProductListView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {

                
                ContextThemeWrapper lContextThemeWrapper = new ContextThemeWrapper(view.getContext(), R.style.theme_dialog_white);
                
                final View lDialogView = View.inflate(lContextThemeWrapper, R.layout.info, null);

                AlertDialog.Builder lDialog = new AlertDialog.Builder(lContextThemeWrapper);
                lDialog.setTitle(R.string.product_description_label);
                lDialog.setView(lDialogView);

                if(!mCursor.moveToPosition(position))
                {
                    return;
                }  // if

                lProductPrice = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PRICE)));
                lProductQuantity = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY)));
                lProductSubtotal = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL)));;
                
                TextView lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_id);
                lTempText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ID)));

                lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_name);
                lTempText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME)));
                
                lTempText = (TextView) lDialogView
                        .findViewById(R.id.info_textview_product_price);
                lTempText.setText("NT. " + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PRICE)));

                ((TextView) lDialogView
                .findViewById(R.id.info_textview_product_description)).setVisibility(View.GONE);
                ((TextView) lDialogView
                .findViewById(R.id.info_textview_product_description_label)).setVisibility(View.GONE);
                
                ((ImageView) lDialogView
                .findViewById(R.id.info_imageview_product_image)).setVisibility(View.GONE);
                
                
                EditText lEditText = (EditText)lDialogView
                        .findViewById(R.id.info_edittext_product_quantity);
                lEditText.setText(""+lProductQuantity);
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

                            public void onClick(DialogInterface lDialog,
                                    int which)
                            {
                                StringBuilder lWhereClause = new StringBuilder();
                                lWhereClause.append(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID + " = '" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.TABLE_ID)) + "'");
                                lWhereClause.append(" AND ");
                                lWhereClause.append(DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID + " = '"+ DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT +"'");
                                
                                if(lProductQuantity == 0)
                                {
                                    mDatabase.delete(DatabaseHelper.TABLE_ORDER_PRODUCT, lWhereClause.toString(), null);
                                }  // if
                                else
                                {
                                    ContentValues lValues = new ContentValues();
                                    lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY, lProductQuantity);
                                    lValues.put(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL, lProductSubtotal);
                                
                                    mDatabase.update(DatabaseHelper.TABLE_ORDER_PRODUCT, lValues, lWhereClause.toString(), null);
                                }  // else       
                                
                                mCursor.requery();
                                
                                int lSumColumnIndex = 0;
                                
                                StringBuilder lSQLCommand = new StringBuilder();
                                lSQLCommand.append("SELECT SUM(" + DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL + ")");
                                lSQLCommand.append(" FROM " + DatabaseHelper.TABLE_ORDER_PRODUCT);
                                lSQLCommand.append(" WHERE ");
                                lSQLCommand.append(DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID + " = '"+ DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT +"'");
                                
                                Cursor lCursor = mDatabase.rawQuery(lSQLCommand.toString(), null);
                                lCursor.moveToFirst();

                                String lTotal = lCursor.getString(lSumColumnIndex);
                                
                                if(lTotal == null)
                                {
                                    lTotal = "0";
                                }  // if
                                
                                lOrderTotal.setText(getString(R.string.order_total_label) + " NT. " + lTotal);
                                
                                lCursor.close();
                                
                                Toast.makeText(getBaseContext(), R.string.message_add_to_order, Toast.LENGTH_SHORT).show();
                            }  // void onClick
                        }  // new DialogInterface.OnClickListener()
                );  // lDialog.setPositiveButton

                lDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {

                    public void onClick(DialogInterface lDialog, int which)
                    {
                        lDialog.cancel();
                        
                    }  // void onClick
                    
                }  // new DialogInterface.OnClickListener()
                );
                
                lDialog.show();
            }  // void onItemClick
        }  // new OnItemClickListener
        );  // setOnItemClickListener
    }  // public void onCreate
    
    @Override
    public boolean onCreateOptionsMenu(Menu pMenu)
    {
        MenuInflater lInflater = getMenuInflater();
        lInflater.inflate(R.menu.order_menu, pMenu);

        return true;
    } // boolean onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem pItem)
    {
        switch (pItem.getItemId())
        {
            case R.id.menu_item_confirm_order:
                confirmOrders();
                return true;
                
            case R.id.menu_item_clear_all:
                clearAllProductOrders();
                return true;
                
            case R.id.menu_item_back_to_product:
                finish();
                return true;
                
            default:
                return super.onOptionsItemSelected(pItem);
        } // switch
    } // boolean onOptionsItemSelected
    
    public void clearAllProductOrders()
    {

        AlertDialog.Builder lDialogBuilder = new AlertDialog.Builder(this);
        lDialogBuilder.setTitle(R.string.product_description_label);
        lDialogBuilder.setMessage(getString(R.string.dialog_clear_all_warning));
        
        lDialogBuilder
        .setPositiveButton(R.string.dialog_clear_all_confirm, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface pDialogInterface, int lArgument1)
            {
                if(mCursor.getCount() >= 0)
                {
                    StringBuilder lWhereClause = new StringBuilder();
                    lWhereClause.append(DatabaseHelper.TABLE_ORDER_PRODUCT_ORDER_ID + " = '"+ DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_ID_CURRENT +"'");

                    mDatabase.delete(DatabaseHelper.TABLE_ORDER_PRODUCT, lWhereClause.toString(), null);
                
                    mCursor.requery();
                    
                    lOrderTotal.setText(getString(R.string.order_total_label) + " NT. 0");
                }  // if

                Toast.makeText(getBaseContext(), R.string.message_clear_all_complete, Toast.LENGTH_SHORT).show();
            }  // onClick
        }  // new OnClickListener
        )  // lDialog.setPositiveButton
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface pDialogInterface, int pWhich)
            {
               pDialogInterface.cancel();
            }  // void onClick
            
        }  // new DialogInterface.OnClickListener
        );  // lDialog.setNegativeButton
        
        lDialogBuilder.create().show();
    }  // void clearAllProductOrders
    
    public void confirmOrders()
    {
        if(mCursor.getCount() == 0)
        {
            Toast.makeText(getApplicationContext(), R.string.message_no_entry_order, Toast.LENGTH_SHORT).show();
            
            return;
        }  // if
        
        ContextThemeWrapper lContextThemeWrapper = new ContextThemeWrapper(this, R.style.theme_dialog_white);
        
        View lDialogView = View.inflate(lContextThemeWrapper, R.layout.dialog_customer_information, null);

        AlertDialog.Builder lDialogBuilder = new AlertDialog.Builder(lContextThemeWrapper);
        lDialogBuilder.setTitle(R.string.dialog_cusomter_information_title);
        lDialogBuilder.setView(lDialogView);
        
        lDialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface pDialogInterface, int lArgument1)
            {
                // Need to implement sending order
                
                Toast.makeText(getApplicationContext(), R.string.message_send_order, Toast.LENGTH_SHORT).show();
            }  // void onClick
        }  // new OnClickListener
        )  // lDialog.setPositiveButton
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface pDialogInterface, int pWhich)
            {
               pDialogInterface.cancel();
            }  // void onClick
            
        }  // new DialogInterface.OnClickListener
        );  // lDialog.setNegativeButton
        
        lDialogBuilder.create().show();
    }  // void confirmOrders
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
        mCursor.close();
        mDatabase.close();
        mDatabaseHelper.close();
    }  // void onDestroy

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
    {
        // TODO Auto-generated method stub
        
    }

    public void onLoaderReset(Loader<Cursor> arg0)
    {
        // TODO Auto-generated method stub
        
    }
}  // class CurrentOrderListViewActivity

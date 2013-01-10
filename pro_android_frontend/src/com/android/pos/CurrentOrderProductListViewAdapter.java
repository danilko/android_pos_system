/**
 * 
 */
package com.android.pos;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * @author Kai - Ting (Danil) Ko
 *
 */
public class CurrentOrderProductListViewAdapter extends SimpleCursorAdapter
{
    public final static String[] FIELDS = {
        DatabaseHelper.TABLE_ID,
        DatabaseHelper.TABLE_PRODUCT_NAME,
        DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY,
        DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL};
    
    private final Activity mActivity;
    
    public CurrentOrderProductListViewAdapter(Activity pActivity, Cursor pCursor)
    {
        super(pActivity, R.layout.current_order_product_listview_content, pCursor, FIELDS, null);
        
        mActivity = pActivity;
    }  // Constructor

    @Override
    public void bindView(View pView, Context pContext, Cursor pCursor)
    {
        LineItemView lLineItemView = (LineItemView) pView.getTag();
        
        File lImage = mActivity.getBaseContext().getFileStreamPath(pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL)));

        if(lImage.exists())
        {
            Bitmap lTemp = BitmapFactory.decodeFile(lImage.getAbsolutePath());
            lLineItemView.mImage.setImageBitmap(lTemp);

        }  // if
        
        lLineItemView.mDescription.setText(
            pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ID))
            + "\n"
            + pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME))
            + "\n"
            + pContext.getResources().getString(R.string.product_quantity_label)
            +  pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY))
            + "\n"
            + pContext.getResources().getString(R.string.product_subtotal_label)
            +  pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL))
            );
    }  // void bindView

    @Override
    public View newView(Context pContext, Cursor pCursor, ViewGroup pParent)
    {
        LineItemView lLineItemView = null;
        
        LayoutInflater lInflater = mActivity.getLayoutInflater();
        View lView = lInflater.inflate(R.layout.current_order_product_listview_content, null);
        
        lLineItemView = new LineItemView();
        lLineItemView.mImage = (ImageView) lView.findViewById(R.id.current_order_product_content_imageView_product);
        lLineItemView.mDescription = (TextView) lView.findViewById(R.id.current_order_product_content_textView_description);
        
        File lImage = mActivity.getBaseContext().getFileStreamPath(pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL)));

        lView.setTag(lLineItemView);
        
        if(lImage.exists())
        {
            Bitmap lTemp = BitmapFactory.decodeFile(lImage.getAbsolutePath());
            lLineItemView.mImage.setImageBitmap(lTemp);
        }
        
        lLineItemView.mDescription.setText(
                pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ID))
                + "\n"
                + pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME))
                + "\n"
                + pContext.getResources().getString(R.string.product_quantity_label)
                +  pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_QUANTITY))
                + "\n"
                + pContext.getResources().getString(R.string.product_subtotal_label)
                +  pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ORDER_PRODUCT_PRODUCT_SUBTOTAL))
                );
            
        return lView;
    }  // View newView
    
    protected static class LineItemView
    {
        protected ImageView mImage;
        protected TextView mDescription;
    }  // class LineItemView
}  // class CurrentOrderProductListViewAdapter

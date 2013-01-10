/**
 * 
 * @author Kai - Ting (Danil) Ko
 * 
 */
package com.android.pos;

import java.io.File;

import com.android.pos.R;

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

public class ProductGridViewAdapter extends SimpleCursorAdapter {
    private Activity mActivity;

    public ProductGridViewAdapter(Activity pActivity, Cursor pCursor) {
        
        super(pActivity, R.layout.main_gridview_content, pCursor, MainActivity.FIELDS, null);
        
        mActivity = pActivity;
    }  // GridViewAdapter

    @Override
    public void bindView(View pView, Context pContext, Cursor pCursor)
    {
        LineItemGridView lLineItemGridView= (LineItemGridView) pView.getTag();
        
        File lImage = mActivity.getBaseContext().getFileStreamPath(pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL)));

        if(lImage.exists())
        {
            Bitmap lTemp = BitmapFactory.decodeFile(lImage.getAbsolutePath());
            lLineItemGridView.mImage.setImageBitmap(lTemp);

        }  // if
        
        lLineItemGridView.mID.setText(
            pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ID))
            + "\n"
            + pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME)));
    }  // void bindView

    @Override
    public View newView(Context pContext, Cursor pCursor, ViewGroup pParent)
    {
        LineItemGridView lLineItemView = null;
        
        LayoutInflater lInflater = mActivity.getLayoutInflater();
        View lView = lInflater.inflate(R.layout.main_gridview_content, null);
        
        lLineItemView = new LineItemGridView();
        lLineItemView.mID = (TextView) lView.findViewById(R.id.main_gridview_textview_product_ID);
        lLineItemView.mImage = (ImageView) lView.findViewById(R.id.main_gridview_imageview_product_image);
        
        File lImage = mActivity.getBaseContext().getFileStreamPath(pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL)));

        lView.setTag(lLineItemView);
        
        if(lImage.exists())
        {
            Bitmap lTemp = BitmapFactory.decodeFile(lImage.getAbsolutePath());
            lLineItemView.mImage.setImageBitmap(lTemp);
        }
        
        lLineItemView.mID.setText(
            pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_ID))
            + "\n"
            + pCursor.getString(pCursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME)));
    
        return lView;
    }  // View newView
    
    protected static class LineItemGridView
    {
        protected TextView mID;
        protected ImageView mImage;
    }  // class LineItemView
}  // class GridViewAdapter

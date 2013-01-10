/**
 * @author Kai - Ting (Danil) Ko
 * 
 * 
 */

package com.android.pos;

import com.android.pos.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity
{

    private DataConnectionService mService;

    public final static String[] FIELDS = {"_id", DatabaseHelper.TABLE_ID, DatabaseHelper.TABLE_PRODUCT_NAME, DatabaseHelper.TABLE_PRODUCT_DESCRIPTION, DatabaseHelper.TABLE_PRODUCT_PRICE, DatabaseHelper.TABLE_PRODUCT_IMAGE_URL};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.main);
        
        super.onCreate(savedInstanceState);

        ImageView lImageView = (ImageView) findViewById(R.id.main_home_imageview_product_enterance);
        lImageView.setOnClickListener(new OnClickListener(){

            public void onClick(View pView)
            {
                Intent lIntent = new Intent(MainActivity.this, ProductGridViewActivity.class);
                startActivity(lIntent);
                MainActivity.this.finish();
                
            }});
        
        TextView lTextView = (TextView) findViewById(R.id.main_home_textview_product_enterance);
        lTextView.setOnClickListener(new OnClickListener(){

            public void onClick(View pView)
            {
                Intent lIntent = new Intent(MainActivity.this, ProductGridViewActivity.class);
                startActivity(lIntent);
                MainActivity.this.finish();
                
            }});
        
        
        doBindService();

    }  // void onCreate

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
                Intent lIntent = new Intent(this, ProductGridViewActivity.class);
                startActivity(lIntent);
                finish();
                
                return true;
            case R.id.menu_item_home:
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
            
            doUnbindService();
        }  // void onDestroy
} // class AndriodPOSActivity
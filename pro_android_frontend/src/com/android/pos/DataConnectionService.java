/**
 * 
 */
package com.android.pos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @author DANILKO
 *
 */
public class DataConnectionService extends Service
{
    private final POSDataConnectionBinder mBinder = new POSDataConnectionBinder();
    
    String mImageURL = "http://10.0.2.2:8080/AndriodWebsite";
    String mServiceURL = "http://10.0.2.2:8080/AndriodWebsite/Convert.asmx";
    String mNamespace = "http://tempuri.org/";
    
    LineItemInventory mInventory;

    DefaultHttpClient mHttpClient;
    
    SOAPMessageComposer mMessage;
    
    private DataUpdater mUpdater;
    

    @Override
    public synchronized void onCreate()
    {

        Log.w("Service", ": Create");
        
        HttpParams lHttpParameters = new BasicHttpParams();
        
        int lTimeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(lHttpParameters, lTimeoutConnection);
       
        int lTimeoutSocket = 3000;
        HttpConnectionParams.setSoTimeout(lHttpParameters, lTimeoutSocket);
        
        mHttpClient = new DefaultHttpClient(lHttpParameters);
        
        mInventory = new LineItemInventory();
        
        mMessage = new SOAPMessageComposer(mServiceURL, mNamespace, "1.1");
        
        String lMethod = "SQLSelect";
        String [] lParameterNames = new String [1];
        lParameterNames [0] = "pParameter";
        
        String [] lParameterValues = new String [1];
        lParameterValues [0] = "20";
        
        mMessage.setMethod(lMethod);
        mMessage.setParameterNames(lParameterNames);
        mMessage.setParameterValues(lParameterValues);
        
        onSynchronizeData();
        
        mUpdater = new DataUpdater();
        
        Log.w("Service", ": Start Service");
        
 
      
    }  // void onCreate
    
    @Override
    public int onStartCommand(Intent pIntent, int pFlags, int pStartId)
    {
        Log.w("Service", ": Start Service Command");
        
        if(!mUpdater.isRunning)
        {
            mUpdater.start();
        }  // if
        
        return START_STICKY;
    }  // int onStartCommand

    @Override
    public synchronized void onDestroy()
    {
        if(mUpdater.isRunning)
        {
            mUpdater.interrupt();
        }  // if
        
        mUpdater = null;
        
        Log.w("Service", ": Stop");
    }  // public void onDestroy
    
    public void onSynchronizeData()
    {
        HttpPost lHttpPost = new HttpPost(mServiceURL);

        StringEntity lStringEntity = null;
        try
        {
            lStringEntity = new StringEntity(mMessage.getMessage(), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        lHttpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
        lHttpPost.setHeader("SOAPAction", "\"" + mMessage.getServiceNamespace() + mMessage.getMethod() + "\"");
        lHttpPost.setEntity(lStringEntity);
        
        BasicHttpResponse lHttpResponse = null;
        
        try
        {
            lHttpResponse = (BasicHttpResponse) mHttpClient.execute(lHttpPost);
        } catch (ClientProtocolException e)
        {
            Log.d("Connection", ": Cannot Connect");

            e.printStackTrace();
            
            return;
        }
        catch(ConnectTimeoutException lException)
        {
            Log.d("Connection", ": Connection Timeout");
            
            return;
        }  // catch
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (lHttpResponse != null)
        {

            if(lHttpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {
                Log.d("Connection", ": Cannot Connect");
                return;
            }  // if
            
            HttpEntity lReturnEntity = lHttpResponse.getEntity();

            if (lReturnEntity != null)
            {
                try
                {

                    InputStream lStream = lReturnEntity.getContent();

                    getInputData(lStream);

                    lStream.close();

                } catch (IOException pException)
                {

                } //
            } // if
        } // if
        else
        {
            Log.e("HttpStatus", "Failed");
        } // else
    }  // void onSynchronizeData
    
    public LineItemInventory getInventory()
    {
        return mInventory;
    }  // void getInventory
    
    /**
     * Convert the input InputStream and decode it according to the setup
     * 
     * @param pInputStream
     * @return The decoded message from the input InputStream
     */
    public void getInputData(InputStream pInputStream)
    {
        try
        {
            SAXParser lParser = SAXParserFactory.newInstance().newSAXParser();
            XMLReader lXMLReader = lParser.getXMLReader();
            SOAPSAXHandler lHandler = new SOAPSAXHandler(mInventory);
            lXMLReader.setContentHandler(lHandler);
            lXMLReader.parse(new InputSource(pInputStream));
        } // try
        catch (Exception pException)
        {
            pException.printStackTrace();
        } // catch
    } // void getInputData
    
    @Override
    public IBinder onBind(Intent arg0)
    {
        Log.w("onBind", "Test OnBind");
        
        return mBinder;
    }  // IBinder onBind

    public class POSDataConnectionBinder extends Binder
    {
        DataConnectionService getService()
        {
            return DataConnectionService.this;
        }  //  POSDataConnectionService getService
    }  // POSDataConnectionBinder
    
    public void downloadImage(String pImage)
    {
        File lImage = this.getBaseContext().getFileStreamPath(pImage);
        
        if(lImage.exists())
        {
            return;
        }
        else
        {
            try
            {
                URL lURL = new URL(mImageURL + "/Grace_Optical_Images/" + pImage);
                URLConnection lURLConnection = lURL.openConnection();
                lURLConnection.connect();
                InputStream lInput = new BufferedInputStream(lURLConnection.getInputStream());

                Bitmap lBitmap = BitmapFactory.decodeStream(lInput);
                lInput.close();
                
                if(!lImage.createNewFile())
                {
                    Log.d("Image File Creation", ": Failed for " + lImage.getName());
                }  // if
                
                FileOutputStream lOutputStream = new FileOutputStream(lImage);
                lBitmap.compress(Bitmap.CompressFormat.PNG, 100, lOutputStream);
                lOutputStream.flush();
            }
            catch(IOException pException)
            {
                
            }  // pException
        }  // else
        
    }  // void downloadImage
    
    // DataConnection
    class DataUpdater extends Thread
    {
        private static final long DELAY = 3000;
        private boolean isRunning = false;
        
        public DataUpdater()
        {
            super("DataUpdater");
        }  // DataUpdater
        
        @Override
        public void run()
        {
            isRunning = true;
            while (isRunning)
            {
                try
                {
                    // Open Database
                    DatabaseHelper lDatabaseHelper = new DatabaseHelper(DataConnectionService.this);
                    SQLiteDatabase lDatabase = lDatabaseHelper.getWritableDatabase();
                    
                    onSynchronizeData();
                    
                    ContentValues lValues = new ContentValues();
                    
                    List <LineItem> lList = mInventory.getList();
                    for(LineItem lTemp : lList)
                    {
                        lValues.put(DatabaseHelper.TABLE_ID, lTemp.getID());
                        lValues.put(DatabaseHelper.TABLE_PRODUCT_NAME, lTemp.getName());
                        lValues.put(DatabaseHelper.TABLE_PRODUCT_DESCRIPTION, lTemp.getDescription());
                        //lValues.put(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL, lTemp.getDescription());
                        lValues.put(DatabaseHelper.TABLE_PRODUCT_PRICE, lTemp.getPrice());
                        //lValues.put(DatabaseHelper.TABLE_STATUS, lTemp.getStatus().toString());
                        //lValues.put(DatabaseHelper.TABLE_STATUS, lTemp.getResult().toString());
                        
                        String lImage = lTemp.getImage();
                        // Download Image
                        downloadImage(lImage);
                        lValues.put(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL, lImage);
                       
                        lDatabase.insertWithOnConflict(DatabaseHelper.TABLE_PRODUCT, null, lValues, SQLiteDatabase.CONFLICT_REPLACE);
                       
                        
                    }  // for
                    
                    // Close Database
                    lDatabase.close();
                    lDatabaseHelper.close();

                    Thread.sleep(DELAY);
                }  // try 
                catch (InterruptedException e)
                {
                    isRunning = false;
                }  // catch
            }  // while
        }  // void run
        
        public boolean isRunning()
        {
            return isRunning;
        }  // boolean isRunning
    }  // class DataUpdater
}  // class DataConnectionService

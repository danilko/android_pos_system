/**
 * 
 */
package com.android.pos;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


/**
 * @author Kai - Ting (Danil) Ko
 *
 */
public class SOAPSAXHandler extends DefaultHandler
{
    LineItemInventory mInventory;
    
    private LineItem lCurrentItem;
    
    private StringBuilder lCurrentInformation;
    
    /**
     * Empty constructor
     */
    public SOAPSAXHandler(LineItemInventory pInventory)
    {
        mInventory = pInventory;
        mInventory.clearLineItem();
        
        lCurrentItem = null;
        
        lCurrentInformation = null;
    }  // SOAPSAXHandler
    
    @Override
    public void startDocument()
    {
    }  // void startDocument
    
    @Override
    public void endDocument()
    {
    }   // void endDocument
    
    public void startElement(String pURI, String pName, String pQName, Attributes pAttributes)
    {
        if(pName == null)
        {
            return;
        }  // if
        
        if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT))
        {   
            lCurrentItem = new LineItem();
            
            lCurrentItem.setStatus(LineItemStatus.PREPARE_TO_SYNCHRONIZE);
            lCurrentItem.setResult(LineItemStatus.PREPARE_TO_SYNCHRONIZE);
            
            mInventory.addLineItem(lCurrentItem);
        }  // if
        else if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_ID) ||
                pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_NAME) ||
                pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_DESCRIPTION) ||
                pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_PRICE) ||
                pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL))
        {
            lCurrentInformation = new StringBuilder();
        }  // else if
    }  // void startElement
    
    public void endElement(String pURI, String pName, String pQName)
    {
        if(pName == null)
        {
            return;
        }  // if
        
        if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT))
        {
            lCurrentItem.setStatus(LineItemStatus.UPDATE);
            lCurrentItem.setResult(LineItemStatus.PREPARE_TO_SYNCHRONIZE);

            lCurrentItem = null;
        }  // if
        else if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_ID))
        {
            lCurrentItem.setID(lCurrentInformation.toString());
        }  // else if
        else if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_NAME))
        {
            lCurrentItem.setName(lCurrentInformation.toString());
        }  // else if
        else if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_DESCRIPTION))
        {
            lCurrentItem.setDescription(lCurrentInformation.toString());
        }  // else if
        else if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_PRICE))
        {
            try
            {
                
                lCurrentItem.setPrice(Double.parseDouble(lCurrentInformation.toString()));
            }
            catch(NumberFormatException pException)
            {
                lCurrentItem.setPrice(0.0);
            }  // catch
        }  // else if
        else if(pName.equalsIgnoreCase(DatabaseHelper.TABLE_PRODUCT_IMAGE_URL))
        {
            lCurrentItem.setImage(lCurrentInformation.toString());
        }  // else if
        
        lCurrentInformation = null;
    }  // void endElement
    
    public void characters(char [] pCharacters, int pStart, int pLength)
    {
        if(lCurrentInformation != null)
        {
            for(int lIndex = pStart; lIndex < pStart + pLength; lIndex++)
            {
                lCurrentInformation.append(pCharacters[lIndex]);
            }  // for
        }  // if
    }  // void characters
}  // class SOAPSAXHandler

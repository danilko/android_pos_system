/**
 * 
 */
package com.android.pos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DANILKO
 *
 */
public class LineItemInventory
{
    private ArrayList <LineItem> mLineItemList;
    
    private int mLineItemAmount;
    
    private int mLineItemCounter = -1;
    
    /**
     * Constructor to initialize variables
     */
    public LineItemInventory()
    {
        mLineItemAmount = 0;
        mLineItemCounter = -1;
        mLineItemList = new ArrayList<LineItem>();
    }  // LineItemInventory
    
    /**
     * Added the inputed LineItem object into the inventory or update values if the LineItem object is already in the inventory
     * @param pLineItem The inputed LineItem object that needs to be added into the inventory
     */
    public void addLineItem(LineItem pLineItem)
    {
        if(mLineItemAmount == 0)
        {
            mLineItemList.add(pLineItem);
            mLineItemAmount++;
            
            return;
        }  // if
        
        LineItem lResultLineItem = getLineItem(pLineItem.getID());
        
        if(lResultLineItem != null)
        {
            lResultLineItem.setName(pLineItem.getName());
            lResultLineItem.setDescription(pLineItem.getDescription());
            lResultLineItem.setPrice(pLineItem.getPrice());
        }  // if
        else
        {
            mLineItemList.add(pLineItem);
            mLineItemAmount++;
        }  // else
    }  // void addLineItem
    
    public void search()
    {
        mLineItemList.remove(0);
        mLineItemList.remove(0);
        mLineItemList.remove(0);
        mLineItemList.remove(0);
        mLineItemList.remove(0);
        mLineItemList.remove(2);
        mLineItemList.remove(2);
    }  // void search
    
    public void clearLineItem()
    {
        mLineItemList.clear();
    }  // void clearLineItem
    
    /**
     * Get the LineItem object from the inventory through the inputed ID
     * 
     * @param pID The LineItem object that has the inputed ID or return null if such object does not exist
     */
    public LineItem getLineItem(String pID)
    {
        LineItem lResultLineItem = null;
        
        for(LineItem lTempLineItem : mLineItemList)
        {
            if(lTempLineItem.getID().equalsIgnoreCase(pID))
            {
                lResultLineItem = lTempLineItem;
            }  // if
        }  // for
        
        return lResultLineItem;
    }  // LineItem getLineItem
    
    /**
     * Reset the counter to point before the first element
     */
    public void restCounter()
    {
        mLineItemCounter = -1;
    }  // void resetIterator
    
    /**
     * Get the next LineItem object by increasing the counter
     */
    public LineItem getNextLineItem()
    {
        if((mLineItemCounter + 1) < mLineItemAmount)
        {
            mLineItemCounter++;
            
            return mLineItemList.get(mLineItemCounter);
        }  // if
        
        return null;
    }  // void getNextLineItem
    
    public List <LineItem> getList()
    {
        return mLineItemList;
    }
}  // class LineItemInventory

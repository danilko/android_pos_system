package com.android.pos;
/**
 * 
 */

/**
 * @author Kai - Ting (Danil) Ko
 *
 */
public class LineItem
{
    private String mID;
    private String mName;
    private String mDescription;
    private double mPrice;
    private String mImage;
    private LineItemStatus mStatus;
    private LineItemStatus mResult;
    
    /**
     * Constructor to initialize variables
     */
    public LineItem()
    {
        mID = "";
        mName = "";
        mDescription = "";
        mPrice = 0.0;
        mImage = "";
    }  // AbstractLineItem
    
    /**
     * Set the inputed ID as the ID for the LineItem
     * @param pID The inputed ID for the LineItem
     */
    public void setID(String pID)
    {
        mID = pID;
    }  // void setID
    
    /**
     * Get the ID of the LineItem
     * @return The ID for the LineItem
     */
    public String getID()
    {
        return mID;
    }  // String getID
    
    /**
     * Set the inputed name as the name for the LineItem
     * @param pName The inputed name for the LineItem
     */
    public void setName(String pName)
    {
        mName = pName;
    }  // void setName
    
    /**
     * Get the name of the LineItem
     * @return The name for the LineItem
     */
    public String getName()
    {
        return mName;
    }  // String getName
    
    /**
     * Set the inputed description as the description for the LineItem
     * @param pName The inputed description for the LineItem
     */
    public void setDescription(String pDescription)
    {
        mDescription = pDescription;
    }  // void setDescription
    
    /**
     * Get the description of the LineItem
     * @return The description for the LineItem
     */
    public String getDescription()
    {
        return mDescription;
    }  // String getDescription
    
    public void setImage(String pImage)
    {
        mImage = pImage;
    }  // void setImage
    
    public String getImage()
    {
        return mImage;
    }  // String getImage
    
    /**
     * Set the inputed price as the amount for the LineItem
     * @param pPrice The inputed price for the LineItem
     */
    public void setPrice(double pPrice)
    {
        mPrice = pPrice;
    }  // void setPrice
    
    /**
     * Get the price of the LineItem
     * @return The price for the LineItem
     */
    public double getPrice()
    {
        return mPrice;
    }  // double getPrice
    
    /**
     * Set the inputed status as the amount for the LineItem
     * @param pName The inputed status for the LineItem
     */
    public void setStatus(LineItemStatus pStatus)
    {
        mStatus = pStatus;
    }  // void setStatus
    
    /**
     * Get the status of the LineItem
     * @return The status for the LineItem
     */
    public LineItemStatus getStatus()
    {
        return mStatus;
    }  // LineItemStatus getAmount
    
    /**
     * Set the inputed result as the amount for the LineItem
     * @param pName The inputed result for the LineItem
     */
    public void setResult(LineItemStatus pResult)
    {
        mResult = pResult;
    }  // void setResult
    
    /**
     * Get the result of the LineItem
     * @return The result for the LineItem
     */
    public LineItemStatus getResult()
    {
        return mResult;
    }  // LineItemStatus getResult
}  // class AbstractLineItem

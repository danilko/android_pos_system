/**
 * 
 */
package com.android.pos;

/**
 * @author Kai - Ting (Danil) Ko
 * 
 * Computed message based on the inputed settings
 */
public abstract class AbstractMessageComposer
{
    protected String mServiceAddress;
    protected String mServiceNamespace;
    
    protected String mMethodName;
    protected String [] mParameterValues;
    
    protected String mComputedMessage;
    
    /**
     * Constructor that initializes variables based on the inputs
     * @param pServiceAddress The service address that will be used in the computed message
     * @param pServiceNamespace The service namespace that will be used in the computed message
     */
    public AbstractMessageComposer(String pServiceAddress, String pServiceNamespace)
    {
        mServiceAddress = pServiceAddress;
        mServiceNamespace = pServiceNamespace;
        
        mMethodName = null;
        mParameterValues = null;
        mComputedMessage = null;
    }  // AbstractMessageHandler
    
    /**
     * Get the computed header for the message with the current settings
     * @return The header for the computed message 
     */
    protected abstract String getHeader();
    
    /**
     * Get the computed body for the message with the current settings
     * @return The body for the computed message 
     */
    protected abstract String getBody();
    
    /**
     * Get the computed footer for the message with the current settings
     * @return The footer for the computed message 
     */
    protected abstract String getFooter();
    
    /**
     * Set the service address that will be used in the computed message
     * @param pServiceAddress The service address that will be used in the computed message
     */
    public void setServiceAddress(String pServiceAddress)
    {
        mServiceAddress = pServiceAddress;
    }  // void setServiceAddress
    
    /**
     * Get the service address that will be used in the computed message
     * @return The service address that will be used in the computed message
     */
    public String getServiceAddress()
    {
        return mServiceAddress;
    }  // String getServiceAddress
    
    /**
     * Set the service namespace that will be used in the computed message
     * @param pServiceNamespace The service namespace that will be used in the computed message
     */
    public void setServiceNamespace(String pServiceNamespace)
    {
        resetComputedMessage();
        mServiceNamespace = pServiceNamespace;
    }  // void setServiceNamespace
    
    /**
     * Get the service namespace that will be used in the computed message
     * @return The service namespace that will be used in the computed message
     */
    public String getServiceNamespace()
    {
        return mServiceNamespace;
    }  // String getServiceNamespace
    
    /**
     * Set the service method name that will be used in the computed message
     * @param pMethodName The service method name that will be used in the computed message
     */
    public void setMethod(String pMethodName)
    {
        resetComputedMessage();
        mMethodName = pMethodName;
    }  // void setMethod
    
    /**
     * The service method name that will be used in the computed message
     * @return The service method name that will be used in the computed message
     */
    public String getMethod()
    {
        return mMethodName;
    }  // String getMethod
    
    /**
     * Set the parameter values for the service method that will be used in the computed message
     * @param pParameterValues The parameters for the service method that will be used in the computed message
     */
    public void setParameterValues(String [] pParameterValues)
    {
        resetComputedMessage();
        mParameterValues = pParameterValues;
    }  // void setParameterValues
    
    /**
     * Get the parameter values for the service method that will be used in the computed message
     * @return The parameter values for the service method that will be used in the computed message
     */
    public String [] getParameterValues()
    {
        return mParameterValues;
    }  // String [] getParameterValues
    
    /**
     * Reset the computed message;
     */
    public void resetComputedMessage()
    {
        mComputedMessage = null;
    }  // void resetComputedMessage
    
    /**
     * Get the computed message with current settings
     * @return The computed message with current settings
     */
    public abstract String getMessage();
}  // abstract class AbstractMessageComposer

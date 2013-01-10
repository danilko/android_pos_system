/**
 * 
 */
package com.android.pos;

/**
 * @author DANILKO
 *
 * Computed SOAP 1.1 message with the inputed settings.
 */
public class SOAPMessageComposer extends AbstractMessageComposer
{
    protected String [] mParameterNames;

    public SOAPMessageComposer(String pServiceAddress, String pServiceNamespace, String SOAPVersion)
    {
        super(pServiceAddress, pServiceNamespace);
        
        mParameterNames = null;
    }  // SOAPMessageComposer

    @Override
    protected String getHeader()
    {
        StringBuilder lRequestData = new StringBuilder();
        // TODO Auto-generated method stub
        lRequestData.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        lRequestData.append(" <soap:Envelope");
        lRequestData.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        lRequestData.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
        lRequestData.append(" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        lRequestData.append(" <soap:Body>");
        
        return lRequestData.toString().trim();
    }  // String getHeader

    @Override
    protected String getBody()
    {
        if (mParameterNames != null)
        {
            StringBuilder lBody = new StringBuilder();
            
            int lIndex = 0;
            
            while(lIndex < mParameterNames.length)
            {
                lBody.append("<" + mMethodName + " xmlns=\"" + mServiceNamespace
                    + "\">");
                lBody.append(" <" + mParameterNames [lIndex] + ">");
                lBody.append(mParameterValues[lIndex]);
                lBody.append("</" + mParameterNames [lIndex] + ">");
                lBody.append("</" + mMethodName + ">");
                
                lIndex++;
            }  // while
            
            return lBody.toString().trim();
        } // if
        
        return "";
    }  // String getBody

    @Override
    protected String getFooter()
    {
        return "</soap:Body></soap:Envelope>";
    }  // String getFooter();

    /**
     * Set the parameter names for the computed message
     * @param pParameterNames Set the parameter names for the computed message
     */
    public void setParameterNames(String [] pParameterNames)
    {
        mComputedMessage = null;
        
        mParameterNames = pParameterNames;
    }  // void setParameterNames
    
    /**
     * Get the parameter names for the computed message
     * @return The parameter names for the computed message
     */
    public String [] getParameterNames()
    {
       return mParameterNames;
    }  // String [] getParameterNames
    
    @Override
    public String getMessage()
    {
        if(mComputedMessage == null)
        {
            StringBuilder lRequestData = new StringBuilder();
    
            lRequestData.append(getHeader());
            lRequestData.append(getBody());
            lRequestData.append(getFooter());

            mComputedMessage = lRequestData.toString().trim();
        }  // if
        
            return mComputedMessage;
    }  // String getMessage
}  // class SOAPMessageComposer

package entities;

import com.google.gson.annotations.SerializedName;

public class CodeCompResponse
{
	  	@SerializedName("fault")
	    private CodeCompErrorFault fault;
	    @SerializedName("hasError")
	    private boolean hasError;
	    @SerializedName("data")
	    private String data;
	    
	public CodeCompResponse(){}
    public CodeCompErrorFault getFault() 
    { 
    	return this.fault; 
    }
    
    public void setFault(CodeCompErrorFault fault)
    {
    	this.fault = fault;
    }
    
    public boolean getHasError()
    {
    	return hasError;
    }
    public void setHasError(boolean hasError)
    {
    	this.hasError = hasError;
    }
    
    public String getData()
    {
    	return data;
    }
    public void setData(String data)
    {
    	this.data = data;
    }    
}







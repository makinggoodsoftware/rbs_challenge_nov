package entities;

import com.google.gson.annotations.SerializedName;

public class CodeCompErrorFault {

	
	@SerializedName("FaultMessage")
	private String FaultMessage;
	@SerializedName("FaultCode")
	private int FaultCode;
	
	public CodeCompErrorFault(){}
	
	public String getFaultMessage() {
		return FaultMessage;
	}
	public void setFaultMessage(String faultMessage) {
		FaultMessage = faultMessage;
	}
	public int getFaultCode() {
		return FaultCode;
	}
	public void setFaultCode(int faultCode) {
		FaultCode = faultCode;
	}
	
	
}

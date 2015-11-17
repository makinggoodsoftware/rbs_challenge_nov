package entities;

import com.google.gson.annotations.SerializedName;
public class ParticipantParameter {
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("password")
    private String password;
    
    public ParticipantParameter(){}

    public ParticipantParameter(String participantName, String password) {
        this.UserName = participantName;
        this.password = password;
    }
    
    public String getUserName() {
    	return UserName;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.UserName = userName;
	}
}
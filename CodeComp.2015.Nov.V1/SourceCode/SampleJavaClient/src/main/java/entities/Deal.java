package entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import enums.SuitType;

public class Deal
{    
	public Deal(){}
    public int getDealNumber() {
		return dealNumber;
	}

	public void setDealNumber(int dealNumber) {
		this.dealNumber = dealNumber;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public SuitType getSuitType() {
		return suitType;
	}

	public void setSuitType(SuitType suitType) {
		this.suitType = suitType;
	}

	public String getDealWinner() {
		return dealWinner;
	}

	public void setDealWinner(String dealWinner) {
		this.dealWinner = dealWinner;
	}
	public List<DealCard> getDealCards() {
		return dealCards;
	}

	
	public void setDealCards(List<DealCard> dealCards) {
		this.dealCards = dealCards;
	}
	@SerializedName("DealNumber")
	private int dealNumber;
	@SerializedName("Initiator")
	private String initiator; 
	@SerializedName("SuitType")
	private SuitType suitType;
	@SerializedName("DealCards")
	private List<DealCard> dealCards;
	@SerializedName("DealWinner")
	private String dealWinner;
}


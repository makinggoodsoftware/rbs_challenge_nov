package entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RoundParameters
{
  
	public RoundParameters(){}
	public int getRoundId() {
		return roundId;
	}
	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}
	public int getInitiationPhaseInSeconds() {
		return initiationPhaseInSeconds;
	}
	public void setInitiationPhaseInSeconds(int initiationPhaseInSeconds) {
		this.initiationPhaseInSeconds = initiationPhaseInSeconds;
	}
	public int getPassingPhaseInSeconds() {
		return passingPhaseInSeconds;
	}
	public void setPassingPhaseInSeconds(int passingPhaseInSeconds) {
		this.passingPhaseInSeconds = passingPhaseInSeconds;
	}
	public int getDealingPhaseInSeconds() {
		return dealingPhaseInSeconds;
	}
	public void setDealingPhaseInSeconds(int dealingPhaseInSeconds) {
		this.dealingPhaseInSeconds = dealingPhaseInSeconds;
	}
	public int getFinishingPhaseInSeconds() {
		return finishingPhaseInSeconds;
	}
	public void setFinishingPhaseInSeconds(int finishingPhaseInSeconds) {
		this.finishingPhaseInSeconds = finishingPhaseInSeconds;
	}
	public int getNumberOfCardsTobePassed() {
		return numberOfCardsTobePassed;
	}
	public void setNumberOfCardsTobePassed(int numberOfCardsTobePassed) {
		this.numberOfCardsTobePassed = numberOfCardsTobePassed;
	}
	public List<CardPoint> getCardPoints() {
		return cardPoints;
	}
	public void setCardPoints(List<CardPoint> cardPoints) {
		this.cardPoints = cardPoints;
	}
	@SerializedName("RoundId")
	private int roundId;
	@SerializedName("InitiationPhaseInSeconds")
	private int initiationPhaseInSeconds;
	@SerializedName("PassingPhaseInSeconds")
	private int passingPhaseInSeconds;
	@SerializedName("DealingPhaseInSeconds")
	private int dealingPhaseInSeconds;
	@SerializedName("FinishingPhaseInSeconds")
	private int finishingPhaseInSeconds;
	@SerializedName("NumberOfCardsTobePassed")
	private int numberOfCardsTobePassed;
	@SerializedName("CardPoints")
	private List<CardPoint> cardPoints;
	
	
}
package com.mgs.rbsnov.client.entities;

import com.google.gson.annotations.SerializedName;
import com.mgs.rbsnov.client.enums.GameInstanceState;
import com.mgs.rbsnov.client.enums.HeartsGameInstanceState;
import com.mgs.rbsnov.client.enums.RoundState;

import java.util.ArrayList;
import java.util.List;

public class GameStatus
{	
	
	public String getCurrentGameId() {
		return currentGameId;
	}

	public void setCurrentGameId(String currentGameId) {
		this.currentGameId = currentGameId;
	}

	public GameInstanceState getCurrentGameState() {
		return currentGameState;
	}

	public void setCurrentGameState(GameInstanceState currentGameState) {
		this.currentGameState = currentGameState;
	}

	public int getCurrentRoundId() {
		return currentRoundId;
	}

	public void setCurrentRoundId(int currentRoundId) {
		this.currentRoundId = currentRoundId;
	}

	public RoundState getCurrentRoundState() {
		return currentRoundState;
	}

	public void setCurrentRoundState(RoundState currentRoundState) {
		this.currentRoundState = currentRoundState;
	}

	public RoundParameters getRoundParameters() {
		return roundParameters;
	}

	public void setRoundParameters(RoundParameters roundParameters) {
		this.roundParameters = roundParameters;
	}

	public HeartsGameInstanceState getMyGameState() {
		return myGameState;
	}

	public void setMyGameState(HeartsGameInstanceState myGameState) {
		this.myGameState = myGameState;
	}

	public List<String> getMyGamePlayers() {
		return myGamePlayers;
	}

	public void setMyGamePlayers(List<String> myGamePlayers) {
		this.myGamePlayers = myGamePlayers;
	}

	public String getMyLeftPlayer() {
		return myLeftPlayer;
	}

	public void setMyLeftPlayer(String myLeftPlayer) {
		this.myLeftPlayer = myLeftPlayer;
	}

	public List<Card> getMyInitialHand() {
		return myInitialHand;
	}

	public void setMyInitialHand(List<Card> myInitialHand) {
		this.myInitialHand = myInitialHand;
	}

	public List<Card> getMyFinalHand() {
		return myFinalHand;
	}

	public void setMyFinalHand(List<Card> myFinalHand) {
		this.myFinalHand = myFinalHand;
	}

	public List<Card> getMyCurrentHand() {
		return myCurrentHand;
	}

	public void setMyCurrentHand(List<Card> myCurrentHand) {
		this.myCurrentHand = myCurrentHand;
	}

	public List<Deal> getMyGameDeals() {
		return myGameDeals;
	}

	public void setMyGameDeals(List<Deal> myGameDeals) {
		this.myGameDeals = myGameDeals;
	}

	public Deal getMyInProgressDeal() {
		return myInProgressDeal;
	}

	public void setMyInProgressDeal(Deal myInProgressDeal) {
		this.myInProgressDeal = myInProgressDeal;
	}

	public boolean isMyTurn() {
		return isMyTurn;
	}

	public void setMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
	}
	@SerializedName("CurrentGameId")
	private String currentGameId;
	@SerializedName("CurrentGameState")
	private GameInstanceState currentGameState;
	@SerializedName("CurrentRoundId")
	private int currentRoundId;
	@SerializedName("CurrentRoundState")
	private RoundState currentRoundState;
	@SerializedName("RoundParameters")
	private RoundParameters roundParameters;
	@SerializedName("MyGameState")
	private HeartsGameInstanceState myGameState;
	@SerializedName("MyGamePlayers")
	private List<String> myGamePlayers;
	@SerializedName("MyLeftPlayer")
	private String myLeftPlayer;	
	@SerializedName("MyInitialHand")
	private List<Card> myInitialHand;
	@SerializedName("MyFinalHand")
    private List<Card> myFinalHand;
	@SerializedName("MyCurrentHand")
    private List<Card> myCurrentHand;
	@SerializedName("MyGameDeals")
    private List<Deal> myGameDeals;
	@SerializedName("MyInProgressDeal")
    private Deal myInProgressDeal;
	@SerializedName("IsMyTurn")
    private boolean isMyTurn;
	
    public GameStatus()
    {
        myInitialHand = new ArrayList<Card>();
        myFinalHand = new ArrayList<Card>();
        myCurrentHand = new ArrayList<Card>();
        myGameDeals = new ArrayList<Deal>();
    }

   
}



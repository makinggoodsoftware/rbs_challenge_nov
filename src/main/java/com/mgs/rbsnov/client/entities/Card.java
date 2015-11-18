package com.mgs.rbsnov.client.entities;

import com.google.gson.annotations.SerializedName;
import com.mgs.rbsnov.client.enums.SuitType;

public class Card
{
	public Card(){}
    public Card(SuitType suit, int number)
    {
        this.suit = suit;
        this.number = number;
    } 
    @SerializedName("Suit")
    private SuitType suit;
    @SerializedName("Number")
    private int number;
    public SuitType getSuit() {
		return suit;
	}
	public void setSuit(SuitType suit) {
		this.suit = suit;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
}
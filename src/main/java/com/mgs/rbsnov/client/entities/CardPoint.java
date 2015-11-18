package com.mgs.rbsnov.client.entities;

import com.google.gson.annotations.SerializedName;

public class CardPoint
{    
	public CardPoint(){}
    public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	@SerializedName("Card")
	private Card card;
	@SerializedName("Point")
	private int point;
}
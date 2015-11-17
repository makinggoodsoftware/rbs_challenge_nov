package entities;

import com.google.gson.annotations.SerializedName;

public class DealCard {
	@SerializedName("TeamName")
	private String teamName;
	@SerializedName("Card")
	private Card card;
    public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	
}

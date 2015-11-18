package com.mgs.rbsnov.client.app;

import com.mgs.rbsnov.adapter.AdapterCardStrategy;
import com.mgs.rbsnov.client.entities.*;
import com.mgs.rbsnov.client.enums.GameInstanceState;
import com.mgs.rbsnov.client.impl.JsonHelper;
import com.mgs.rbsnov.client.interfaces.ICardStrategy;
import com.mgs.rbsnov.client.util.Settings;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

	private final static Logger logger = Logger.getLogger(App.class.getSimpleName());
	private JsonHelper helper;
	private String baseUrl;
	private final ICardStrategy cardStrategy;
	private Map<String, Boolean> playerActivityTracker = new HashMap<String, Boolean>();
	private String teamName;
	
	public Player(String username, String password, ICardStrategy cardStrategy) throws Exception {
		this.cardStrategy = cardStrategy;
		if (username == null || username.isEmpty())
			this.teamName = Settings.getString("username");
		else 
			this.teamName = username;
		
		String pass = password;
		if (password == null || password.isEmpty())
			pass = Settings.getString("password");		 
			
		String hostname = Settings.getString("hostname");
		Thread.currentThread().setName(teamName);
		
		if (this.teamName == null || this.teamName.isEmpty())
			throw new Exception("teamName is empty or null");
		if (pass == null || pass.isEmpty())
			throw new Exception("password is empty or null ");
		
		baseUrl = "http://" + hostname + "/api/participant";
		helper = new JsonHelper(baseUrl, teamName, pass);
	}

	public void Play() throws InterruptedException {

		playerActivityTracker.clear();
		boolean exitLoop = false;
		while (true) {
			try {
				GameStatus currentGameStatus = GetGameStatus();
				if (null == currentGameStatus)
					continue;
				String keyGameStatus = "Game State - " + currentGameStatus.getCurrentGameState();
				if (!playerActivityTracker.containsKey(keyGameStatus)) {
					playerActivityTracker.put(keyGameStatus, Boolean.TRUE);
				}
				GameInstanceState state = currentGameStatus.getCurrentGameState();
				switch (state) {
				case Open:
					String keyJoinStatus = "JoinGame";
					if (!playerActivityTracker.containsKey(keyJoinStatus)) {
						Boolean joinSuccessful = JoinGame();
						if (joinSuccessful) {
							playerActivityTracker.put(keyJoinStatus, true);
							logger.info("Join Successful");
						}
					}
					break;
				case Finished:
				case Cancelled:
					exitLoop = true;
					break;

				case Running: {
					if (currentGameStatus.getCurrentRoundId() > 0) {
						String keyRoundStatus = "Round " + currentGameStatus.getCurrentRoundId() + " - "
								+ currentGameStatus.getCurrentRoundState();
						if (!playerActivityTracker.containsKey(keyRoundStatus)) {
							playerActivityTracker.put(keyRoundStatus, true);
						}

						switch (currentGameStatus.getCurrentRoundState()) {
						case Running: {
							String keyMyHeartsGameStatus = "My game - Round " + currentGameStatus.getCurrentRoundId()
									+ " " + currentGameStatus.getMyGameState();
							if (!playerActivityTracker.containsKey(keyMyHeartsGameStatus)) {
								playerActivityTracker.put(keyMyHeartsGameStatus, true);
							}

							switch (currentGameStatus.getMyGameState()) {
							case Passing:
								DisplayMyCurrentHand(currentGameStatus);
								String keyPassing = "Passing - Round " + currentGameStatus.getCurrentRoundId();
								if (!playerActivityTracker.containsKey(keyPassing)) {
									DoPassingActivity(currentGameStatus);
									playerActivityTracker.put(keyPassing, true);
								}
								break;
							case Dealing:
								DisplayMyCurrentHand(currentGameStatus);
								if (currentGameStatus.isMyTurn()) {
									String keyDealing = "Dealing - Round " + currentGameStatus.getCurrentRoundId()
											+ " Deal " + currentGameStatus.getMyInProgressDeal().getDealNumber();
									if (!playerActivityTracker.containsKey(keyDealing)) {
										DoDealingActivity(currentGameStatus);
										playerActivityTracker.put(keyDealing, true);
									}
								}
								break;
							case Finished:
								break;
							default:
								break;
							}
						}
							break;
						default:
							break;
						}
					}
				}
					break;
				default:
					break;
				}
			}

			catch (Exception ex) {
				logger.error(JsonHelper.getStackTrace(ex));
				exitLoop = true;
			}
			Thread.sleep(300);
			if (exitLoop)
				break;
			Play();
		}

	}

	private void DoDealingActivity(GameStatus currentGameStatus) throws IllegalStateException, IOException {
		Card cardToDeal = cardStrategy.PlayCard(currentGameStatus, teamName);
		if (cardToDeal == null) return;
		
		CodeCompResponse response = helper.processPostRequest("/playcard", cardToDeal,
				CodeCompResponse.class);
        

		if (response.getHasError() == false)
			logger.info("Card " + cardToDeal.getSuit() + " " + cardToDeal.getNumber()
					+ " dealt Successfully");
		else
			logger.info("Error while dealing a new card to the server "
					+ response.getFault().getFaultMessage());

		logger.info("");

    
		
	}

	private void DoPassingActivity(GameStatus currentGameStatus) throws IllegalStateException, IOException {
		 int noOfCardsToBePassed = currentGameStatus.getRoundParameters().getNumberOfCardsTobePassed();
         List<Card> cardsToPass = cardStrategy.PassCards(currentGameStatus);

         if (null != cardsToPass && cardsToPass.size() == noOfCardsToBePassed)
         {
        	 CodeCompResponse gameResponse = helper.processPostRequest("/passcards", cardsToPass,
						CodeCompResponse.class);             

             if (gameResponse.getHasError() == true)
             {
                 logger.info(gameResponse.getFault().getFaultMessage());
             }
             else
             {
                 logger.info(noOfCardsToBePassed + " cards passed successfully. Cards are :");
                 for (Card passedCard : cardsToPass)
                	 logger.info(passedCard.getSuit().toString() + passedCard.getNumber());
             }
         }

	}

	private void DisplayMyCurrentHand(GameStatus currentGameStatus) {
		 if (currentGameStatus.getMyCurrentHand().size()> 0)
         {
             //Don't print current hand if it didn't change
             String currentHandBuilder = "" ;
             for (Card c : currentGameStatus.getMyCurrentHand())
             {
                 currentHandBuilder +=  c.getSuit() + "-" + c.getNumber() + "|";
             }

             String keyDisplayCurrentHand = currentGameStatus.getCurrentRoundId() + "-" + currentHandBuilder;

             if (!playerActivityTracker.containsKey(keyDisplayCurrentHand))
             {
                 playerActivityTracker.put(keyDisplayCurrentHand, true);
             }
         }

     }


	

	private GameStatus GetGameStatus() throws IllegalStateException, IOException {
		CodeCompResponse response = helper.processGetRequest("/gamestatus", CodeCompResponse.class);
		if (response == null || response.getHasError() == true || response.getData() == null) {
			if (response == null)
				logger.error("Server is not started");
			
			else if (response.getFault() != null)
				logger.error("Error Server Response is : " + response.getFault().getFaultMessage());
			return null;
		}
		GameStatus game = helper.getClassfromJson(response.getData(), GameStatus.class);
		return game;
	}

	private boolean JoinGame() throws InterruptedException, IllegalStateException, IOException {
		CodeCompResponse response = helper.processPostRequest("/join", null, CodeCompResponse.class);
		if (response == null)
			return false;

		if (response.getHasError() == true || response.getData() == null)

		{
			logger.info("Not able to join game. Server Response is : " + response.getFault().getFaultMessage());
			return false;

		}
		GameStatus game = helper.getClassfromJson(response.getData(), GameStatus.class);
		logger.info("Joined the game " + game.getCurrentGameId());
		return true;

	}

	private void CheckServerConnectivity() throws InterruptedException {
		while (helper.ping(5000)) {
			Thread.sleep(5000);
		}

	}
}

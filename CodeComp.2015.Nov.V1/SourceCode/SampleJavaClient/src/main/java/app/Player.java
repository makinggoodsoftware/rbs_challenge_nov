package app;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import entities.Card;
import entities.CardStrategy;
import entities.CodeCompResponse;
import entities.DealCard;
import entities.GameStatus;
import enums.GameInstanceState;
import impl.JsonHelper;
import interfaces.ICardStrategy;
import util.Settings;

public class Player {

	private final static Logger logger = Logger.getLogger(App.class.getSimpleName());
	private JsonHelper helper;
	private String baseUrl;
	private ICardStrategy cardStrategy = new CardStrategy();
	private Map<String, Boolean> playerActivityTracker = new HashMap<String, Boolean>();
	private String teamName;
	
	public Player(String username, String password) throws Exception {
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
		CheckServerConnectivity();
		boolean exitLoop = false;
		while (true) {
			try {
				GameStatus currentGameStatus = GetGameStatus();
				if (null == currentGameStatus)
					continue;
				String keyGameStatus = "Game State - " + currentGameStatus.getCurrentGameState();
				if (!playerActivityTracker.containsKey(keyGameStatus)) {
					logger.info(keyGameStatus);
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
							logger.info(keyRoundStatus);
							playerActivityTracker.put(keyRoundStatus, true);
						}

						switch (currentGameStatus.getCurrentRoundState()) {
						case Running: {
							String keyMyHeartsGameStatus = "My game - Round " + currentGameStatus.getCurrentRoundId()
									+ " " + currentGameStatus.getMyGameState();
							if (!playerActivityTracker.containsKey(keyMyHeartsGameStatus)) {
								logger.info(keyMyHeartsGameStatus);
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
			Thread.sleep(1000);
			if (exitLoop)
				break;
			Play();
		}

	}

	private void DoDealingActivity(GameStatus currentGameStatus) throws IllegalStateException, IOException {
		Card cardToDeal = cardStrategy.PlayCard(currentGameStatus, teamName);
		
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
         logger.info("");
         logger.info(noOfCardsToBePassed + " cards need to be passed to right.");
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

         logger.info("");

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
                 logger.info("My Current Hand : ");

                 for (Card card : currentGameStatus.getMyCurrentHand())
                 {
                     logger.info(card.getSuit() + " " + card.getNumber());
                     logger.info(",");
                 }

                 logger.info("");
                 playerActivityTracker.put(keyDisplayCurrentHand, true);
             }
         }

         if (currentGameStatus.getMyInProgressDeal().getDealCards() != null &&
                 currentGameStatus.getMyInProgressDeal().getDealCards().size()> 0)
         {
             //display current deal
             logger.info("Cards played so far in current deal :");

             for (DealCard dealInfo : currentGameStatus.getMyInProgressDeal().getDealCards())
                 logger.info(dealInfo.getTeamName() + ":" + dealInfo.getCard().getSuit()+ " " + dealInfo.getCard().getNumber());
         }

         if (currentGameStatus.getMyInProgressDeal().getDealWinner() == null || 
        		 currentGameStatus.getMyInProgressDeal().getDealWinner().isEmpty())
             logger.info("Current Deal Winner : " + currentGameStatus.getMyInProgressDeal().getDealWinner());
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
			logger.info(String.format("Trying to connect server " + baseUrl));
		}
		logger.info(String.format("Connected to server  " + baseUrl));

	}
}

using RestSharp;
using RestSharp.Authenticators;
using System;
using System.Configuration;
using System.Net;
using System.Threading;
using System.Collections.Generic;
using RBS.CodeComp.Client.Entity;
using RBS.CodeComp.Client.IO;
using RBS.CodeComp.Client.Strategy;
using System.Text;

namespace RBS.CodeComp.Client
{
    internal class Player : IPlayer
    {
        #region Private members
        private readonly IRestClient _proxy;
        private readonly IWriter _writer;
        private readonly ICardStrategy _cardStrategy;
        private readonly string _myTeamName;
        #endregion

        #region Constructor

        public Player(
            IRestClient proxy,
            IAuthenticator authenticator,
            ICardStrategy cardStrategy,
            IWriter writer,
            string myTeamName)
        {
            if (proxy == null) throw new ArgumentNullException("proxy");
            if (authenticator == null) throw new ArgumentNullException("authenticator");
            if (cardStrategy == null) throw new ArgumentNullException("cardStrategy");
            if (writer == null) throw new ArgumentNullException("writer");
            if (string.IsNullOrWhiteSpace(myTeamName)) throw new ArgumentNullException("myTeamName");

            _proxy = proxy;
            _proxy.Authenticator = authenticator;
            _writer = writer;
            _cardStrategy = cardStrategy;
            _myTeamName = myTeamName;
        }
        #endregion

        private Dictionary<string, bool> _playerActivityTracker = new Dictionary<string, bool>();
        // ReSharper disable once FunctionRecursiveOnAllPaths
        public void Play()
        {
            _playerActivityTracker.Clear();

            CheckServerConnectivity();

            var exitLoop = false;
            while (true)
            {
                try
                {
                    var currentGameStatus = GetGameStatus();

                    if (null == currentGameStatus)
                        continue;

                    var keyGameStatus = string.Format("Game State - {0}", currentGameStatus.CurrentGameState);
                    if (!_playerActivityTracker.ContainsKey(keyGameStatus))
                    {
                        _writer.Info(keyGameStatus, true);
                        _playerActivityTracker.Add(keyGameStatus, true);
                    }

                    switch (currentGameStatus.CurrentGameState)
                    {
                        case GameInstanceState.Open:
                            //Join game
                            var keyJoinStatus = "JoinGame";
                            if (!_playerActivityTracker.ContainsKey(keyJoinStatus))
                            {
                                var joinSuccessful = JoinGame();
                                if (joinSuccessful)
                                {
                                    _playerActivityTracker.Add(keyJoinStatus, true);
                                    _writer.Info("Join Successful", true);
                                }
                            }
                            break;
                        case GameInstanceState.Finished:
                        case GameInstanceState.Cancelled:
                            exitLoop = true;
                            break;
                        case GameInstanceState.Running:
                        {
                            if (currentGameStatus.CurrentRoundId > 0)
                            {
                                var keyRoundStatus = string.Format("Round {0} - {1}", currentGameStatus.CurrentRoundId,
                                    currentGameStatus.CurrentRoundState);
                                if (!_playerActivityTracker.ContainsKey(keyRoundStatus))
                                {
                                    _writer.Info(keyRoundStatus, true, ConsoleColor.DarkGreen);
                                    _playerActivityTracker.Add(keyRoundStatus, true);
                                }

                                switch (currentGameStatus.CurrentRoundState)
                                {
                                    case RoundState.Running:
                                    {
                                        var keyMyHeartsGameStatus = string.Format("My game - Round {0} {1}",
                                            currentGameStatus.CurrentRoundId, currentGameStatus.MyGameState);
                                        if (!_playerActivityTracker.ContainsKey(keyMyHeartsGameStatus))
                                        {
                                            _writer.Info(keyMyHeartsGameStatus, true);
                                            _playerActivityTracker.Add(keyMyHeartsGameStatus, true);
                                        }

                                        switch (currentGameStatus.MyGameState)
                                        {
                                            case HeartsGameInstanceState.Passing:
                                                DisplayMyCurrentHand(currentGameStatus);
                                                var keyPassing = string.Format("Passing - Round {0}",
                                                    currentGameStatus.CurrentRoundId);
                                                if (!_playerActivityTracker.ContainsKey(keyPassing))
                                                {
                                                    DoPassingActivity(currentGameStatus);
                                                    _playerActivityTracker.Add(keyPassing, true);
                                                }
                                                break;
                                            case HeartsGameInstanceState.Dealing:
                                                DisplayMyCurrentHand(currentGameStatus);
                                                if (currentGameStatus.IsMyTurn)
                                                {
                                                    var keyDealing = string.Format("Dealing - Round {0} Deal {1}",
                                                        currentGameStatus.CurrentRoundId,
                                                        currentGameStatus.MyInProgressDeal.DealNumber);
                                                    if (!_playerActivityTracker.ContainsKey(keyDealing))
                                                    {
                                                        DoDealingActivity(currentGameStatus);
                                                        _playerActivityTracker.Add(keyDealing, true);
                                                    }
                                                }
                                                break;
                                            case HeartsGameInstanceState.Finished:
                                                break;
                                        }
                                    }
                                        break;
                                }
                            }
                        }
                            break;
                    }
                }
                catch
                {
                    exitLoop = true;
                }

                Thread.Sleep(1000);

                if (exitLoop) break;
            }

            Play(); 
        }

        #region Helper

        private void CheckServerConnectivity()
        {
            var serverAddress = ConfigurationManager.AppSettings["ServerBaseAddress"];

            var request = new RestRequest("admin/ping") { Timeout = 5000 };
            var response = _proxy.Execute(request);
            while (response.StatusCode != HttpStatusCode.OK)
            {
                Thread.Sleep(5000);
                response = _proxy.Execute(request);
                _writer.Info(string.Format("Trying to connect server {0}", serverAddress), true);
            }

            _writer.Info(string.Format("Connected to server {0}", serverAddress), true);
        }

        private GameStatus GetGameStatus()
        {
            GameStatus currentGameStatus = null;
            var request = new RestRequest("api/participant/gamestatus") { Timeout = 5000 };

            var response = _proxy.Get(request);

            if (!string.IsNullOrEmpty(response.Content))
            {
                var gameResponse = (CodeCompResponse) ServiceStack.Text.Json.JsonReader<CodeCompResponse>.Parse(response.Content);

                if (!gameResponse.HasError)
                {
                    currentGameStatus =
                        (GameStatus) ServiceStack.Text.Json.JsonReader<GameStatus>.Parse(gameResponse.Data);
                }
            }

            return currentGameStatus;
        }

        private bool JoinGame()
        {
            var request = new RestRequest("api/participant/join") { Timeout = 5000 };
            var response = _proxy.Post(request);

            if (string.IsNullOrEmpty(response.Content)) return false;

            var gameResponse = (CodeCompResponse)ServiceStack.Text.Json.JsonReader<CodeCompResponse>.Parse(response.Content);

            if (gameResponse.HasError)
            {
                _writer.Info(gameResponse.Fault.FaultMessage, true);
                return false;
            }

            var gameStatus = (GameStatus)ServiceStack.Text.Json.JsonReader<GameStatus>.Parse(gameResponse.Data);
            _writer.Info(string.Format("Joined the game {0}", gameStatus.CurrentGameId), true, ConsoleColor.DarkGreen);

            return true;
        }

        private void DoPassingActivity(GameStatus currentGameStatus)
        {
            var noOfCardsToBePassed = currentGameStatus.RoundParameters.NumberOfCardsTobePassed;

            _writer.Info(string.Empty, true);

            _writer.Info(string.Format("{0} cards need to be passed to right.", noOfCardsToBePassed), true);

            var cardsToPass = _cardStrategy.PassCards(currentGameStatus);

            if (null != cardsToPass && cardsToPass.Count == noOfCardsToBePassed)
            {
                var request = new RestRequest("api/participant/passcards") { Timeout = 5000 };

                request.AddJsonBody(cardsToPass);

                var response = _proxy.Post(request);

                if (string.IsNullOrEmpty(response.Content)) return;

                var gameResponse = (CodeCompResponse)ServiceStack.Text.Json.JsonReader<CodeCompResponse>.Parse(response.Content);

                if (gameResponse.HasError)
                {
                    _writer.Info(gameResponse.Fault.FaultMessage, true);
                }
                else
                {
                    _writer.Info(string.Format("{0} cards passed successfully. Cards are :", noOfCardsToBePassed), true, ConsoleColor.DarkGreen);
                    foreach (Card passedCard in cardsToPass)
                        _writer.Info(string.Format("{0} {1} ", passedCard.Suit.GetSymbol(), passedCard.Number), true, passedCard.Suit.GetColor());
                }
            }

            _writer.Info(string.Empty, true);
        }

        private void DoDealingActivity(GameStatus currentGameStatus)
        {
            var cardToDeal = _cardStrategy.PlayCard(currentGameStatus, _myTeamName);

            var request = new RestRequest("api/participant/playcard") { Timeout = 5000 };

            request.AddJsonBody(cardToDeal);

            var response = _proxy.Post(request);

            if (string.IsNullOrEmpty(response.Content)) return;

            var gameResponse = (CodeCompResponse)ServiceStack.Text.Json.JsonReader<CodeCompResponse>.Parse(response.Content);

            _writer.Info(
                gameResponse.HasError
                    ? gameResponse.Fault.FaultMessage
                    : string.Format("Card {0} {1} dealt successfully", cardToDeal.Suit.GetSymbol(),
                        cardToDeal.Number), true, ConsoleColor.DarkGreen);

            _writer.Info("", true);
        }

        private void DisplayMyCurrentHand(GameStatus currentGameStatus)
        {
            if (currentGameStatus.MyCurrentHand.Count > 0)
            {
                //Don't print current hand if it didn't change
                var currentHandBuilder = new StringBuilder();
                foreach (var c in currentGameStatus.MyCurrentHand)
                {
                    currentHandBuilder.AppendFormat("{0}-{1}|", c.Suit, c.Number);
                }

                var keyDisplayCurrentHand = string.Format("{0}-{1}", currentGameStatus.CurrentRoundId, currentHandBuilder.ToString());

                if (!_playerActivityTracker.ContainsKey(keyDisplayCurrentHand))
                {
                    _writer.Info("My Current Hand : ", true);

                    foreach (var card in currentGameStatus.MyCurrentHand)
                    {
                        _writer.Info(card.Suit.GetSymbol() + " " + card.Number, false, card.Suit.GetColor());
                        _writer.Info(",");
                    }

                    _writer.Info(string.Empty, true);
                    _playerActivityTracker.Add(keyDisplayCurrentHand, true);
                }
            }

            if (currentGameStatus.MyInProgressDeal.DealCards != null &&
                    currentGameStatus.MyInProgressDeal.DealCards.Count > 0)
            {
                //display current deal
                _writer.Info("Cards played so far in current deal :", true);

                foreach (var dealInfo in currentGameStatus.MyInProgressDeal.DealCards)
                    _writer.Info(dealInfo.TeamName + ":" + dealInfo.Card.Suit.GetSymbol() + " " + dealInfo.Card.Number.ToString(), true, dealInfo.Card.Suit.GetColor());
            }

            if (!string.IsNullOrWhiteSpace(currentGameStatus.MyInProgressDeal.DealWinner))
                _writer.Info(string.Format("Current Deal Winner : {0}", currentGameStatus.MyInProgressDeal.DealWinner), true);
        }

        #endregion
    }
}

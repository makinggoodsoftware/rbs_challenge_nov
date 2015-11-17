using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Reflection;
using System.Xml.Serialization;

namespace RBS.CodeComp.Client.Entity
{
    internal class CodeCompFault
    {
        public string FaultMessage { get; set; }
        public int FaultCode { get; set; }
    }

    internal class CodeCompResponse
    {
        public CodeCompFault Fault { get; set; }
        public bool HasError { get; set; }
        public string Data { get; set; }
    }

    internal class GameStatus
    {
        public Guid CurrentGameId { get; set; }
        public GameInstanceState CurrentGameState { get; set; }

        public int CurrentRoundId { get; set; }
        public RoundState CurrentRoundState { get; set; }
        public RoundParameters RoundParameters { get; set; }

        public HeartsGameInstanceState MyGameState { get; set; }
        public string MyGameStateDescription { get; set; }
        public List<HeartsGameParticipant> MyGameParticipants { get; set; }
        public List<Card> MyInitialHand { get; set; } // player hand received after card distribution
        public List<Card> CardsPassedByMe { get; set; }
        public List<Card> CardsPassedToMe { get; set; }
        public List<Card> MyFinalHand { get; set; } // player hand after passing cards
        public List<Card> MyCurrentHand { get; set; } // what the player has in hand currently
        public List<Deal> MyGameDeals { get; set; } // all deals that have been played so far in the game
        public Deal MyInProgressDeal { get; set; } // so a player can see what cards others have played in the on-going deal
        public bool IsMyTurn { get; set; } // is it your turn to play a card in the ongoing deal
    }

    internal class HeartsGameParticipant
    {
        public string TeamName { get; set; }
        // player on given player's left side
        public string LeftParticipant { get; set; }
        // number of cards given player has in his hand
        public int? NumberOfCardsInHand { get; set; }
        public bool HasTurn { get; set; }
        public int CurrentScore { get; set; }
    }

    internal class Deal
    {
        public int DealNumber { get; set; }
        // player who started the deal
        public string Initiator { get; set; }
        // suit of card played by initiator
        public SuitType SuitType { get; set; }
        // cards played in the deal & by whom
        public List<DealCard> DealCards { get; set; }
        // who won this deal
        public string DealWinner { get; set; }
    }

    internal class DealCard
    {
        public string TeamName { get; set; }
        public Card Card { get; set; }
    }

    internal class RoundParameters
    {
        public int RoundId { get; set; }
        public int InitiationPhaseInSeconds { get; set; }
        public int PassingPhaseInSeconds { get; set; }
        public int DealingPhaseInSeconds { get; set; }
        public int FinishingPhaseInSeconds { get; set; }
        public int NumberOfCardsTobePassed { get; set; }
        public List<CardPoint> CardPoints { get; set; }
    }

    internal class CardPoint
    {
        public Card Card { get; set; }
        public int Point { get; set; }
    }

    internal class Card
    {
        public Card(SuitType suit, int number)
        {
            Suit = suit;
            Number = number;
        }

        public SuitType Suit { get; set; }
        public int Number { get; set; }
    }

    internal enum GameInstanceState
    {
        NotStarted = 0,
        Initiated = 1,
        Open = 2,
        Running = 3,
        Finished = 4,
        Cancelled = 5
    }

    internal enum RoundState
    {
        NotStarted = 0,
        Initiated = 1,
        Running = 2,
        Finished = 3,
        Cancelled = 4
    }

    internal enum HeartsGameInstanceState
    {
        NotStarted = 0,
        Initiated = 1,
        Passing = 2,
        Dealing = 3,
        Finished = 4,
        Cancelled = 5
    }

    internal enum SuitType
    {
        Club = 0,
        Diamond = 1,
        Heart = 2,
        Spade = 3
    }

    internal static class EnumUtil
    {
        public static char GetSymbol(this SuitType element)
        {
            switch (element)
            {
                case SuitType.Club:
                    return (char)5;
                case SuitType.Spade:
                    return (char)6;
                case SuitType.Diamond:
                    return (char)4;
                case SuitType.Heart:
                    return (char)3;
            }

            return 'x';
        }

        public static ConsoleColor GetColor(this SuitType element)
        {
            switch (element)
            {
                case SuitType.Club:
                case SuitType.Spade:
                    return ConsoleColor.DarkGray;
                case SuitType.Diamond:
                case SuitType.Heart:
                    return ConsoleColor.Red;
            }

            return ConsoleColor.Black;

        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using RBS.CodeComp.Client.Entity;

namespace RBS.CodeComp.Client.Strategy
{
    internal class CardStrategy : ICardStrategy
    {
        public List<Card> PassCards(GameStatus gameStatus)
        {
            //Obviously you don't want to do below, Right?
            var random = new Random(DateTime.Now.Millisecond);

            return gameStatus.MyInitialHand
                .OrderBy(item => random.Next())
                .Take(gameStatus.RoundParameters.NumberOfCardsTobePassed).ToList();
        }

        public Card PlayCard(GameStatus gameStatus, string myTeamName)
        {
            //Trust me, below code is bad, really bad. Wanna improve this?
            // Hints
            // 1. https://en.wikibooks.org/wiki/Card_Games/Hearts/Strategy
            // 2. http://boardgames.about.com/od/hearts/a/Basic-Hearts-Strategy.htm
            // 3. http://www.dummies.com/how-to/content/playing-hearts-game-strategies.html

            //Play random card of same suit
            var random = new Random(DateTime.Now.Millisecond);

            if (string.Compare(myTeamName, gameStatus.MyInProgressDeal.Initiator,
                StringComparison.InvariantCultureIgnoreCase) != 0) // I am not initiator
            {
                var randomCardOfSameSuit =
                    gameStatus.MyCurrentHand.Where(item => item.Suit == gameStatus.MyInProgressDeal.SuitType)
                        .OrderBy(s => random.Next())
                        .FirstOrDefault();

                if (randomCardOfSameSuit != null) return randomCardOfSameSuit;
            }

            //If this suit is not present, play random card of any other suit
            return gameStatus.MyCurrentHand.OrderBy(s => random.Next()).FirstOrDefault();
        }

    }
}

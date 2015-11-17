using System.Collections.Generic;
using RBS.CodeComp.Client.Entity;

namespace RBS.CodeComp.Client.Strategy
{
    internal interface ICardStrategy
    {
        List<Card> PassCards(GameStatus gameStatus);

        Card PlayCard(GameStatus gameStatus, string myTeamName);
    }
}

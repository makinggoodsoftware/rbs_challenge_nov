var gameBoardData = null;
var team = "";
var pwd = "";
var processing = false;
var leftTeam = null;
var rightTeam = null;
var frontTeam = null;
var timerId = 0;

function filterResponse(data) {
    var filteredResponse = data.data.replace("'\'", "''");
    return jQuery.parseJSON(filteredResponse);
    //return JSON.parse(filteredResponse);
};

function getParticipant(teamName) {
    var participants = gameBoardData.MyGameParticipants;
    if (participants == undefined) return null;

    for (var i = 0; i < participants.length; i++) {
        if (participants[i].TeamName === teamName) {
            return participants[i];
        }
    }
    return null;
};

function showCurrentHand() {
    var hand = null;
    if (gameBoardData.MyGameState == 'Initiated') hand = gameBoardData.MyInitialHand;
    if (gameBoardData.MyGameState == 'Passing') hand = gameBoardData.MyFinalHand;
    if (gameBoardData.MyGameState == 'Dealing') hand = gameBoardData.MyCurrentHand;

    createLoggedInParticipantsCards("15%", "10%", "10%", 35, 3, "absolute", hand);

    var me = getParticipant(team);
    createParticipant(teamName, me.CurrentScore, "me", "90%", "40%");

    //left
    var leftParticipant = getParticipant(me.LeftParticipant);
    if (leftParticipant == null) return;

    createFaceDownCards("flippedHorizontal", "15%", "10%", 30, 2, 15, 0, 0, 0, "absolute", leftParticipant.NumberOfCardsInHand);
    leftTeam = leftParticipant.TeamName;
    createParticipant(leftTeam, leftParticipant.CurrentScore, "left", "40%", "10%");

    //front
    var frontParticipant = getParticipant(leftParticipant.LeftParticipant);
    if (frontParticipant == null) return;
    createFaceDownCards("flipped", "15%", "10%", 5, 0, 35, 2, 0, 0, "absolute", frontParticipant.NumberOfCardsInHand);
    frontTeam = frontParticipant.TeamName;
    createParticipant(frontTeam, frontParticipant.CurrentScore, "front", "1%", "40%");


    //right
    var rightParticipant = getParticipant(frontParticipant.LeftParticipant);
    if (rightParticipant == null) return;
    createFaceDownCards("flippedHorizontal", "15%", "10%", 30, 2, 0, 0, 15, 0, "absolute", rightParticipant.NumberOfCardsInHand);
    rightTeam = rightParticipant.TeamName;
    createParticipant(rightTeam, rightParticipant.CurrentScore, "right", "40%", "90%");


};

function showLoading() {
    $("#titleHeartsGameState").text("Initializing game board. Please wait...");
}

function showGameState() {
    $("#titleGameState").text("(" + gameBoardData.CurrentGameState + ")");
}

function showRoundState() {
    $("#titleRoundState").text("(" + gameBoardData.CurrentRoundState + ")");
}

function showHeartGameState() {
    $("#titleHeartsGameState").text(gameBoardData.MyGameStateDescription);
}

function createParticipant(teamName, score, positionOnBoard, top, left) {
    var div = document.createElement("div");
    $("#game-region").append(div);

    div.id = positionOnBoard;
    div.style.left = left;
    div.style.top = top;
    div.style.zIndex = 10;
    div.style.fontWeight = "900";
    div.style.position = "absolute";

    if (positionOnBoard == 'left' || positionOnBoard == 'right') {
        div.className += "rotated-text";
        div.innerHTML = "<span style='background-color:white;' class=\"rotated-text__inner\">" + teamName + " : " + score + "</span>";
    } else {
        div.innerHTML = "<span style='background-color:white;'>" + teamName + " : " + score + "</span>";
    }
}

function myInProgressDealForParticipant(teamName, dealCards) {
    for (var i = 0; i < dealCards.length; i++) {
        if (dealCards[i].TeamName == teamName) {
            return dealCards[i].Card;
        }
    }
    return null;
};

function showCurrentDealtCard() {
    if (gameBoardData.MyInProgressDeal == undefined) return;
    var dealCards = gameBoardData.MyInProgressDeal.DealCards;
    var myInProgressCard = myInProgressDealForParticipant(team, dealCards);
    if (myInProgressCard != null) {
        currentDealtCardPosition("15%", "10%", "55%", "45%", "absolute", myInProgressCard);
    }

    var leftInProgressCard = myInProgressDealForParticipant(leftTeam, dealCards);
    if (leftInProgressCard != null) {
        currentDealtCardPosition("15%", "10%", "40%", "35%", "absolute", leftInProgressCard);
    }

    var frontInProgressCard = myInProgressDealForParticipant(frontTeam, dealCards);
    if (frontInProgressCard != null) {
        currentDealtCardPosition("15%", "10%", "25%", "45%", "absolute", frontInProgressCard);
    }

    var rightInProgressCard = myInProgressDealForParticipant(rightTeam, dealCards);
    if (rightInProgressCard != null) {
        currentDealtCardPosition("15%", "10%", "40%", "55%", "absolute", rightInProgressCard);
    }
};

function currentDealtCardPosition(height, width, top, left, pos, currentCard) {
    var image = document.createElement("img");
    $("#game-region").append(image);
    image.src = "/api/Web/Get?id=Pages/images/" + currentCard.Suit + currentCard.Symbol + ".gif";
    image.style.height = height;
    image.style.width = width;
    image.style.top = top;
    image.style.position = pos;
    image.style.left = left;
}

function initializeGameBoard(teamName, password) {
    team = teamName;
    pwd = password;

    showLoading();

    timerId = setInterval(function () {
        if (team != "" && pwd != "") {

            if (processing) return;

            processing = true;

            $.ajax({
                url: '/api/participant/gamestatus',
                cache: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", MakeBaseAuth(team, pwd));
                },
                success: function (data) {
                    try {
                        if (data != undefined) {
                            gameBoardData = filterResponse(data);
                            showGameState();
                            showRoundState();
                            showHeartGameState();
                            showCurrentHand();
                            if (gameBoardData != null && gameBoardData.MyGameState == 'Passing') {
                                showCardsPassedByMe("15%", "10%", "15%", 70, 4, "absolute");
                            }
                            if (gameBoardData != null && gameBoardData.MyGameState == 'Dealing') {
                                showCurrentDealtCard();
                            }
                        }
                    }
                    catch (e) {
                        console.log(e);
                    }
                    finally {
                        processing = false;
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {

                    //var responseText = JSON.parse(xhr.responseText);
                    var responseText = jQuery.parseJSON(xhr.responseText);
                    
                    if (responseText.fault.FaultCode == "15001") {
                        var errorMessage = responseText.fault.FaultMessage;
                        clearInterval(timerId);
                        team = pwd = "";
                        $.toaster({ priority: 'danger', title: 'Error', message: errorMessage });
                        $("#loginDiv").show();
                        $("#game-region").hide();
                    }

                    processing = false;
                }
            });
        }
    }, 5000);

};

function createFaceDownCards(img, height, width, top, topOffSet, left, leftOffSet, right, rightOffSet, pos, cardsInhand) {
    for (var i = 0; i < cardsInhand; i++) {
        var image = document.createElement("img");
        $("#game-region").append(image);
        image.src = "/api/Web/Get?id=Pages/images/" + img + ".gif";
        image.style.height = height;
        image.style.width = width;
        image.style.top = (top + i * topOffSet) + "%";
        image.style.position = pos;
        if (left > 0) {
            image.style.left = (left + i * leftOffSet) + "%";
        }
        if (right > 0) {
            image.style.right = (right + i * rightOffSet) + "%";
        }

    }
};

function createLoggedInParticipantsCards(height, width, bottom, left, leftOffSet, pos, hand) {
    $("#game-region").empty();
    if (hand != undefined) {
        for (var counter = 0; counter < hand.length; counter++) {
            var image = document.createElement("img");
            $("#game-region").append(image);
            image.src = "/api/Web/Get?id=Pages/images/" + hand[counter].Suit + hand[counter].Symbol + ".gif";
            image.style.height = height;
            image.style.width = width;
            image.style.bottom = bottom;
            image.style.position = pos;
            image.style.left = (left + counter * leftOffSet) + "%";
        }
    }
};

function showCardsPassedByMe(height, width, bottom, left, leftOffSet, pos) {
    var cards = gameBoardData.CardsPassedByMe;

    if (cards != null) {
        for (var counter = 0; counter < cards.length; counter++) {
            var image = document.createElement("img");
            $("#game-region").append(image);
            image.src = "/api/Web/Get?id=Pages/images/" + cards[counter].Suit + cards[counter].Symbol + ".gif";
            image.style.height = height;
            image.style.width = width;
            image.style.bottom = bottom;
            image.style.position = pos;
            image.style.left = (left + counter * leftOffSet) + "%";
        }
    }

};






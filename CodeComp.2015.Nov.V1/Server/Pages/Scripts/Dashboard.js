$(function () {
    $('[data-toggle="tooltip"]').tooltip();
});

$(document).ready(function () {

    var processingScoreApi = false;

    var roundScoreData;
    var gameScoreData;

    var tableRoundScore = $('#table_round_score').dataTable({
        "data": roundScoreData,
        "paging": false,
        "ordering": false,
        "info": false,
        "bFilter": false,
        "bDestroy": false,
        "bPaginate": false,
        "bScrollCollapse": true,
        "scrollY": "400px"

    });


    var tableGameScore = $('#table_game_score').dataTable({
        "data": roundScoreData,
        "paging": false,
        "ordering": false,
        "info": false,
        "bFilter": false,
        "bDestroy": false,
        "bPaginate": false,
        "bScrollCollapse": true,
        "scrollY": "400px"

    });


    setInterval(function () {

        if (processingScoreApi) return;

        processingScoreApi = true;

        $.ajax({
            url: "/api/gamedashboard/score",
            cache: false,
            success: function (result) {


                scoreData = jQuery.parseJSON(result.data);
                //scoreData = JSON.parse(result.data);

                if (scoreData.currentRoundScores) {
                    roundScoreData = scoreData.currentRoundScores;
                    tableRoundScore.fnClearTable();
                    if (roundScoreData.length > 0)
                        tableRoundScore.fnAddData(roundScoreData);
                    $('#roundNumber').text((scoreData.currentRoundId == "0") ? "" : "[" + scoreData.currentRoundId + "]");
                }

                if (scoreData.currentGameScores) {
                    gameScoreData = scoreData.currentGameScores;

                    tableGameScore.fnClearTable();
                    if (gameScoreData.length > 0)
                        tableGameScore.fnAddData(gameScoreData);
                }

                processingScoreApi = false;
            },
            error: function (xhr, ajaxOptions, thrownError) {
                processingScoreApi = false;
            }
        });
    }, 5000);
});

$("#btnStartNewGame").click(function () {
    $("#btnStartNewGame").attr("disabled", true);
    $.ajax({
        url: "/api/gamedashboard/startnewgame",
        cache: false,
        success: function () {
            $.toaster({ priority: 'success', title: 'Success', message: 'New game started' });
            $("#btnStartNewGame").removeAttr("disabled");
        },
        error: function () {
            $.toaster({ priority: 'danger', title: 'Error', message: 'New game could not be started due to an error' });
            $("#btnStartNewGame").removeAttr("disabled");
        }
    });
});

var teamName = "", password = "";
$("#loginButton").click(function () {

    teamName = $.trim($("#loginText").val());
    password = $.trim($("#passwordText").val());

    if (teamName != "" && password != "") {
        $("#loginDiv").hide();
        $("#game-region").show();
        initializeGameBoard(teamName, password);
    }
});

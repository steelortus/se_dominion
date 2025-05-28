package de.htwg.se.dominion.model

import de.htwg.se.dominion.model.Player
import scala.collection.immutable.List

trait Builder {
    def setNumberOfPlayers(th: TurnHandlerBuilder, numberOfPlayers: Int): TurnHandlerBuilder
    def setTurn(th: TurnHandlerBuilder, turn: Int): TurnHandlerBuilder
    def getResult(): TurnHandler
}

case class TurnHandlerBuilder(numberOfPlayers: Int = 0, turn: Int = 0) extends Builder {

    def setNumberOfPlayers(th: TurnHandlerBuilder, numberOfPlayers: Int): TurnHandlerBuilder = {
        th.copy(numberOfPlayers = numberOfPlayers)
    }

    def setTurn(th: TurnHandlerBuilder, turn: Int): TurnHandlerBuilder = {
        th.copy(turn = turn)
    }

    def getResult(): TurnHandler = {
        TurnHandler(numberOfPlayers, turn)
    }

    override def toString(): String = {
        s"TurnHandlerBuilder(numberOfPlayers=$numberOfPlayers, turn=$turn)"
    }
}

case class TurnHandler(numberOfPlayers: Int, turn: Int, players: List[Player] = List(Player().shuffleDeck().drawFiveCardsFromDeck(),
                                                                                     Player().shuffleDeck().drawFiveCardsFromDeck(),
                                                                                     Player().shuffleDeck().drawFiveCardsFromDeck(),
                                                                                     Player().shuffleDeck().drawFiveCardsFromDeck())) {
    
    def nextTurn(): TurnHandler = {
        val nextTurn = 1
        //val nextTurn = (turn + 1) % numberOfPlayers
        this.copy(turn = nextTurn)
    }

    def getPlayer(): Player = {
        players(turn)
    }

    def updatePlayer(player: Player): TurnHandler = {
        val updatedPlayers = players.updated(turn, player)
        this.copy(players = updatedPlayers)
    }

    def totalTurnCount(): Int = {
        turn
    }

    override def toString: String = {
        s"TurnHandler(numberOfPlayers=$numberOfPlayers, turn=$turn, players=${players.mkString("\n")})"
    }
}
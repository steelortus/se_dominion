package de.htwg.se.dominion.model

import de.htwg.se.dominion.model.Player
import scala.collection.immutable.List

trait Builder {
    def setNumberOfPlayers(numberOfPlayers: Int): TurnHandlerBuilder
    def setTurn(turn: Int): TurnHandlerBuilder
    def getResult(): TurnHandler
}

case class TurnHandlerBuilder(numberOfPlayers: Int = 0, turn: Int = 0) extends Builder {

    def setNumberOfPlayers(noP: Int): TurnHandlerBuilder = {
        this.copy(numberOfPlayers = noP)
    }

    def setTurn(t: Int): TurnHandlerBuilder = {
        this.copy(turn = t)
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
        val nextTurn = turn + 1
        this.copy(turn = nextTurn)
    }

    def getPlayer(): Player = {
        players((turn + numberOfPlayers) % numberOfPlayers)
    }

    def updatePlayer(player: Player): TurnHandler = {
        val updatedPlayers = players.updated(((turn + numberOfPlayers) % numberOfPlayers), player)
        this.copy(players = updatedPlayers)
    }

    def totalTurnCount(): Int = {
        turn
    }

    override def toString: String = {
        s"TurnHandler(numberOfPlayers=$numberOfPlayers, turn=$turn, players=${players.mkString("\n")})"
    }
}
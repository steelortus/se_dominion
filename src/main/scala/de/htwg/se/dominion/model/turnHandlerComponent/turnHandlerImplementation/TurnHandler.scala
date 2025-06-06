package de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation

import de.htwg.se.dominion.model.turnHandlerComponent.{Builder, TurnHandlerInterface}

import de.htwg.se.dominion.model.playerComponent.PlayerInterface
import de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player
import scala.collection.immutable.List

case class TurnHandlerBuilder(numberOfPlayers: Int = 0, turn: Int = 0) extends Builder {

    def setNumberOfPlayers(noP: Int): TurnHandlerBuilder = {
        this.copy(numberOfPlayers = noP)
    }

    def setTurn(t: Int): TurnHandlerBuilder = {
        this.copy(turn = t)
    }

    def getResult(): TurnHandler = {
        new TurnHandler(numberOfPlayers, turn)
    }
}

case class TurnHandler(override val numberOfPlayers: Int, override val turn: Int, override val players: List[PlayerInterface]) extends TurnHandlerInterface(numberOfPlayers, turn, players) {

    def this(numberOfPlayers: Int, turn: Int) = {
        this(numberOfPlayers, turn, List(Player().shuffleDeck().drawFiveCardsFromDeck(),
                                         Player().shuffleDeck().drawFiveCardsFromDeck(),
                                         Player().shuffleDeck().drawFiveCardsFromDeck(),
                                         Player().shuffleDeck().drawFiveCardsFromDeck()))
    }
    
    def nextTurn(): TurnHandler = {
        val nextTurn = turn + 1
        this.copy(turn = nextTurn)
    }

    def getPlayer(): PlayerInterface = {
        players((turn + numberOfPlayers) % numberOfPlayers)
    }

    def updatePlayer(player: PlayerInterface): TurnHandler = {
        val updatedPlayers = players.updated(((turn + numberOfPlayers) % numberOfPlayers), player)
        this.copy(players = updatedPlayers)
    }

    def totalTurnCount(): Int = {
        turn
    }
}
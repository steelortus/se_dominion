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
}

case class TurnHandler(numberOfPlayers: Int, turn: Int, players: List[Player] = List()) {
    
    def nextTurn(): TurnHandler = {
        val nextTurn = (turn + 1) % numberOfPlayers
        this.copy(turn = nextTurn)
    }

    def getPlayer(index: Int): Player = {
        players(index)
    }

    def updatePlayer(index: Int, player: Player): TurnHandler = {
        val updatedPlayers = players.updated(index, player)
        this.copy(players = updatedPlayers)
    }

    def totalTurnCount(): Int = {
        turn
    }
}

object TurnHandler {
    def apply(numberOfPlayers: Int, turn: Int): TurnHandler = {
        new TurnHandler(numberOfPlayers, turn, List.fill(numberOfPlayers)(Player()))
    }
}
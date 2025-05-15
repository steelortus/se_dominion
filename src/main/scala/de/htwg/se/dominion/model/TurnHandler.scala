package de.htwg.se.dominion.model

import de.htwg.se.dominion.model.Player
import scala.collection.immutable.List

case class TurnHandler(numberOfPlayers: Int, turn: Int) {
    val players = List.fill(numberOfPlayers)(Player())

    def nextTurn(): TurnHandler = {
        val nextTurn = (turn + 1) % numberOfPlayers
        this.copy(turn = nextTurn)
    }

    def getPlayer(index: Int): Player = {
        players(index)
    }

    def totalTurnCount(): Int = {
        turn
    }
}
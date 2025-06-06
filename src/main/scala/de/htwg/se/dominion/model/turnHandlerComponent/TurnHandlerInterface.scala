package de.htwg.se.dominion.model.turnHandlerComponent

import de.htwg.se.dominion.model.playerComponent.PlayerInterface
import de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player

trait Builder {
    def setNumberOfPlayers(numberOfPlayers: Int): Builder
    def setTurn(turn: Int): Builder
    def getResult(): TurnHandlerInterface
}

trait TurnHandlerInterface {
    def nextTurn(): TurnHandlerInterface
    def getPlayer(): Player
    def updatePlayer(player: Player): TurnHandlerInterface
    def totalTurnCount(): Int
}
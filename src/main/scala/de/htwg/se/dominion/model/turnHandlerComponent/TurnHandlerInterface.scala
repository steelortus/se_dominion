package de.htwg.se.dominion.model.turnHandlerComponent

import de.htwg.se.dominion.model.playerComponent.PlayerInterface

trait Builder {
    def setNumberOfPlayers(numberOfPlayers: Int): Builder
    def setTurn(turn: Int): Builder
    def getResult(): TurnHandlerInterface
}

trait TurnHandlerInterface(val numberOfPlayers: Int, val turn: Int, val players: List[PlayerInterface]) {
    def nextTurn(): TurnHandlerInterface
    def getPlayer(): PlayerInterface
    def updatePlayer(player: PlayerInterface): TurnHandlerInterface
    def totalTurnCount(): Int
}
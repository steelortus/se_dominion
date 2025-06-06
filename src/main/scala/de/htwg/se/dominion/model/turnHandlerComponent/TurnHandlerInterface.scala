package de.htwg.se.dominion.model.turnHandlerComponent

trait Builder {
    def setNumberOfPlayers(numberOfPlayers: Int): TurnHandlerBuilder
    def setTurn(turn: Int): TurnHandlerBuilder
    def getResult(): TurnHandler
}
package de.htwg.se.dominion
package control.stateComponent

import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import model.Card
import util.UndoManager
import model.turnHandlerComponent.TurnHandlerInterface

trait State {
    def addCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock
    def removeCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock
    def play(stock: Stock, th: TurnHandlerInterface): Boolean
    def fillStock(stock: Stock, um: UndoManager[Stock]): Stock
    def listCards(stock: Stock): String
    def purchase(stock: Stock, card: Card, th: TurnHandlerInterface, um: UndoManager[TurnHandlerInterface]): TurnHandlerInterface
}
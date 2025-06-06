package de.htwg.se.dominion
package control.stateComponent

import de.htwg.se.dominion.model.stockComponent.StockInterface
import model.Card
import util.UndoManager
import model.turnHandlerComponent.TurnHandlerInterface

trait State {
    def addCard(card: String, stock: StockInterface, um: UndoManager[StockInterface]): StockInterface
    def removeCard(card: String, stock: StockInterface, um: UndoManager[StockInterface]): StockInterface
    def play(stock: StockInterface, th: TurnHandlerInterface): Boolean
    def fillStock(stock: StockInterface, um: UndoManager[StockInterface]): StockInterface
    def listCards(stock: StockInterface): String
    def purchase(stock: StockInterface, card: Card, th: TurnHandlerInterface, um: UndoManager[TurnHandlerInterface]): TurnHandlerInterface
}
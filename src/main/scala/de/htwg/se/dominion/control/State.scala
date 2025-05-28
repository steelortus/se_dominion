package de.htwg.se.dominion
package control

import model.Stock
import model.Card
import util.UndoManager
import model.TurnHandler

trait State {
    def addCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock
    def removeCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock
    def play(stock: Stock, th: TurnHandler): Boolean
    def fillStock(stock: Stock, um: UndoManager[Stock]): Stock
    def listCards(stock: Stock): String
    def purchase(stock: Stock, card: Card, th: TurnHandler, um: UndoManager[TurnHandler]): TurnHandler
}
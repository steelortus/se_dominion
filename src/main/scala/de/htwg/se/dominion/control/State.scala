package de.htwg.se.dominion
package control

import model.Stock
import util.UndoManager

trait State {
    def addCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock
    def removeCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock
    def play(stock: Stock, um: UndoManager[Stock]): Boolean
    def fillStock(stock: Stock, um: UndoManager[Stock]): Stock
    def listCards(stock: Stock, um: UndoManager[Stock]): String
}
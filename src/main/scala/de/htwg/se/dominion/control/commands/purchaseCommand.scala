package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.Player
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.util.Command

class PurchaseCommand(card: Card) extends Command[Stock] {
    override def doStep(stock: Stock): Stock = {
        stock.removeCard(card)
    }

    override def undoStep(stock: Stock): Stock = {
        stock.addCard(card)
    }

    override def redoStep(stock: Stock): Stock = {
        doStep(stock)
    }
}
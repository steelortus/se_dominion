package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.util.Command

class RemoveCardCommand(card: Card) extends Command[StockInterface] {
    override def doStep(stock: StockInterface): StockInterface = {
        stock.removeCard(card)
    }

    override def undoStep(stock: StockInterface): StockInterface = {
        stock.addCard(card)
    }

    override def redoStep(stock: StockInterface): StockInterface = {
        doStep(stock)
    }
}
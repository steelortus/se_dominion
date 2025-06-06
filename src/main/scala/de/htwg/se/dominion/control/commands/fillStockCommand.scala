package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.util.Command

class FillStockCommand() extends Command[StockInterface] {
    override def doStep(stock: StockInterface): StockInterface = {
        var debug_stock = stock
        debug_stock = debug_stock.addCard(Card.Garten)
        debug_stock = debug_stock.addCard(Card.Markt)
        debug_stock = debug_stock.addCard(Card.Jahrmarkt)
        debug_stock = debug_stock.addCard(Card.Dieb)
        debug_stock = debug_stock.addCard(Card.Abenteurer)
        debug_stock = debug_stock.addCard(Card.Dorf)
        debug_stock = debug_stock.addCard(Card.Hexe)
        debug_stock = debug_stock.addCard(Card.Laboratorium)
        debug_stock = debug_stock.addCard(Card.Bibliothek)
        debug_stock = debug_stock.addCard(Card.Holzfaeller)
        debug_stock
    }

    override def undoStep(stock: StockInterface): StockInterface = {
        new Stock()
    }

    override def redoStep(stock: StockInterface): StockInterface = {
        doStep(stock)
    }
}
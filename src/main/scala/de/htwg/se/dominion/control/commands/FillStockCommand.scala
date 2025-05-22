package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.util.*
import de.htwg.se.dominion.model.*
import de.htwg.se.dominion.control.*

final case class FillStockCommand(controller: Controller) extends Command {
    private var previousStock: Stock = controller.getStock()

    override def execute(): Unit = {
        previousStock = controller.getStock()
        val newStock = controller.state.fillStock(Stock())
        if (newStock == previousStock) {
            controller.notifyObservers(ErrorEvent.invalidCommand)
        } else {
            controller.stock = newStock
            controller.updateState(Event.stockFull)
        }
    }

    override def undo(): Unit = {
        controller.stock = previousStock
        controller.notifyObservers(Event.cardRemoved)
    }
}

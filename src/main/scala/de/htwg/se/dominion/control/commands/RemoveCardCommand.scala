package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.control.*
import de.htwg.se.dominion.util.*
import de.htwg.se.dominion.model.*
import de.htwg.se.dominion.util.ErrorEvent

final case class RemoveCardCommand(controller: Controller, cardName: String) extends Command {
    private var previousStock: Stock = controller.getStock()

    override def execute(): Unit = {
        previousStock = controller.getStock()
        val newStock = controller.state.removeCard(cardName, previousStock)
        if (newStock == previousStock) {
            controller.notifyObservers(ErrorEvent.couldNotRemoveCard)
        } else {
            controller.stock = newStock
            controller.notifyObservers(Event.cardRemoved)
        }
    }

    override def undo(): Unit = {
        controller.stock = previousStock
        controller.notifyObservers(Event.cardAdded)
    }
}

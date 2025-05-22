package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.*
import de.htwg.se.dominion.util.*
import de.htwg.se.dominion.control.*
import java.util.ResourceBundle.Control

case class AddCardCommand(controller: Controller, cardName: String) extends Command {
    private var previousStock: Stock = controller.getStock()

    override def execute(): Unit = {
        previousStock = controller.getStock()
        val newStock = controller.state.addCard(cardName, previousStock)
        if (newStock == previousStock) {
            if (newStock.getLength() >= 17) {
                controller.notifyObservers(Event.stockFull)
            } else {
                controller.notifyObservers(ErrorEvent.couldNotAddCard)
            }
        } else {
            controller.stock = newStock
            controller.notifyObservers(Event.cardAdded)
            if (newStock.getLength() == 17) {
                controller.notifyObservers(Event.stockFull)
            }
        }
    }

    override def undo(): Unit = {
        controller.stock = previousStock
        controller.notifyObservers(Event.cardRemoved)
    }
}
package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.util.*
import de.htwg.se.dominion.model.*
import de.htwg.se.dominion.control.*

final case class PurchaseCardCommand(controller: Controller, cardName: String) extends Command {
    private var previousPlayer: Player = controller.th.getPlayer(controller.th.turn)
    private var card: Card = Card.NotACard

    override def execute(): Unit = {
        if (!controller.state.isInstanceOf[StatePlaying]) {
            controller.notifyObservers(ErrorEvent.invalidCommand)
            return
        }
        
        val currentPlayer = controller.th.getPlayer(controller.th.turn)
        card = Card.values.find(_.name.equalsIgnoreCase(cardName)).getOrElse(Card.NotACard)
        val money = currentPlayer.getMoneyInHand()

        if (card == Card.NotACard || money < card.getCost) {
            controller.notifyObservers(ErrorEvent.invalidCommand)
        } else {
            val updatedPlayer = currentPlayer.purchaseCard(card, money)
            controller.th = controller.th.updatePlayer(controller.th.turn, updatedPlayer)
            controller.notifyObservers(Event.cardAdded)
        }
    }

    override def undo(): Unit = {
        controller.th = controller.th.updatePlayer(controller.th.turn, previousPlayer)
        controller.notifyObservers(Event.cardRemoved)
    }
}

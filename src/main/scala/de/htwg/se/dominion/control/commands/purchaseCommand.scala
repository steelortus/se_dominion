package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.Player
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.util.Command

class PurchaseCommand(card: Card, stock: Stock) extends Command[TurnHandler] {
    override def doStep(th: TurnHandler): TurnHandler = {
        val newTh = th.updatePlayer(th.getPlayer().purchaseCard(card, stock))
        newTh
    }

    override def undoStep(th: TurnHandler): TurnHandler = {
        val oldTh = th.updatePlayer(th.getPlayer().returnCard(card))
        oldTh
    }

    override def redoStep(th: TurnHandler): TurnHandler = {
        doStep(th)
    }
}
package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.Player
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.model.TurnHandler
import de.htwg.se.dominion.util.Command

class PurchaseCommand(card: Card, stock: Stock) extends Command[TurnHandler] {
    override def doStep(th: TurnHandler): TurnHandler = {
        th.updatePlayer(th.getPlayer().purchaseCard(card, stock))
    }

    override def undoStep(th: TurnHandler): TurnHandler = {
        th.updatePlayer(th.getPlayer().returnCard(card))
    }

    override def redoStep(th: TurnHandler): TurnHandler = {
        doStep(th)
    }
}
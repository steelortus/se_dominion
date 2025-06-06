package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.playerComponent.PlayerInterface
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.util.Command

class PurchaseCommand(card: Card, stock: StockInterface) extends Command[TurnHandlerInterface] {
    override def doStep(th: TurnHandlerInterface): TurnHandlerInterface = {
        val newTh = th.updatePlayer(th.getPlayer().purchaseCard(card, stock))
        newTh
    }

    override def undoStep(th: TurnHandlerInterface): TurnHandlerInterface = {
        val oldTh = th.updatePlayer(th.getPlayer().returnCard(card))
        oldTh
    }

    override def redoStep(th: TurnHandlerInterface): TurnHandlerInterface = {
        doStep(th)
    }
}
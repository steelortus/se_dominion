package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.Player
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.util.Command

class AddCardCommand(card: Card, player: Player) extends Command[Stock] {
    override def doStep(stock: Stock): Stock = {
        player.addCard(card)
    }

    override def undoStep(stock: Stock): Stock = {
        player.removeCard(card)
    }

    override def redoStep(stock: Stock): Stock = {
        doStep()
    }
}
package de.htwg.se.dominion.control.commands

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.Player
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.util.Command

class AddCardCommand(card: Card, player: Player) extends Command[Stock] {
  override def doStep(): Unit = {
    player.addCard(card)
  }

  override def undoStep(): Unit = {
    player.removeCard(card)
  }

  override def redoStep(): Unit = {
    doStep()
  }
}
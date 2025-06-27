package de.htwg.se.dominion.fileio

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler

class FileIOJsonSpec extends AnyWordSpec {
    "write and read game state correctly" in {
        val fileIO = new FileIOJson()

        val stock = new Stock ++ List(Card.Garten, Card.Burggraben, Card.Dorf, Card.Holzfaeller, Card.Werkstatt, Card.Kapelle, Card.Thronsaal, Card.Schmiede, Card.Hexe, Card.Spion)
        val player = new Player(deck = List(Card.Gold), hand = List(Card.Kupfer), discard = Nil, purchases = 1, actions = 1)
        val th = new TurnHandler(2, 1)

        fileIO.save(stock, th)
        val (loadedStock, loadedTH) = fileIO.load()

        loadedStock.stock should contain theSameElementsAs stock.stock
        loadedStock.stockAmount should equal(stock.stockAmount)
        loadedTH.players.head.hand should equal(player.hand)
        loadedTH.turn should equal(1)

    }
}

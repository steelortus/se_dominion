package de.htwg.se.dominion.fileio

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.modules.DefaultSettings.{fileIO}

class FileIOJsonSpec extends AnyWordSpec {
    "write and read a JSON game state correctly" in {
        import de.htwg.se.dominion.modules.TestSettings.{fullStock, newTh, testPlayStateFullStock}
        val controller = new Controller()

        controller.save()
        val (loadedStock, loadedTH) = controller.fileIO.load()

        loadedStock.stock should contain theSameElementsAs controller.stock.stock
        loadedStock.stockAmount should equal(controller.stock.stockAmount)
        loadedTH.players.head.hand should equal(controller.th.getPlayer().hand)
        loadedTH.turn should equal(0)
    }

    "write and read a XML game state correctly" in {
        import de.htwg.se.dominion.modules.TestSettings.{fullStock, newTh, testPlayStateFullStock, newFileIO}
        val controller = new Controller()

        controller.save()
        val (loadedStock, loadedTH) = controller.fileIO.load()

        loadedStock.stock should contain theSameElementsAs controller.stock.stock
        loadedStock.stockAmount should equal(controller.stock.stockAmount)
        loadedTH.players.head.hand should equal(controller.th.getPlayer().hand)
        loadedTH.turn should equal(0)
    }
}

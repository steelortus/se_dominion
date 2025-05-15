package de.htwg.se.dominion.control

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util._
import de.htwg.se.dominion.control._

class ControllerSpec extends AnyWordSpec {

    "A Controller" should {

        val initialStock = new Stock
        val testHandler = new TurnHandler(2, 0)
        val testState = new StatePreparation(initialStock)
        val testCard = Card.Markt

        "start in Preparation State" in {
            val controller = Controller(initialStock, testState, testHandler)
            controller.state should be(StatePreparation(initialStock))
        }

        "return it's associated stock" in {
            val controller = Controller(initialStock, testState, testHandler)
            controller.getStock() should be(initialStock)
        }

        "return a list of all Cards not in it's Stock in Preparation State" in {
            val controller = Controller(initialStock, testState, testHandler)
            val regx = "Liste der noch verfuegbaren Karten:\n\n([a-zA-Z]* \\| )*[a-zA-Z]*"
            controller.listCards() should fullyMatch regex regx
        }

        "return of a list of all Cards in it's Stock in Playing State" in {
            val controller = Controller(initialStock, testState, testHandler)
            controller.fillStock()
            controller.play()
            val regx = "([a-zA-Z]+ - Cost: [0-8]{1}, Value: [0123]{1}, Points: (-1|[01369])+, Amount: [0-9]+\n?)*"
            controller.listCards() should fullyMatch regex regx
        }

        "add a Card to the Stock in Preparation State" in {
            val controller = Controller(initialStock, testState, testHandler)
            val newStock = controller.state.addCard(testCard.toString, initialStock)
            newStock.contains(testCard) should be(true)
        }
    }
}

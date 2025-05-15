package de.htwg.se.dominion.control

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.control._
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util._

class ControllerSpec extends AnyWordSpec {

    "A Controller" should {

        val initialStock = new Stock
        val testCard = Card.Markt

        "start in Preparation State" in {
            val controller = Controller(initialStock)
            controller.state should be(StatePreparation(controller, initialStock))
        }

        "switch to Playing State when play is called if Stock is full" in {
            val controller = Controller(initialStock)

            controller.play()
            controller.state should be(StatePreparation(controller, initialStock))

            controller.fillStock()
            controller.play()
            controller.state should be(StatePlaying(controller, controller.getStock()))
        }

        "return it's associated stock" in {
            val controller = Controller(initialStock)
            controller.getStock() should be(initialStock)
        }

        "return a list of all Cards not in it's Stock in Preparation State" in {
            val controller = Controller(initialStock)
            val regx = "Liste der noch verfuegbaren Karten:\n\n([a-zA-Z]* \\| )*[a-zA-Z]*"
            controller.listCards() should fullyMatch regex regx
        }

        "return of a list of all Cards in it's Stock in Playing State" in {
            val controller = Controller(initialStock)
            controller.fillStock()
            controller.play()
            val regx = "([a-zA-Z]+ - Cost: [0-8]{1}, Value: [0123]{1}, Points: (-1|[01369])+, Amount: [0-9]+\n?)*"
            controller.listCards() should fullyMatch regex regx
        }

        "add a Card to the Stock in Preparation State" in {
            val controller = Controller(initialStock)
            val newStock = controller.state.addCard(testCard.toString, initialStock)
            newStock.contains(testCard) should be(true)
        }
    }
}

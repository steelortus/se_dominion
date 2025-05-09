package de.htwg.se.dominion.control

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util._

class ControllerSpec extends AnyWordSpec {

    "A Controller" should {

        val initialStock = new Stock
        val testCard = Card.Markt

        "add a card to the stock if it is not full and the card is not already present" in {
            val controller = Controller(initialStock)

            val result = controller.addCard(testCard.toString())

            result should include ("Card added successfully")
            controller.getStock().contains(testCard) should be(true)
        }

        "not add a card if it is already in stock" in {
            val controller = Controller(initialStock)
            controller.addCard(testCard.toString())

            val result = controller.addCard(testCard.toString())

            result should include ("Maybe it's already in it")
            controller.getStock().getLength() should be(8)
        }

        "not add a card if stock is full" in {
            val controller = Controller(initialStock)
            val fullStock = controller.fillStock()
        
            val result = controller.addCard("Garten")

            result should include ("Stock is already full")
            controller.getStock().getLength() should be(17)
        }

        "notify observers with stockFull event when stock reaches 17 cards" in {
            val controller = Controller(initialStock)

            var receivedEvent: Option[Event] = None
            val testObserver = new Observer {
                override def update(e: Event): Unit = receivedEvent = Some(e)
            }
            controller.add(testObserver)
            
            val cards = List(
                Card.Burggraben, Card.Kapelle, Card.Keller, Card.Dorf, Card.Holzfaeller,
                Card.Werkstatt, Card.Buerokrat, Card.Dieb, Card.Festmahl, Card.Geldverleiher
            )

            cards.foreach(card => controller.addCard(card.toString()))

            receivedEvent should be(Some(Event.stockFull))
        }

        "remove a card that is in stock" in {
            val controller = Controller(initialStock)
            controller.fillStock()

            val result = controller.removeCard(testCard.toString())

            result should include ("Card removed successfully")
            controller.getStock().contains(testCard) should be(false)
        }

        "not remove a card that is not in stock" in {
            val controller = Controller(initialStock)

            val result = controller.removeCard(testCard.toString())

            result should include ("Maybe it's not in it")
        }

        "fill stock with predefined cards" in {
            val controller = Controller(initialStock)
            controller.fillStock()

            controller.getStock().getLength() should be > 0
            controller.getStock().getLength() should be(17)
            controller.getStock().getCard("Holzfaeller") should be(Card.Holzfaeller)
        }

        "list cards that are not in the stock" in {
            val controller = Controller(initialStock)

            noException should be thrownBy controller.listCards()
        }
    }
}

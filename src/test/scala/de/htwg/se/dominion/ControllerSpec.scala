package de.htwg.se.dominion.control

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.control._
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util._
import de.htwg.se.dominion.control._
import scala.annotation.init
import munit.Test

class ControllerSpec extends AnyWordSpec {

    class TestObserver extends Observer {
        var lastEvent: Option[Event] = None
        var lastErrorEvent: Option[ErrorEvent] = None

        override def update(e: Event): Unit = {
            lastEvent = Some(e)
        }

        override def update(e: ErrorEvent): Unit = {
            lastErrorEvent = Some(e)
        }
    }

    "A Controller" should {

        val initialStock = new Stock
        val fullStock = new Stock ++ List(Card.Garten, Card.Burggraben, Card.Dorf, Card.Holzfaeller, Card.Werkstatt, Card.Kapelle, Card.Thronsaal, Card.Schmiede, Card.Hexe, Card.Spion)
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
            controller.createPlayers(2)
            val regx = "([a-zA-Z]+ - Cost: [0-8]{1}, Value: [0123]{1}, Points: (-1|[01369])+, Amount: [0-9]+\n?)*"
            controller.listCards() should fullyMatch regex regx
        }

        "add a Card to the Stock in Preparation State" in {
            val controller = Controller(initialStock, testState, testHandler)
            val testObserver = new TestObserver()
            controller.add(testObserver)
            controller.addCard(testCard.toString)
            testObserver.lastEvent should be(Some(Event.cardAdded))
        }

        "notify if Stock is full" in {
            val controller = Controller(fullStock, testState, testHandler)
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.addCard(testCard.toString)
            testObserver.lastEvent should be(Some(Event.stockFull))
        }

        "notify if card could not be added" in {
            val controller = Controller(initialStock, testState, testHandler)
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.addCard(Card.Provinz.toString)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotAddCard))
        }

        "not remove a card from default stock" in {
            val controller = Controller(initialStock, testState, testHandler)
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(Card.Provinz.toString)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotRemoveCard))
        }

        "not remove a card that is not in stock" in {
            val controller = Controller(initialStock, testState, testHandler)
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(testCard.toString)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotRemoveCard))
        }

        "notify if card has been removed" in {
            val controller = Controller(fullStock, testState, testHandler)
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(Card.Garten.toString)
            testObserver.lastEvent should be(Some(Event.cardRemoved))
        }
    }
}

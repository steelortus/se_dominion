package de.htwg.se.dominion.control

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation
import de.htwg.se.dominion.control.stateComponent.statePlayingImplementation.StatePlaying
import de.htwg.se.dominion.util.Observer
import de.htwg.se.dominion.util.Observable
import de.htwg.se.dominion.util.Event
import de.htwg.se.dominion.util.ErrorEvent
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.util.UndoManager
import scala.annotation.init
import munit.Test
import de.htwg.se.dominion.modules.DefaultSettings.{stock, th, state, fileIO}
import de.htwg.se.dominion.fileio.FileIOInterface
import de.htwg.se.dominion.fileio.FileIOJson

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
        //given fileIO: FileIOInterface = new FileIOJson()

        val initialStock = new Stock
        val testFullStock = new Stock ++ List(Card.Garten, Card.Burggraben, Card.Dorf, Card.Holzfaeller, Card.Werkstatt, Card.Kapelle, Card.Thronsaal, Card.Schmiede, Card.Hexe, Card.Spion)
        val testHandler = new TurnHandler(2, 0)
        val testState = new StatePreparation(initialStock)
        val testCard = Card.Markt

        "start in Preparation State" in {
            val controller = Controller()
            controller.state should be(StatePreparation(initialStock))
        }

        "return it's associated stock" in {
            val controller = Controller()
            controller.getStock() should be(initialStock)
        }

        "return a list of all Cards not in it's Stock in Preparation State" in {
            val controller = Controller()
            val regx = "Liste der noch verfuegbaren Karten:\n\n([a-zA-Z]* \\| )*[a-zA-Z]*"
            controller.listCards() should fullyMatch regex regx
        }

        "return of a list of all Cards in it's Stock in Playing State" in {
            val controller = Controller()
            controller.fillStock()
            controller.createPlayers(2)
            val regx = "([a-zA-Z]+ - Cost: [0-8]{1}, Value: [0123]{1}, Points: (-1|[01369])+, Amount: [0-9]+\n?)*"
            controller.listCards() should fullyMatch regex regx
        }

        "add a Card to the Stock in Preparation State" in {
            val controller = Controller()
            val testObserver = new TestObserver()
            controller.add(testObserver)
            controller.addCard(testCard.toString)
            testObserver.lastEvent should be(Some(Event.cardAdded))
            controller.state.addCard(Card.Dorf.toString, initialStock, controller.prepUndoManager) should not equal controller.stock
        }

        "notify if Stock is full" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.addCard(testCard.toString)
            testObserver.lastEvent should be(Some(Event.stockFull))
        }

        "notify if card could not be added" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.addCard(Card.Provinz.toString)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotAddCard))
        }

        "not remove a card from default stock" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(Card.Provinz.toString)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotRemoveCard))
        }

        "not remove a card that is not in stock" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(testCard.toString)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotRemoveCard))
        }

        "notify if card has been removed" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(Card.Garten.toString)
            testObserver.lastEvent should be(Some(Event.cardRemoved))
            controller.state.removeCard(Card.Dorf.toString, testFullStock, controller.prepUndoManager) should not equal controller.stock
        }

        "before game starts ask for player quantity if stock is full" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.play()
            testObserver.lastEvent should be(Some(Event.selectNumberOfPlayers))
        }

        "not start the game if stock is not filled" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.play()
            testObserver.lastErrorEvent should be(Some(ErrorEvent.cantStart))
        }

        "not try to fill an already full stock" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.fillStock()
            testObserver.lastErrorEvent should be(Some(ErrorEvent.invalidCommand))
        }

        "only accept player count between 2 and 4" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(1)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.invalidNumberOfPlayers))
            testObserver.lastEvent should be(Some(Event.selectNumberOfPlayers))
        }

        "not add card to stock while playing" in {
            import de.htwg.se.dominion.modules.TestSettings.{testPlayState}
            val playState = StatePlaying(initialStock)
            val controller = Controller()
            controller.addCard(Card.Geldverleiher.toString)
            controller.getStock() should equal (initialStock)
        }

        "not remove a card from stock while playing" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock, testPlayState}
            val playState = StatePlaying(testFullStock)
            val controller = Controller()
            controller.removeCard(Card.Garten.toString)
            controller.getStock() should equal (testFullStock)
        }

        "not fill cards to stock while playing" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val playState = StatePlaying(testFullStock)
            val controller = Controller()
            controller.fillStock()
            controller.getStock() should equal (testFullStock)
        }

        "not purchase a Card while in Preparation State" in {
            import de.htwg.se.dominion.modules.TestSettings.{newTh}
            val controller = Controller()
            val oldTh = controller.th
            controller.purchase(Card.Kupfer)
            oldTh == controller.th should be(true)
        }

        "be able to return the current turn" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            controller.getTurn() should be(0)
        }

        "be able to return the current player's Hand as String" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            controller.createPlayers(2)
            val player = controller.getPlayerHand()
            player should fullyMatch regex "(((Deck|Hand|Discard):\n?)?([a-zA-Z]+ - Value: [0123]{1}, Points: (-1|[01369])+\n?)*\n?)+"
        }

        "be able to return the current player's money in Hand" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            controller.createPlayers(2)
            val playerMoney = controller.getPlayerMoney()
            playerMoney should (be >= 2 and be <= 5)
        }

        "be able to return how many purchases the current player has left" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            controller.createPlayers(2)
            val purchases = controller.getPlayerPurchases()
            purchases should be(1)
        }

        "be able to return how many actions the current player has left" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            controller.createPlayers(2)
            val actions = controller.getPlayerActions()
            actions should be(1)
        }

        "observe ErrorEvent.invalidCommand when nextTurn() is called if the State is in Preparation" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.nextTurn()
            testObserver.lastErrorEvent should be(Some(ErrorEvent.invalidCommand))
        }

        "go to the next turn in Playing State" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            val initialTurn = controller.getTurn()
            controller.nextTurn()
            controller.getTurn() should be(initialTurn + 1)
        }

        "purchase a card" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.purchase(Card.Kupfer)
            controller.th.getPlayer().discard.contains(Card.Kupfer) should be(true)
            testObserver.lastEvent should be(Some(Event.playing))
        }

        "not purchase a card if the player has no purchases left" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.purchase(Card.Kupfer)
            controller.purchase(Card.Kupfer)
            testObserver.lastEvent should be(Some(Event.playing))
            testObserver.lastErrorEvent should be(Some(ErrorEvent.outOfPurchases))
        }

        "not purchase a card if the player has no money left or the card is not in the stock" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.purchase(Card.Gold)
            testObserver.lastEvent should be(Some(Event.playing))
            testObserver.lastErrorEvent should be(Some(ErrorEvent.couldNotPurchaseCard))
        }

        "be able to purchase a card with a String of a card" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.purchase(Card.Kupfer.toString)
            controller.th.getPlayer().discard.contains(Card.Kupfer) should be(true)
            testObserver.lastEvent should be(Some(Event.playing))
        }

        "not end the game if it hasn't started yet" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.endGame()
            testObserver.lastErrorEvent should be(Some(ErrorEvent.invalidCommand))
        }

        "be able to end the game" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.play()
            controller.endGame()
            testObserver.lastEvent should be(Some(Event.endGame))
        }

        "be able to get a List of Points of all Players" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            controller.createPlayers(2)
            controller.getAllPoints() should be(List(3, 3))
        }

        "display Preparation commands in Preparation State" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.showHelp()
            testObserver.lastEvent should be(Some(Event.commandsPreparation))
        }

        "display Playing commands in Playing State" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.play()
            controller.showHelp()
            testObserver.lastEvent should be(Some(Event.commandsPlaying))
        }

        "signal an Event that the State is now in Preparation" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.updateState(Event.preparation)
            testObserver.lastEvent should be(Some(Event.preparation))
        }

        "return an invalid State Event if the wrong Event is sent to updateState" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.updateState(Event.endGame)
            testObserver.lastErrorEvent should be(Some(ErrorEvent.invalidState))
        }

        "undo and redo adding Cards in Preparation State" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.addCard(testCard.toString)
            val addedStock = controller.getStock()
            controller.undo()
            addedStock should not equal controller.getStock()
            controller.redo()
            addedStock should equal(controller.getStock())
        }

        "undo and redo removing Cards in Preparation State" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.removeCard(Card.Garten.toString)
            val removedStock = controller.getStock()
            controller.undo()
            removedStock should not equal controller.getStock()
            controller.redo()
            removedStock should equal(controller.getStock())
        }

        "undo and redo filling the Stock in Preparation State" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.fillStock()
            val filledStock = controller.getStock()
            controller.undo()
            filledStock should not equal controller.getStock()
            controller.redo()
            filledStock should equal(controller.getStock())
        }

        "undo and redo purchasing Cards in Playing State" in {
            import de.htwg.se.dominion.modules.TestSettings.{fullStock}
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.createPlayers(2)
            controller.play()
            controller.purchase(Card.Kupfer)
            val newTh = controller.th
            controller.undo()
            newTh should not equal controller.th
            controller.redo()
            newTh should equal(controller.th)
        }

        "fill a stock in Preparation State" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            val newStock = controller.state.fillStock(initialStock, controller.prepUndoManager)
            newStock should not equal initialStock
        }

        "remove an observable again" in {
            val controller = Controller()
            val testObserver = new TestObserver
            controller.add(testObserver)
            controller.remove(testObserver)
        }
    }
}
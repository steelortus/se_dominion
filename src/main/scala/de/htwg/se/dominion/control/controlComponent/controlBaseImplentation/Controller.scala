package de.htwg.se.dominion
package control.controlComponent.controlBaseImplentation

import control.controlComponent.ControllerInterface

import util.Observable
import util.Event
import util.ErrorEvent
import model.Stock
import model.Card
import model.Player
import model.ConsoleColors.*
import model.turnHandlerComponent.TurnHandlerInterface
import model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import util.UndoManager
import control.stateComponent.State
import control.stateComponent.statePreparationImplementation.StatePreparation
import control.stateComponent.statePlayingImplementation.StatePlaying
import util.Command

case class Controller(var stock: Stock, var state: State, var th: TurnHandler) extends ControllerInterface with Observable {

    val prepUndoManager = new UndoManager[Stock]
    val playUndoManager = new UndoManager[TurnHandler]

    def getStock(): Stock = {
        stock
    }

    def addCard(card: String): Unit = {
        val newStock = state.addCard(card, stock, prepUndoManager)
        if (stock == newStock) {
            if (newStock.getLength() == 17) {
                notifyObservers(Event.stockFull)
            } else {
                notifyObservers(ErrorEvent.couldNotAddCard)
            }
        } else {
            stock = newStock
            notifyObservers(Event.cardAdded)
        }
    }

    def removeCard(card: String): Unit = {
        val newStock = state.removeCard(card, stock, prepUndoManager)
        if (stock == newStock) {
            notifyObservers(ErrorEvent.couldNotRemoveCard)
        } else {
            stock = newStock
            notifyObservers(Event.cardRemoved)
        }
    }

    def play(): Unit = {
        if (state.play(stock, th) == true) {
            notifyObservers(Event.selectNumberOfPlayers)
        } else {
            notifyObservers(ErrorEvent.cantStart)
        }
    }

    def fillStock(): Unit = {
        val newStock = state.fillStock(stock, prepUndoManager)
        if (stock == newStock) {
            notifyObservers(ErrorEvent.invalidCommand)
        } else {
            stock = newStock
            updateState(Event.stockFull)
        }
    }

    def listCards(): String = {
        state.listCards(stock)
    }

    def createPlayers(noP: Int): Unit = {
        if (noP < 2 || noP > 4) {
            notifyObservers(ErrorEvent.invalidNumberOfPlayers)
            notifyObservers(Event.selectNumberOfPlayers)
        } else {
            val builder = new TurnHandlerBuilder()
            val finishedBuilder = builder.setNumberOfPlayers(noP).setTurn(0)
            th = finishedBuilder.getResult()
            updateState(Event.playing)
        }
    }

    def nextTurn(): Unit = {
        state match {
            case StatePreparation(_) =>
                notifyObservers(ErrorEvent.invalidCommand)
            case StatePlaying(_) =>
                val player = th.getPlayer()
                th = th.updatePlayer(player.nextTurn())
                th = th.nextTurn()
                notifyObservers(Event.playing)
        }
    }

    def getTurn(): Int = {
        th.turn
    }

    def getPlayerHand(): String = {
        th.getPlayer().handToString()
    }

    def getPlayerMoney(): Int = {
        th.getPlayer().getMoneyInHand()
    }

    def getPlayerActions(): Int = {
        th.getPlayer().actions
    }

    def getPlayerPurchases(): Int = {
        th.getPlayer().purchases
    }

    def purchase(card: Card): Unit = {
        val newTh = state.purchase(stock, card, th, playUndoManager)
        if (th == newTh) {
            if (getPlayerPurchases() > 0) {
                notifyObservers(Event.playing)
                notifyObservers(ErrorEvent.couldNotPurchaseCard)
            } else {
                notifyObservers(Event.playing)
                notifyObservers(ErrorEvent.outOfPurchases)
            }
        } else {
            th = newTh
            notifyObservers(Event.playing)
        }
    }

    def purchase(card: String): Unit = {
        purchase(stock.getCard(card))
    }

    def endGame(): Unit = {
        state match {
            case StatePreparation(stock) =>
                notifyObservers(ErrorEvent.invalidCommand)
            case StatePlaying(stock) =>
                notifyObservers(Event.endGame)
        }
    }

    def getAllPoints(): List[Int] = {
        th.players.take(th.numberOfPlayers).map(_.getPoints())
    }

    def showHelp(): Unit = {
        state match {
            case StatePreparation(stock) =>
                notifyObservers(Event.commandsPreparation)
            case StatePlaying(stock) =>
                notifyObservers(Event.commandsPlaying)
        }
    }

    def updateState(e: Event): Unit = {
        e match {
            case Event.preparation =>
                state = StatePreparation(stock)
                notifyObservers(Event.preparation)
            case Event.stockFull =>
                state = StatePreparation(stock)
                notifyObservers(Event.stockFull)
            case Event.playing =>
                state = StatePlaying(stock)
                notifyObservers(Event.playing)
            case _ =>
                notifyObservers(ErrorEvent.invalidState)
        }
    }

    def undo(): Unit = {
        state match {
            case StatePreparation(stock) =>
                this.stock = prepUndoManager.undoStep(this.stock)
                notifyObservers(Event.undoPrep)
            case StatePlaying(stock) =>
                this.th = playUndoManager.undoStep(this.th)
                notifyObservers(Event.undoPlay)
        }
    }

    def redo(): Unit = {
        state match {
            case StatePreparation(stock) =>
                this.stock = prepUndoManager.redoStep(this.stock)
                notifyObservers(Event.redoPrep)
            case StatePlaying(stock) =>
                this.th = playUndoManager.redoStep(this.th)
                notifyObservers(Event.redoPlay)
        }
    }
}
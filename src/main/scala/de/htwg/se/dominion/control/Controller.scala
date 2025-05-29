package de.htwg.se.dominion
package control

import util.Observable
import util.Event
import util.ErrorEvent
import model.Stock
import model.Card
import model.Player
import model.ConsoleColors.*
import model.TurnHandler
import model.TurnHandlerBuilder
import util.UndoManager
import control.State
import util.Command

case class Controller(var stock: Stock, var state: State, var th: TurnHandler) extends Observable {

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
            println(th.toString())
            updateState(Event.playing)
        }
    }

    def nextTurn(): Unit = {
        val resetActionsAndPurchases = th.getPlayer()
        th.updatePlayer(resetActionsAndPurchases.copy(purchases = 1, actions = 1))
        th = th.nextTurn()
        notifyObservers(Event.playing)
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

    def purchase(card: String): Unit = {
        val cardOpt = stock.getCard(card)
        val newTh = state.purchase(stock, cardOpt, th, playUndoManager)
        notifyObservers(Event.playing)
    }

    def showHelp(): Unit = {
        state match {
            case StatePreparation(_) =>
                notifyObservers(Event.commandsPreparation)
            case StatePlaying(_) =>
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
        stock = prepUndoManager.undoStep(stock)
        th = playUndoManager.undoStep(th)
    }

    def redo(): Unit = {
        stock = prepUndoManager.redoStep(stock)
        th = playUndoManager.redoStep(th)
    }
}
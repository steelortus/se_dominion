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
        th = state.purchase(stock, card, th, playUndoManager)
        if (getPlayerPurchases() > 0) {
            notifyObservers(Event.playing)
        } else {
            notifyObservers(Event.playing)
            notifyObservers(Event.outOfPurchases)
        }
    }

    def purchase(card: String): Unit = {
        purchase(stock.getCard(card))
    }

    def endGame(): Unit = {
        state match {
            case StatePreparation(_) =>
                notifyObservers(ErrorEvent.invalidCommand)
            case StatePlaying(_) =>
                notifyObservers(Event.endGame)
        }
    }

    def getAllPoints(): List[Int] = {
        th.players.take(th.numberOfPlayers).map(_.getPoints())
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
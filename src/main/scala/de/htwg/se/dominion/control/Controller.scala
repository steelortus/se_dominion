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

case class Controller(var stock: Stock, var state: State, var th: TurnHandler) extends Observable {

    def getStock(): Stock = {
        stock
    }

    def addCard(card: String): Unit = {
        val newStock = state.addCard(card, stock)
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
        val newStock = state.removeCard(card, stock)
        if (stock == newStock) {
            notifyObservers(ErrorEvent.couldNotRemoveCard)
        } else {
            stock = newStock
            notifyObservers(Event.cardRemoved)
        }
    }

    def play(): Unit = {
        if (state.play(stock) == true) {
            notifyObservers(Event.selectNumberOfPlayers)
        } else {
            notifyObservers(ErrorEvent.cantStart)
        }
    }

    def fillStock(): Unit = {
        val newStock = state.fillStock(new Stock())
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
            th = new TurnHandler(noP, 0)
            updateState(Event.playing)
        }
    }

    def nextTurn(): Unit = {
        th = th.nextTurn()
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
}
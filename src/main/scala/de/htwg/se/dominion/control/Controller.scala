package de.htwg.se.dominion
package control

import util.Observable
import util.Event
import model.Stock
import model.Card
import model.Player
import model.ConsoleColors.*

case class Controller(var stock: Stock) extends Observable {

    var state: State = StatePreparation(this, stock)

    def addCard(card: String): Unit = {
        stock = state.addCard(card, stock)
    }

    def removeCard(card: String): Unit = {
        stock = state.removeCard(card, stock)
    }

    def play(): Unit = {
        if (state.play(stock) == true) {
            updateState(Event.playing)
        }
    }

    def fillStock(): Unit = {
        stock = state.fillStock(new Stock())
        updateState(Event.stockFull)
    }

    def listCards(): String = {
        state.listCards(stock)
    }

    def updateState(e: Event): Unit = {
        e match {
            case Event.preparation =>
                state = StatePreparation(this, stock)
                notifyObservers(Event.preparation)
            case Event.stockFull =>
                state = StatePreparation(this, stock)
                notifyObservers(Event.stockFull)
            case Event.playing =>
                state = StatePlaying(this, stock)
                notifyObservers(Event.playing)
        }
    }
}
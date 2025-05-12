package de.htwg.se.dominion
package control

import util.Observable
import util.Event
import model.Stock
import model.Card
import model.Player
import model.ConsoleColors.*

case class Controller(var stock: Stock) extends Observable {

    var state: State = StatePreparation()

    def addCard(card: String): Unit = {
        stock = state.addCard(card)
    }

    def removeCard(card: String): Unit = {
        stock = state.removeCard(card)
    }

    def play(): Unit = {
        if (state.play() == true) {
            updateState(Event.playing)
        }
    }

    def fillStock(): Unit = {
        stock = state.fillStock(new Stock())
        updateState(Event.stockFull)
    }

    def listCards(): String = {
        state.listCards()
    }

    def updateState(e: Event): Unit = {
        e match {
            case Event.preparation =>
                state = StatePreparation()
                notifyObservers(Event.preparation)
            case Event.stockFull =>
                state = StatePreparation()
                notifyObservers(Event.stockFull)
            case Event.playing =>
                state = StatePlaying()
                notifyObservers(Event.playing)
        }
    }
}
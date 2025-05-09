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

    def addCard(card: String): String = {
        state.addCard(card)
    }

    def removeCard(card: String): String = {
        val updatedStock = stock.removeCard(stock.getCard(card))
        if (updatedStock == stock) {
            RED("Cannot remove this Card from the Stock. Maybe it's not in it?")
        } else {
            stock = updatedStock
            GREEN("Card removed successfully!")
        }
    }

    def play(): Unit = {
        if (stock.getLength() < 17) {
            println(RED(s"There is not enough cards in the Stock! ${stock.getLength()} out of required 17."))
        } else {
            updateState(Event.playing)
            var p1 = new Player()
            println(YELLOW(s"Player 1 Deck:\n${p1.deckToString()}\n"))
            p1 = p1.shuffleDeck()
            println(GREEN(s"Player 1 shuffled Deck:\n${p1.deckToString()}\n"))
            p1 = p1.drawCardFromDeck()
            println(YELLOW(s"Player 1 Deck after drawing:\n${p1.deckToString()}\n"))
            println(PURPLE(s"Player 1 Hand:\n${p1.handToString()}\n"))
            p1 = p1.drawCardFromDeck()
            p1 = p1.drawCardFromDeck()
            println(PURPLE(s"Player 1 Hand after drawing 2 more from Hand:\n${p1.handToString()}\n"))
            p1 = p1.discardFromHand(1)
            println(YELLOW(s"Player 1 Deck after discarding from Hand:\n${p1.deckToString()}\n"))
            println(PURPLE(s"Player 1 Hand after discarding from Hand:\n${p1.handToString()}\n"))
            println(CYAN(s"Player 1 Discard after discarding from Hand:\n${p1.discardToString()}\n"))

            println(YELLOW("\nGame over! (not implemented yet)\n\n"))
            var p2 = new Player()
            println(YELLOW(s"Player 2 Deck:\n${p2.deckToString()}\n"))
            p2 = p2.shuffleDeck()
            println(GREEN(s"Player 2 shuffled Deck:\n${p2.deckToString()}\n"))
            p2 = p2.drawFiveCardsFromDeck()
            println(YELLOW(s"Player 2 Deck after drawing five times:\n${p2.deckToString()}\n"))
            println(PURPLE(s"Player 1 Hand:\n${p2.handToString()}\n"))
            println(RED(s"Player 2 Money in Hand: ${p2.getMoneyInHand()}\n"))
            p2 = p2.discardAllFromHand()
            println(PURPLE(s"Player 2 Hand after discarding from Hand:\n${p2.handToString()}\n"))
            println(CYAN(s"Player 2 Discard after discarding from Hand:\n${p2.discardToString()}\n"))
        }
    }

    def fillStock(): Unit = {
        var debug_stock = stock
        debug_stock = debug_stock.addCard(Card.Garten)
        debug_stock = debug_stock.addCard(Card.Markt)
        debug_stock = debug_stock.addCard(Card.Jahrmarkt)
        debug_stock = debug_stock.addCard(Card.Dieb)
        debug_stock = debug_stock.addCard(Card.Abenteurer)
        debug_stock = debug_stock.addCard(Card.Dorf)
        debug_stock = debug_stock.addCard(Card.Hexe)
        debug_stock = debug_stock.addCard(Card.Laboratorium)
        debug_stock = debug_stock.addCard(Card.Bibliothek)
        debug_stock = debug_stock.addCard(Card.Holzfaeller)
        updateState(Event.stockFull)
        stock = debug_stock
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
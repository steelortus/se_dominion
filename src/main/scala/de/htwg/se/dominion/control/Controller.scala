package de.htwg.se.dominion
package control

import util.Observable
import util.Event
import model.Stock
import model.Card
import model.Player
import model.ConsoleColors.*

case class Controller(var stock: Stock) extends Observable {

    def addCard(card: String): String = {
        val updatedStock = stock.addCard(stock.getCard(card))
        if (updatedStock == stock) {
            if (updatedStock.getLength() == 17) {
                RED("Cannot add this Card to the Stock. The Stock is already full!")
            } else {
                RED("Cannot add this Card to the Stock. Maybe it's already in it?")
            }
        } else {
            stock = updatedStock
            if (stock.getLength() == 17) {
                notifyObservers(Event.stockFull)
            }
            GREEN("Card added successfully!")
        }
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
            notifyObservers(Event.play)
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
        notifyObservers(Event.stockFull)
        stock = debug_stock
    }

    def getStock(): Stock = stock

    def listCards(): Unit = {
        val all_cards = List[Card](Card.Burggraben, Card.Kapelle, Card.Keller, Card.Dorf, Card.Holzfaeller,
                        Card.Werkstatt, Card.Buerokrat, Card.Dieb, Card.Festmahl, Card.Geldverleiher, Card.Miliz,
                        Card.Schmiede, Card.Spion, Card.Thronsaal, Card.Umbau, Card.Bibliothek, Card.Hexe,
                        Card.Jahrmarkt, Card.Laboratorium, Card.Markt, Card.Mine, Card.Ratsversammlung,
                        Card.Abenteurer, Card.Garten)
        val notIncluded = all_cards.filterNot(card => stock.contains(card))
        println(YELLOW("Liste der noch verfuegbaren Karten:"))
        println(CYAN(notIncluded.map(_.name).mkString(" | ")))
    }
}
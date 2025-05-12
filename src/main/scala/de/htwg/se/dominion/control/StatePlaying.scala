package de.htwg.se.dominion
package control

import control.State
import model.ConsoleColors.*
import model.Stock
import model.Card
import model.Player
import util.Event
import util.Observable
import util.Observer

case class StatePlaying() extends State {
    override def addCard(card: String): String = {
        RED("Cannot add a card in playing state!")
    }

    override def removeCard(card: String): String = {
        RED("Cannot remove a card in playing state!")
    }

    override def play(): Boolean = {
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

        notifyObservers(ErrorEvent.cantStart)
        false
    }

    override def fillStock(stock: Stock): Stock = {
        notifyObservers(ErrorEvent.invalidCommand)
        stock
    }

    override def listCards(): String = {
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
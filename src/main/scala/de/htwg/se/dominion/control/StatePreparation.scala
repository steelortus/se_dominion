package de.htwg.se.dominion
package control

import control.State
import model.Stock
import model.Card
import model.Player
import util.Event
import util.ErrorEvent
import util.Observable

case class StatePreparation(controller: Controller, stock: Stock) extends State {

    override def addCard(card: String, stock: Stock): Stock = {
        val updatedStock = stock.addCard(stock.getCard(card))
        if (updatedStock == stock) {
            if (updatedStock.getLength() == 17) {
                controller.notifyObservers(ErrorEvent.stockFull)
                updatedStock
            } else {
                controller.notifyObservers(ErrorEvent.couldNotAddCard)
                updatedStock
            }
        } else {
            controller.notifyObservers(Event.cardAdded)
            if (updatedStock.getLength() == 17) {
                controller.notifyObservers(Event.stockFull)
            }
            updatedStock
        }
    }

    override def removeCard(card: String, stock: Stock): Stock = {
        val updatedStock = stock.removeCard(stock.getCard(card))
        if (updatedStock == stock) {
            controller.notifyObservers(ErrorEvent.couldNotRemoveCard)
            updatedStock
        } else {
            controller.notifyObservers(Event.cardRemoved)
            updatedStock
        }
    }

    override def play(stock: Stock): Boolean = {
        if (stock.getLength() < 17) {
            false
        } else {
            true
        }
    }

    override def fillStock(stock: Stock): Stock = {
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
        debug_stock
    }

    override def listCards(stock: Stock): String = {
        val all_cards = List[Card](Card.Burggraben, Card.Kapelle, Card.Keller, Card.Dorf, Card.Holzfaeller,
                        Card.Werkstatt, Card.Buerokrat, Card.Dieb, Card.Festmahl, Card.Geldverleiher, Card.Miliz,
                        Card.Schmiede, Card.Spion, Card.Thronsaal, Card.Umbau, Card.Bibliothek, Card.Hexe,
                        Card.Jahrmarkt, Card.Laboratorium, Card.Markt, Card.Mine, Card.Ratsversammlung,
                        Card.Abenteurer, Card.Garten)
        val notIncluded = all_cards.filterNot(card => stock.contains(card))
        val s1 = "Liste der noch verfuegbaren Karten:"
        val s2 = notIncluded.map(_.name).mkString(" | ")
        s"$s1\n\n$s2"
    }
}
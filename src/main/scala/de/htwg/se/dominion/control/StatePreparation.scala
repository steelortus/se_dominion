package de.htwg.se.dominion
package control

import control.State
import model.ConsoleColors.*
import model.Stock
import model.Card
import model.Player
import util.Event
import util.Observable

case class StatePreparation(controller:Controller, stock:Stock) extends State {

    override def addCard(card: String): Stock = {
        val updatedStock = stock.addCard(stock.getCard(card))
        if (updatedStock == stock) {
            if (updatedStock.getLength() == 17) {
                notifyObservers(ErrorEvent.stockFull)
                updatedStock
            } else {
                notifyObservers(ErrorEvent.couldNotAddCard)
                updatedStock
            }
        } else {
            if (updatedStock.getLength() == 17) {
                notifyObservers(Event.stockFull)
            }
            notifyObservers(Event.cardAdded)
            updatedStock
        }
    }

    override def removeCard(card: String): Stock = {
        val updatedStock = stock.removeCard(stock.getCard(card))
        if (updatedStock == stock) {
            notifyObservers(ErrorEvent.couldNotRemoveCard)
            updatedStock
        } else {
            notifyObservers(Event.cardRemoved)
            updatedStock
        }
    }

    override def play(): Unit = {
        RED("Cannot play in preparation state!")
    }

    override def fillStock(): Unit = {
        RED("Cannot fill the stock in preparation state!")
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
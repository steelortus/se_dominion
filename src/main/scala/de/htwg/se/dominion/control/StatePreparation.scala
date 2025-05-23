package de.htwg.se.dominion
package control

import control.State
import model.Stock
import model.Card
import model.Player
import util.Event
import util.ErrorEvent
import util.Observable

case class StatePreparation(stock: Stock) extends State {

    override def addCard(card: String, stock: Stock): Stock = {
        val updatedStock = stock.getCard(card) match {
            case Some(c) => stock.addCard(c)
            case None => stock
        }
        updatedStock
    }

    override def removeCard(card: String, stock: Stock): Stock = {
        val updatedStock = stock.getCard(card) match {
            case Some(c) => stock.removeCard(c)
            case None => stock
        }
        updatedStock
    }

    override def play(stock: Stock): Boolean = {
        if (stock.getLength() < 17) {
            false
        } else {
            true
        }
    }

    override def fillStock(stock: Stock): Stock = {
        val newStock = stock ++ List(Card.Garten, Card.Markt, Card.Jahrmarkt, Card.Dieb, Card.Abenteurer, Card.Dorf, Card.Hexe, Card.Laboratorium, Card.Bibliothek, Card.Holzfaeller)
        newStock
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
package de.htwg.se.dominion
package control

import control.State
import model.Stock
import model.Card
import model.Player
import model.TurnHandler
import util.Event
import util.ErrorEvent
import util.Observable
import util.Command
import util.UndoManager
import control.commands._

case class StatePreparation(stock: Stock) extends State {

    override def addCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock = {
        val updatedStock = um.doStep(stock, AddCardCommand(stock.getCard(card)))
        updatedStock
    }

    override def removeCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock = {
        val updatedStock = um.doStep(stock, RemoveCardCommand(stock.getCard(card)))
        updatedStock
    }

    override def play(stock: Stock, um: UndoManager[Stock]): Boolean = {
        if (stock.getLength() < 17) {
            false
        } else {
            true
        }
    }

    override def fillStock(stock: Stock, um: UndoManager[Stock]): Stock = {
        val updatedStock = um.doStep(stock, FillStockCommand(stock))
        updatedStock
    }

    override def listCards(stock: Stock, um: UndoManager[Stock]): String = {
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
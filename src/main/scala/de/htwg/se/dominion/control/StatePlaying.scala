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

    override def play(): Unit = {
        YELLOW("Already Playing!")
    }

    override def fillStock(): Unit = {
        RED("Cannot fill the stock in playing state!")
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
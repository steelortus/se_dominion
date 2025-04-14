package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Card
import scala.collection.immutable.List

case class Stock() {
    val setupStock = List[Card](Card.Kupfer, Card.Silber, Card.Gold, Card.Anwesen, Card.Herzogtum, Card.Provinz, Card.Fluch)
    var stock = setupStock

    def getCard(name: String): Card = {
        Card.values.find(_.toString.equalsIgnoreCase(name)).getOrElse(Card.NotACard)
    }

    def addCard(card:Card): Boolean = {
        if (stock.length < 17 && !stock.contains(card) && card != Card.NotACard) {
            stock = stock :+ card
            true
        } else {
            println("Cannot add this Card to the Stock. Maybe it's already in it?")
            false
        }
    }

    def addCard(name:String): Boolean = {
        addCard(getCard(name))
    }

    def removeCard(card:Card): Boolean = {
        if (!setupStock.contains(card) && stock.contains(card)) {
            stock = stock.filterNot(_ == card)
            true
        } else {
            println("Cannot remove this Card from the Stock. Maybe it's not in it?")
            false
        }
    }

    def removeCard(name:String): Boolean = {
        removeCard(getCard(name))
    }

    override def toString(): String = {
        stock.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }
}
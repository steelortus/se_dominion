package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Card
import scala.collection.immutable.List

case class Stock(stock: List[Card] = List(Card.Kupfer, Card.Silber, Card.Gold, Card.Anwesen, Card.Herzogtum, Card.Provinz, Card.Fluch)) {
    val setupStock = List[Card](Card.Kupfer, Card.Silber, Card.Gold, Card.Anwesen, Card.Herzogtum, Card.Provinz, Card.Fluch)

    def getCard(name: String): Card = Card.values.find(_.toString.equalsIgnoreCase(name)).getOrElse(Card.NotACard)

    def addCard(card:Card): Stock = {
        if (stock.length < 17 && !stock.contains(card) && card != Card.NotACard) {
            Stock(stock :+ card)
        } else {
            this
        }
    }

    def addCard(name:String): Stock = addCard(getCard(name))

    def removeCard(card:Card): Stock = {
        if (!setupStock.contains(card) && stock.contains(card)) {
            Stock(stock.filterNot(_ == card))
        } else {
            this
        }
    }

    def contains(card: Card): Boolean = stock.contains(card)

    def removeCard(name:String): Stock = removeCard(getCard(name))

    def getLength(): Int = stock.length

    override def toString(): String = {
        stock.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }
}
package de.htwg.se.dominion.model.stockComponent.stockComponentImplementation

import de.htwg.se.dominion.model.stockComponent.StockInterface

import scala.io.StdIn._
import de.htwg.se.dominion.model.Card
import scala.collection.immutable.List

case class Stock(override val stock: List[Card] = List(Card.Kupfer, Card.Silber, Card.Gold, Card.Anwesen, Card.Herzogtum, Card.Provinz, Card.Fluch),
                 override val stockAmount: List[Int] = List(50, 50, 50, 50, 25, 12, 50)) extends StockInterface(stock, stockAmount) {
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

    def ++(cards: List[Card]): Stock = cards.foldLeft(this)((acc, card) => acc.addCard(card))

    def removeCard(card:Card): Stock = {
        if (!setupStock.contains(card) && stock.contains(card)) {
            Stock(stock.filterNot(_ == card))
        } else {
            this
        }
    }

    def removeCard(name:String): Stock = removeCard(getCard(name))

    def contains(card: Card): Boolean = stock.contains(card)

    def contains(card: String): Boolean = stock.contains(getCard(card))

    def getLength(): Int = stock.length

    override def toString(): String = {
        stock.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }

    def toSellString(): String = {
        stock.map(card =>
            s"${card.getName} - Cost: ${card.getCost}"
        ).mkString("\n")
    }
}
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
            Stock(stock :+ card, stockAmount :+ card.amount)
        } else {
            this
        }
    }

    def addCard(name:String): Stock = addCard(getCard(name))

    def ++(cards: List[Card]): Stock = cards.foldLeft(this)((acc, card) => acc.addCard(card))

    def removeCard(card:Card): Stock = {
        if (!setupStock.contains(card) && stock.contains(card)) {
            val ind = stock.indexOf(card)
            val amounts = stockAmount.patch(ind, Nil, 1)
            val newStock = stock.patch(ind, Nil, 1)
            Stock(newStock, amounts)
        } else {
            this
        }
    }

    def removeCard(name:String): Stock = removeCard(getCard(name))

    def contains(card: Card): Boolean = stock.contains(card)

    def contains(card: String): Boolean = stock.contains(getCard(card))

    def getLength(): Int = stock.length

    def getCardAmount(card: Card): Int = {
        val index = stock.indexOf(card)
        if (index >= 0 && index < stockAmount.length) {
            stockAmount(index)
        } else {
            0
        }
    }

    def cardPurchased(card: Card): Stock = {
        if (stock.contains(card) && getCardAmount(card) > 0) {
            val cardIndex = stock.indexOf(card)
            val newAmounts = stockAmount.updated(cardIndex, getCardAmount(card) - 1)
            this.copy(stockAmount = newAmounts)
        } else {
            this
        }
    }

    def checkIfGameShouldEnd(): Boolean = {
        val emptyStacks = stockAmount.count(_ == 0)
        val provinceEmpty = stockAmount(stock.indexOf(Card.Provinz)) == 0
        if (emptyStacks >= 3 || provinceEmpty) {
            true
        } else {
            false
        }
    }

    override def toString(): String = {
        stock.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }

    def toSellString(): String = {
        stock.map(card =>
            s"${card.getName} - Cost: ${card.getCost}. Amount: ${stockAmount(stock.indexOf(card))}"
        ).mkString("\n")
    }
}
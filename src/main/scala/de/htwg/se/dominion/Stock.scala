package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Card
import scala.collection.immutable.List

case class Stock() {
    var stock = List[Card](Card.Kupfer, Card.Silber, Card.Gold, Card.Anwesen, Card.Herzogtum, Card.Provinz, Card.Fluch)

    /*def setupStock(): Unit = {
        stock += Card.Kupfer
        stock += Card.Silber
        stock += Card.Gold
        stock += Card.Anwesen
        stock += Card.Herzogtum
        stock += Card.Provinz
        stock += Card.Fluch
    }*/
    
    def printStock(): Unit = {
        println("Current stock:")
        for (card <- stock) {
            println(s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}")
        }
    }

    def addCard(card:Card): Boolean = {
        if (stock.length < 17 && !stock.contains(card)) {
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

    private def getCard(name: String): Card = {
        var selectedCard: Option[Card] = None
        var inputName = name

        while (selectedCard.isEmpty) {
            selectedCard = Card.values.find(_.toString.equalsIgnoreCase(inputName))
            if (selectedCard.isEmpty) {
                println(s"Card '$inputName' not found. Please try again:")
                inputName = readLine()
            }
        }
        selectedCard.get
    }
}
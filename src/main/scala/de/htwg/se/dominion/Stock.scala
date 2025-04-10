package de.htwg.se.dominion

import de.htwg.se.dominion.Card
import scala.collection.mutable.ListBuffer

case class Stock() {
    val stock = ListBuffer[Card]()

    def setupStock(): Unit = {
        stock += Card.Kupfer
        stock += Card.Silber
        stock += Card.Gold
        stock += Card.Anwesen
        stock += Card.Herzogtum
        stock += Card.Provinz
        stock += Card.Fluch
    }
    
    def printStock(): Unit = {
        println("Current stock:")
        for (card <- stock) {
            println(s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}")
        }
    }

    def addCard(card:Card): Boolean = {
        if (stock.length < 17 && !stock.contains(card)) {
            stock += card
            true
        } else {
            println("Cannot add this Card to the Stock. Maybe it's already in it?")
            false
        }
    }
}
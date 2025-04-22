package de.htwg.se.dominion

import scala.util.Random
import de.htwg.se.dominion.Card
import de.htwg.se.dominion.Stock

case class Player() {
    var deck = List[Card](Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Anwesen, Card.Anwesen, Card.Anwesen)
    val hand = List[Card]()
    val discard = List[Card]()

    def shuffleDeck(deck2:List[Card]): List[Card] = {
        deck = Random.shuffle(deck2)
        deck
    }

    def getDeck(): List[Card] = {
        deck
    }

    override def toString(): String = {
        deck.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }
}
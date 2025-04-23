package de.htwg.se.dominion

import scala.util.Random
import de.htwg.se.dominion.Card
import de.htwg.se.dominion.Stock

case class Player(deck: List[Card] = List(
                    Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer,
                    Card.Anwesen, Card.Anwesen, Card.Anwesen),
                  hand: List[Card] = List(),
                  discard: List[Card] = List()) {

    def shuffleDeck(): Player = {
        val shuffledDeck = Random.shuffle(deck)
        this.copy(deck = shuffledDeck)
    }

    def refillDeck(): Player = {
        if (deck.isEmpty) {
            val shuffledDeck = Random.shuffle(discard)
            this.copy(deck = shuffledDeck, discard = List[Card]())
        } else {
            this
        }
    }

    def drawCardFromDeck(): Player = {
        if (deck.isEmpty) {
            val refilledPlayer = refillDeck()
            refilledPlayer.drawCardFromDeck()
        } else {
            val drawnCard = deck.head
            val newDeck = deck.drop(1)
            val newHand = hand :+ drawnCard
            this.copy(deck = newDeck, hand = newHand)
        }
    }

    def discardFromHand(index:Int): Player = {
        if (index >= 1 && index <= hand.length) {
            val discardedCard = hand(index)
            val newHand = hand.drop(index)
            val newDiscard = discard :+ discardedCard
            this.copy(hand = newHand, discard = newDiscard)
        } else {
            this
        }
    }

    def discardFromDeck(index:Int): Player = {
        if (index >= 1 && index <= deck.length) {
            val discardedCard = deck(index)
            val newDeck = deck.drop(index)
            val newDiscard = discard :+ discardedCard
            this.copy(deck = newDeck, discard = newDiscard)
        } else {
            this
        }
    }

    def getDeck(): List[Card] = deck
    def getHand(): List[Card] = hand
    def getDiscard(): List[Card] = discard

    override def toString(): String = {
        deck.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }

    def handToString(): String = {
        hand.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }
}
package de.htwg.se.dominion.model

import scala.util.Random
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.Stock

case class Player(deck: List[Card] = List(
                    Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer, Card.Kupfer,
                    Card.Anwesen, Card.Anwesen, Card.Anwesen),
                  hand: List[Card] = List(),
                  discard: List[Card] = List(),
                  purchases: Int = 1,
                  actions: Int = 1) {

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

    def drawFiveCardsFromDeck(): Player = {
        val newPlayer = (1 to 5).foldLeft(this) { (player, _) =>
            player.drawCardFromDeck()
        }
        newPlayer
    }

    def discardFromHand(index:Int): Player = {
        if (index >= 0 && index < hand.length) {
            val discardedCard = hand(index)
            val newHand = hand.take(index) ++ hand.drop(index + 1)
            val newDiscard = discard :+ discardedCard
            this.copy(hand = newHand, discard = newDiscard)
        } else {
            this
        }
    }

    def discardAllFromHand(): Player = {
        val newDiscard = discard ++ hand
        this.copy(hand = List[Card](), discard = newDiscard)
    }

    def discardFromDeck(index:Int): Player = {
        if (index >= 0 && index < deck.length) {
            val discardedCard = deck(index)
            val newDeck = deck.take(index) ++ deck.drop(index + 1)
            val newDiscard = discard :+ discardedCard
            this.copy(deck = newDeck, discard = newDiscard)
        } else {
            this
        }
    }

    def getMoneyInHand(): Int = {
        hand.map(_.getValue).sum
    }

    def getPoints(): Int = {
        hand.map(_.getPoints).sum + discard.map(_.getPoints).sum + deck.map(_.getPoints).sum
    }

    def purchaseCard(card: Card, stock: Stock): Player = {
        if (stock.contains(card)) {
            if (getMoneyInHand() >= card.getCost && purchases > 0) {
                this.copy(discard = discard :+ card, purchases = purchases - 1)
            } else {
                this
            }
        } else {
            this
        }
    }

    def purchaseCard(card: String, stock: Stock): Player = {
        purchaseCard(stock.getCard(card), stock)
    }

    // Never call this method directly, only through the UndoManager, otherwise this will break the game logic.
    def returnCard(card: Card): Player = {
        if (discard.contains(card)) {
            val newDiscard = discard.dropRight(1)
            this.copy(discard = newDiscard, purchases = purchases + 1)
        } else {
            this
        }
    }

    def nextTurn(): Player = {
        val newPlayer = this.discardAllFromHand()
        val redrawnPlayer = newPlayer.drawFiveCardsFromDeck()
        redrawnPlayer.copy(actions = 1, purchases = 1)
    }

    def addCardToHand(card: Card): Player = {
        this.copy(hand = hand :+ card)
    }

    def addCardToDeck(card: Card): Player = {
        this.copy(deck = deck :+ card)
    }

    override def toString(): String = {
        "Deck:\n" + deckToString() + "\nHand:\n" + handToString() + "\nDiscard:\n" + discardToString()
    }
    
    def deckToString(): String = {
        deck.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }

    def handToString(): String = {
        hand.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }

    def discardToString(): String = {
        discard.map(card =>
            s"${card.getName} - Cost: ${card.getCost}, Value: ${card.getValue}, Points: ${card.getPoints}, Amount: ${card.getAmount}"
        ).mkString("\n")
    }
}
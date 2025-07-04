package de.htwg.se.dominion.model.playerComponent

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.StockInterface

trait PlayerInterface(val deck: List[Card] = List(),
                      val hand: List[Card] = List(),
                      val discard: List[Card] = List(),
                      val purchases: Int,
                      val actions: Int) {
    def shuffleDeck(): PlayerInterface
    def refillDeck(): PlayerInterface
    def drawCardFromDeck(): PlayerInterface
    def drawFiveCardsFromDeck(): PlayerInterface
    def discardFromHand(index: Int): PlayerInterface
    def discardFromHand(card: Card): PlayerInterface
    def discardAllFromHand(): PlayerInterface
    def discardFromDeck(index: Int): PlayerInterface
    def getMoneyInHand(): Int
    def getPoints(): Int
    def purchaseCard(card: Card, stock: StockInterface): PlayerInterface
    def purchaseCard(card: String, stock: StockInterface): PlayerInterface
    def cardPlayed(): PlayerInterface
    def returnCard(card: Card): PlayerInterface
    def nextTurn(): PlayerInterface
    def addCardToHand(card: Card): PlayerInterface
    def addCardToDeck(card: Card): PlayerInterface
    def addPurchase(): PlayerInterface
    def addAction(): PlayerInterface
    def deckToString(): String
    def handToString(): String
    def discardToString(): String
}
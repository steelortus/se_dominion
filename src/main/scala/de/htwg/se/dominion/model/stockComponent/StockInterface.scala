package de.htwg.se.dominion.model.stockComponent

import de.htwg.se.dominion.model.Card

trait StockInterface(val stock: List[Card], val stockAmount: List[Int]) {
    def getCard(name: String): Card
    def addCard(card: Card): StockInterface
    def addCard(name: String): StockInterface
    def ++(cards: List[Card]): StockInterface
    def removeCard(card: Card): StockInterface
    def removeCard(name: String): StockInterface
    def contains(card: Card): Boolean
    def contains(card: String): Boolean
    def getLength(): Int
    def getCardAmount(card: Card): Int
    def cardPurchased(card: Card): StockInterface
    def checkIfGameShouldEnd(): Boolean
    def toSellString(): String
}
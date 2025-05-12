package de.htwg.se.dominion
package control

import model.Stock

trait State {
    def addCard(card: String, stock: Stock): Stock
    def removeCard(card: String, stock: Stock): Stock
    def play(stock: Stock): Boolean
    def fillStock(stock: Stock): Stock
    def listCards(stock: Stock): String
}
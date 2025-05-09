package de.htwg.se.dominion
package control

import model.Stock

trait State {
    def addCard(card: String): Stock
    def removeCard(card: String): String
    def play(): Unit
    def fillStock(): Unit
    def getStock(): Stock
    def listCards(): Unit
}
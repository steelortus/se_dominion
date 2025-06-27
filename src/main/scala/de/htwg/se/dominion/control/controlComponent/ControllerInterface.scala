package de.htwg.se.dominion.control.controlComponent

import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.util.Observable
import de.htwg.se.dominion.util.Event

trait ControllerInterface extends Observable {

  def getStock(): StockInterface
  def addCard(card: String): Unit
  def removeCard(card: String): Unit
  def play(): Unit
  def fillStock(): Unit
  def listCards(): String
  def createPlayers(noP: Int): Unit
  def nextTurn(): Unit
  def getTurn(): Int
  def getPlayerHand(): String
  def getPlayerMoney(): Int
  def getPlayerActions(): Int
  def getPlayerPurchases(): Int
  def purchase(card: Card): Unit
  def purchase(card: String): Unit
  def endGame(): Unit
  def getAllPoints(): List[Int]
  def showHelp(): Unit
  def updateState(e: Event): Unit
  def undo(): Unit
  def redo(): Unit
  def save(): Unit
  def load(): Unit
}
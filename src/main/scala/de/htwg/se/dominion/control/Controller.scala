package de.htwg.se.dominion
package control

import util.Observable
import model.Stock
import model.Card
import model.Player

case class Controller(stock: Stock) extends Observable:
  def addCard(card: Card): Unit = {
    stock.addCard(card)
    //notifyObservers(Event(stock))
  }

  def removeCard(card: Card): Unit = {
    stock.removeCard(card)
    //notifyObservers(Event(stock))
  }

  def getStock(): Stock = stock
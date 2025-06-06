package de.htwg.se.dominion
package control.stateComponent.statePlayingImplementation

import control.stateComponent.State
import model.ConsoleColors.*
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import model.Card
import model.playerComponent.PlayerInterface
import model.turnHandlerComponent.TurnHandlerInterface
import util.Event
import util.ErrorEvent
import util.Observable
import util.Command
import util.UndoManager
import control.commands._

case class StatePlaying(stock: Stock) extends State {
    override def addCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock = {
        stock
    }

    override def removeCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock = {
        stock
    }

    override def play(stock: Stock, th: TurnHandlerInterface): Boolean = {
        println(CYAN(s"TODO\n"))
        false
    }

    override def fillStock(stock: Stock, um: UndoManager[Stock]): Stock = {
        stock
    }

    override def listCards(stock: Stock): String = {
        stock.toString()
    }

    override def purchase(stock: Stock, card: Card, th: TurnHandlerInterface, um: UndoManager[TurnHandlerInterface]): TurnHandlerInterface = {
        val updatedTurnHandler = um.doStep(th, PurchaseCommand(card, stock))
        updatedTurnHandler
    }
}
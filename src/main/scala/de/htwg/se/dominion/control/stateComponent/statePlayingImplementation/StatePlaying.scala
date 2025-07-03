package de.htwg.se.dominion
package control.stateComponent.statePlayingImplementation

import control.stateComponent.State
import model.ConsoleColors.*
import de.htwg.se.dominion.model.stockComponent.StockInterface
import model.Card
import model.playerComponent.PlayerInterface
import model.turnHandlerComponent.TurnHandlerInterface
import util.Event
import util.ErrorEvent
import util.Observable
import util.Command
import util.UndoManager
import control.commands._
import fileio.FileIOInterface

case class StatePlaying(stock: StockInterface) extends State {
    override def addCard(card: String, stock: StockInterface, um: UndoManager[StockInterface]): StockInterface = {
        stock
    }

    override def removeCard(card: String, stock: StockInterface, um: UndoManager[StockInterface]): StockInterface = {
        stock
    }

    override def play(stock: StockInterface, th: TurnHandlerInterface): Boolean = {
        false
    }

    override def fillStock(stock: StockInterface, um: UndoManager[StockInterface]): StockInterface = {
        stock
    }

    override def listCards(stock: StockInterface): String = {
        stock.toString()
    }

    override def purchase(stock: StockInterface, card: Card, th: TurnHandlerInterface, um: UndoManager[TurnHandlerInterface]): TurnHandlerInterface = {
        val updatedTurnHandler = um.doStep(th, PurchaseCommand(card, stock))
        updatedTurnHandler
    }

    override def load(fileIO: FileIOInterface): (StockInterface, TurnHandlerInterface) = {
        fileIO.load()
    }

    override def save(fileIO: FileIOInterface, stock: StockInterface, th: TurnHandlerInterface): Unit = {
        fileIO.save(stock, th)
    }
}
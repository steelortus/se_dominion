package de.htwg.se.dominion
package control

import util.Observable
import util.Event
import util.ErrorEvent
import model.Stock
import model.Card
import model.Player
import model.ConsoleColors.*
import model.TurnHandler
import model.TurnHandlerBuilder
import commands.*

case class Controller(var stock: Stock, var state: State, var th: TurnHandler) extends Observable {

    private val commandManager = new CommandManager()

    def getStock(): Stock = {
        stock
    }

    def play(): Unit = {
        if (state.play(stock) == true) {
            notifyObservers(Event.selectNumberOfPlayers)
        } else {
            notifyObservers(ErrorEvent.cantStart)
        }
    }

    def fillStock(): Unit = {
        val newStock = state.fillStock(stock)
        if (stock == newStock) {
            notifyObservers(ErrorEvent.invalidCommand)
        } else {
            stock = newStock
            updateState(Event.stockFull)
        }
    }

    def listCards(): String = {
        state.listCards(stock)
    }

    def createPlayers(noP: Int): Unit = {
        if (noP < 2 || noP > 4) {
            notifyObservers(ErrorEvent.invalidNumberOfPlayers)
            notifyObservers(Event.selectNumberOfPlayers)
        } else {
            val builder = new TurnHandlerBuilder()
            val finishedBuilder = builder.setNumberOfPlayers(builder, noP).setTurn(builder, 0)
            th = finishedBuilder.getResult()
            updateState(Event.playing)
        }
    }

    def nextTurn(): Unit = {
        th = th.nextTurn()
    }

    def purchase(s:String): Unit = {
        println(th)
    }

    def updateState(e: Event): Unit = {
        e match {
            case Event.preparation =>
                state = StatePreparation(stock)
                notifyObservers(Event.preparation)
            case Event.stockFull =>
                state = StatePreparation(stock)
                notifyObservers(Event.stockFull)
            case Event.playing =>
                state = StatePlaying(stock)
                notifyObservers(Event.playing)
            case _ =>
                notifyObservers(ErrorEvent.invalidState)
        }
    }

    def executeCommand(cmd: Command): Unit = {
        commandManager.executeCommand(cmd)
    }

    def undo(): Unit = {
        commandManager.undo()
    }

    def redo(): Unit = {
        commandManager.redo()
    }
}
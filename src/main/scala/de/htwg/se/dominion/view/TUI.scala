package de.htwg.se.dominion
package view

import scala.io.StdIn._
import model.Stock
import model.Player
import model.Card
import control.Controller
import control.commands.*
import util.Observer
import util.Event
import util.ErrorEvent
import model.ConsoleColors.*
import scala.util.Try
import scala.util.Success
import scala.util.Failure

case class TUI(controller: Controller) extends Observer {
    controller.add(this)
    def run(): Unit = {
        println(BLUE("> Enter your command: (h for help)"))
        print("> ")
        val input = readLine().toLowerCase()

        if (input.equalsIgnoreCase("exit")) {
            println(YELLOW("Exiting the program."))
        } else {
            input match {
                case "add" =>
                    add()
                case "remove" =>
                    remove()
                case "play" =>
                    play()
                case "fill" =>
                    fill()
                case "show" =>
                    show()
                case "list" =>
                    list()
                case "purchase" =>
                    purchase()
                case "undo" =>
                    controller.undo()
                case "redo" =>
                    controller.redo()
                case "h" =>
                    println(YELLOW(s"""COMMANDS:
                            |add\t-   Type in cards to add to your stock
                            |remove\t-   Type in cards to remove from current stock
                            |play\t-   Starts the match (17 cards in stock needed!)
                            |fill\t-   Fills your stock with the default cards
                            |show\t-   Displays the current stock
                            |list\t-   Shows a list of all cards you can still add
                            |exit\t-   Exit this program""".stripMargin))
                case _ =>
                    println(RED("Unknown command. Please try again."))
            }
            run()
        }
    }

    override def update(e: Event): Unit = {
        e match {
            case Event.preparation =>
                println(YELLOW("You are in preparation state. Please add cards to the stock."))
            case Event.stockFull =>
                println(YELLOW("Stock is full! You can start the game now!"))
            case Event.cardAdded =>
                println(GREEN("Card added successfully!"))
            case Event.cardRemoved =>
                println(GREEN("Card removed successfully!"))
            case Event.playing =>
                println(YELLOW("Game started!"))
            case Event.selectNumberOfPlayers =>
                println(CYAN("Please select the number of players (2-4):"))
                print("> ")
                val input = readLine()
                Try(input.toInt) match {
                    case Success(n) => controller.createPlayers(n)
                    case Failure(_) => println(RED("Please type in a number [2, 3, 4]"))
                }
        }
    }

    override def update(e: ErrorEvent): Unit = {
        e match {
            case ErrorEvent.stockFull =>
                println(RED("Cannot add this Card to the Stock, as the Stock is already full."))
            case ErrorEvent.couldNotAddCard =>
                println(RED("Cannot add this Card to the Stock. Maybe it's already in it?"))
            case ErrorEvent.couldNotRemoveCard =>
                println(RED("Cannot remove this Card from the Stock. Maybe it's not in it?"))
            case ErrorEvent.cantStart =>
                println(RED("Cannot start the game. Please make sure the Stock is full and try again."))
            case ErrorEvent.invalidCommand =>
                println(RED("Invalid Command. Please try again."))
            case ErrorEvent.invalidNumberOfPlayers =>
                println(RED("Invalid number of players. Please select a number between 2 and 4."))
            case ErrorEvent.invalidState =>
                println(YELLOW("[Warning] The State has been attemped to be changed, but it is not possible with the called Event."))
        }
    }

    def add(): Unit = {
        println(CYAN("Please type out a Card you want to add to the Stock:"))
        print("> ")
        val card = readLine()
        controller.executeCommand(AddCardCommand(controller, card))
    }

    def remove(): Unit = {
        println(CYAN("Please type out a Card you want to remove from the Stock:"))
        print("> ")
        val card = readLine()
        controller.executeCommand(RemoveCardCommand(controller, card))
    }

    def play(): Unit = {
        controller.play()    
    }

    def show(): Unit = {
        println(YELLOW("Current Stock:"))
        println(CYAN(controller.getStock().toString()))
    }

    def fill(): Unit = {
        controller.executeCommand(FillStockCommand(controller))
        println(GREEN("Stock filled successfully!"))
    }

    def list(): Unit = {
        println(CYAN(controller.listCards()))
    }

    def purchase(): Unit = {
        println(CYAN("What Card would you like to purchase?"))
        print("> ")
        val card = readLine()
        controller.executeCommand(PurchaseCardCommand(controller, card))
    }
}


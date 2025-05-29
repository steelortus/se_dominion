package de.htwg.se.dominion
package view

import scala.io.StdIn._
import model.Stock
import model.Player
import model.Card
import control.Controller
import util.Observer
import util.Event
import util.ErrorEvent
import model.ConsoleColors.*

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
                //case addCmd if add.matches("add [a-zA-Z]+") =>
                    //add(input)
                case "remove" =>
                    remove()
                //case removeCmd if remove.matches("remove [a-zA-Z]+") =>
                    //remove(input)
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
                case "end" =>
                    end()
                case "h" =>
                    controller.showHelp()
                case "undo" =>
                    controller.undo()
                    println(GREEN("> Undone!"))
                case "redo" =>
                    controller.redo()
                    println(GREEN("> Redone!"))
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
                println(YELLOW(s"\n----- Turn ${controller.getTurn()} -----\n"))
                println(CYAN(s"Current Hand:\n${controller.getPlayerHand()}\n"))
                println(GREEN(s"Current Money in Hand: ${controller.getPlayerMoney()}"))
                println(GREEN(s"Current Actions: ${controller.getPlayerActions()}"))
                println(GREEN(s"Current Purchases: ${controller.getPlayerPurchases()}\n"))
            case Event.selectNumberOfPlayers =>
                println(CYAN("Please select the number of players (2-4):"))
                print("> ")
                val input = readLine()
                controller.createPlayers(input.toInt)
            case Event.outOfActions =>
                println(YELLOW("You are out of actions for this turn. You can either continue purchasing or end your turn.\n"))
            case Event.outOfPurchases =>
                println(YELLOW("You are out of purchases for this turn.\n"))
            case Event.commandsPreparation =>
                println(YELLOW(s"""COMMANDS:
                            |h\t-   Show this help message
                            |add\t-   Type in cards to add to your stock
                            |remove\t-   Type in cards to remove from current stock
                            |play\t-   Starts the match (17 cards in stock needed!)
                            |fill\t-   Fills your stock with the default cards
                            |show\t-   Displays the current stock
                            |list\t-   Shows a list of all cards you can still add
                            |undo\t-   Undo the last action
                            |redo\t-   Redo the last action
                            |exit\t-   Exit this program""".stripMargin))
            case Event.commandsPlaying =>
                println(YELLOW(s"""COMMANDS:
                            |h\t-   Show this help message
                            |purchase\t-   Purchase a card from the stock
                            |end\t-   End your turn
                            |undo\t-   Undo the last action
                            |redo\t-   Redo the last action
                            |exit\t-   Exit this program""".stripMargin))
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
        controller.addCard(readLine())
    }

    def add(input: String): Unit = {
        val cardName = input.stripPrefix("add ").trim
        controller.addCard(cardName)
    }

    def remove(): Unit = {
        println(CYAN("Please type out a Card you want to remove from the Stock:"))
        print("> ")
        controller.removeCard(readLine())
    }

    def remove(input: String): Unit = {
        val cardName = input.stripPrefix("remove ").trim
        controller.removeCard(cardName)
    }

    def play(): Unit = {
        controller.play()    
    }

    def show(): Unit = {
        println(YELLOW("Current Stock:"))
        println(CYAN(controller.getStock().toString()))
    }

    def fill(): Unit = {
        controller.fillStock()
        println(GREEN("Stock filled successfully!"))
    }

    def list(): Unit = {
        println(CYAN(controller.listCards()))
    }

    def purchase(): Unit = {
        println(CYAN(controller.getStock().toSellString()))
        println(YELLOW("\nWhat Card would you like to purchase?"))
        print("> ")
        controller.purchase(readLine())
    }

    def end(): Unit = {
        controller.nextTurn()
    }
}


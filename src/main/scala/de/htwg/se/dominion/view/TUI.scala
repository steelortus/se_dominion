package de.htwg.se.dominion
package view

import scala.io.StdIn._
import control.controlComponent.ControllerInterface
import util.Observer
import util.Event
import util.ErrorEvent
import model.ConsoleColors.*
import scala.util.{Try, Success, Failure}
import scala.annotation.tailrec

import de.htwg.se.dominion.modules.DefaultSettings.{controller}

class TUI(using controller: ControllerInterface) extends Observer {
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
                //temporary command to end the game prematurely
                case "e" =>
                    endGame()
                case "undo" =>
                    controller.undo()
                case "redo" =>
                    controller.redo()
                case "save" =>
                    controller.save()
                case "load" =>
                    controller.load()
                case _ =>
                    println(RED("Unknown command. Please try again."))
            }
            run()
        }
    }

    override def update(e: Event): Unit = {
        println(s"[TUI] Received event: $e")
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
                @annotation.tailrec
                def requestNumberOfPlayers(): Unit = {
                    println(CYAN("Please select the number of players (2-4):"))
                    print("> ")
                    val input = readLine()
                    Try(input.toInt) match {
                        case Success(n) if n >= 2 && n <= 4 =>
                            controller.createPlayers(n)
                        case Success(_) =>
                            println(RED("Number must be between 2 and 4."))
                            requestNumberOfPlayers()
                        case Failure(_) =>
                            println(RED("Please type in a number [2, 3, 4]"))
                            requestNumberOfPlayers()
                    }
                }
                requestNumberOfPlayers()
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
            case Event.endGame =>
                println(YELLOW("Game over! Here are the final results:"))
                val pointsList = controller.getAllPoints()
                val resultString = pointsList.zipWithIndex
                    .map { case (points, idx) => s"Player ${idx + 1}: $points Points" }
                    .mkString("\n")
                println(PURPLE(resultString))
                System.exit(0)
            case Event.undoPrep =>
                println(GREEN("> Undone!"))
            case Event.redoPrep =>
                println(GREEN("> Redone!"))
            case Event.undoPlay =>
                println(GREEN("> Undone!"))
            case Event.redoPlay =>
                println(GREEN("> Redone!"))
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
            case ErrorEvent.couldNotPurchaseCard =>
                println(RED("Could not purchase the Card."))
            case ErrorEvent.outOfActions =>
                println(RED("You are out of actions for this turn. You can either continue purchasing or end your turn.\n"))
            case ErrorEvent.outOfPurchases =>
                println(RED("You are out of purchases for this turn.\n"))
            case ErrorEvent.couldNotPurchaseCard =>
                println(RED("Could not purchase the Card.\n"))
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

    def endGame(): Unit = {
        controller.endGame()
    }
}


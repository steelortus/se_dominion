package de.htwg.se.dominion
package view

import scala.io.StdIn._
import model.Stock
import model.Player
import model.Card
import control.Controller
import util.Observer
import util.Event
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
            case Event.playing =>
                println(YELLOW("Game started!"))
        }
    }

    def add(): Unit = {
        println(CYAN("Please type out a Card you want to add to the Stock:"))
        print("> ")
        println(controller.addCard(readLine()))
    }

    def remove(): Unit = {
        println(CYAN("Please type out a Card you want to remove from the Stock:"))
        print("> ")
        println(controller.removeCard(readLine()))
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
        println(controller.listCards())
    }
}


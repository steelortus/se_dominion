package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Stock
import de.htwg.se.dominion.ConsoleColors._

case class TUI() {
    def run(stock: Stock): Unit = {
        println(s"${BLUE}> Enter your command:${CLEARCOLOR}")
        print("> ")
        val input = readLine().toLowerCase()

        if (input.equalsIgnoreCase("exit")) {
            println(s"${YELLOW}Exiting the program.${CLEARCOLOR}")
        } else {
            val updatedStock = input match {
                case "add" =>
                    add(stock)
                case "remove" =>
                    remove(stock)
                case "play" =>
                    play(stock)
                    stock
                case "fill" =>
                    fill(stock)
                case "show" =>
                    println(s"${PURPLE}${stock.toString()}${CLEARCOLOR}")
                    println(s"${PURPLE}\nAmount of Cards: ${stock.getLength()}${CLEARCOLOR}")
                    stock
                case _ =>
                    println(s"${RED}Unknown command. Please try again.${CLEARCOLOR}")
                    stock
            }
            run(updatedStock)
        }
    }

    def add(stock:Stock): Stock = {
        println(s"${CYAN}Please type out a Card you want to add to the Stock:${CLEARCOLOR}")
        print("> ")
        val cardName = readLine()
        val updatedStock = stock.addCard(stock.getCard(cardName))
        if (updatedStock == stock) {
            if (updatedStock.getLength() == 17) {
                println(s"${RED}Cannot add this Card to the Stock. The Stock is already full!${CLEARCOLOR}")
            } else {
                println(s"${RED}Cannot add this Card to the Stock. Maybe it's already in it?${CLEARCOLOR}")
            }
            updatedStock
        } else {
            println(s"${GREEN}Card added successfully!${CLEARCOLOR}")
            updatedStock
        }
    }

    def remove(stock:Stock): Stock = {
        println(s"${CYAN}Please type out a Card you want to remove from the Stock:${CLEARCOLOR}")
        print("> ")
        val cardName = readLine()
        val updatedStock = stock.removeCard(stock.getCard(cardName))
        if (updatedStock == stock) {
            println(s"${RED}Cannot remove this Card from the Stock. Maybe it's not in it?${CLEARCOLOR}")
            updatedStock
        } else {
            println(s"${GREEN}Card removed successfully!${CLEARCOLOR}")
            updatedStock
        }
    }

    def play(stock:Stock): Unit = {
        if (stock.getLength() < 17) {
            println(s"${RED}There is not enough cards in the Stock! ${stock.getLength()} out of required 17${CLEARCOLOR}")
        } else {
            println(s"${YELLOW}Playing the game... (not implemented yet)${CLEARCOLOR}")
            var p1 = new Player()
            println(s"${YELLOW}Player 1 Deck:\n${p1.toString()}${CLEARCOLOR}\n")
            p1 = p1.shuffleDeck()
            println(s"${GREEN}Player 1 shuffled Deck:\n${p1.toString()}${CLEARCOLOR}\n")
            p1 = p1.drawCardFromDeck()
            println(s"${PURPLE}Player 1 Hand:\n${p1.handToString()}${CLEARCOLOR}\n")
        }
    }

    def fill(stock:Stock): Stock = {
        var debug_stock = stock
        debug_stock = debug_stock.addCard(Card.Garten)
        debug_stock = debug_stock.addCard(Card.Markt)
        debug_stock = debug_stock.addCard(Card.Jahrmarkt)
        debug_stock = debug_stock.addCard(Card.Dieb)
        debug_stock = debug_stock.addCard(Card.Abenteurer)
        debug_stock = debug_stock.addCard(Card.Dorf)
        debug_stock = debug_stock.addCard(Card.Hexe)
        debug_stock = debug_stock.addCard(Card.Laboratorium)
        debug_stock = debug_stock.addCard(Card.Bibliothek)
        debug_stock = debug_stock.addCard(Card.Holzfaeller)
        println(s"${GREEN}Stock filled successfully!${CLEARCOLOR}")
        debug_stock
    }
}

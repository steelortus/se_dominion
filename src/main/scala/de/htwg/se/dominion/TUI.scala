package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Stock

case class TUI() {
    def run(stock: Stock): Unit = {
        println("> Enter your command:")
        print("> ")
        val input = readLine().toLowerCase()

        if (input.equalsIgnoreCase("exit")) {
            println("Exiting the program.")
        } else {
            val updatedStock = input match {
                case "add" =>
                    add(stock)
                case "remove" =>
                    remove(stock)
                case "play" =>
                    play(stock)
                case "show" =>
                    println(stock.toString())
                    stock // Return the unchanged stock
                case _ =>
                    println("Unknown command. Please try again.")
                    stock // Return the unchanged stock
            }
            run(updatedStock) // Recursively call `run` with the updated stock
        }
    }

    def add(stock:Stock): Stock = {
        println("Please type out a Card you want to add to the Stock:")
        print("> ")
        val cardName = readLine()
        val updatedStock = stock.addCard(stock.getCard(cardName))
        println(updatedStock.toString())
        updatedStock
    }

    def remove(stock:Stock): Stock = {
        println("Please type out a Card you want to remove from the Stock:")
        print("> ")
        val cardName = readLine()
        val updatedStock = stock.removeCard(stock.getCard(cardName))
        println(updatedStock.toString())
        updatedStock
    }

    def play(stock:Stock): Stock = {
        if (stock.getLength() < 17) {
            println(s"There is not enough cards in the Stock! ${stock.getLength()} out of required 17")
            stock // Return the unchanged stock
        } else {
            println("Playing the game... (not implemented yet)")
            println(s"There are ${stock.getLength()} cards in the Stock.")
            stock // Return the unchanged stock

        }
    }
}

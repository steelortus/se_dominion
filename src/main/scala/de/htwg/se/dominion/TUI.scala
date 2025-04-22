package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Stock

case class TUI() {
    var s = new Stock()

    def run(): Unit = {
        println("> Enter your command:")
        print("> ")
        var input = readLine()
        while (input.equalsIgnoreCase("exit") == false) {
            input = input.toLowerCase()
            input match {
                case "add" =>
                    add()
                case "remove" =>
                    remove()
                case "show" =>
                    println(s.toString())
                case _ =>
                    println("Unknown command. Please try again.")
            }
            println("> Enter your command:")
            print("> ")
            input = readLine()
        }
        println("Exiting the program.")
    }

    def add(): Unit = {
        println("Please type out a Card you want to add to the Stock:")
        print("> ")
        s = s.addCard(readLine())
    }

    def remove(): Unit = {
        println("Please type out a Card you want to remove from the Stock:")
        print("> ")
        s = s.removeCard(readLine())
    }
}

package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Stock

case class TUI() {
    var s = new Stock()

    def run(): Unit = {
        println("> Enter your command:")
        print("> ")
        var input = readLine()
        while (input != "exit") {
            input match {
                case "add" =>
                    println("Please type out a Card you want to add to the Stock:")
                    s = s.addCard(readLine())
                case "remove" =>
                    println("Please type out a Card you want to remove from the Stock:")
                    s = s.removeCard(readLine())
                case "show" =>
                    println(s.toString())
                case _ =>
                    println("Unknown command. Please try again.")
            }
            print("> ")
            input = readLine()
        }
        println("Exiting the program.")
    }
}

package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Stock
import de.htwg.se.dominion.ConsoleColors.*

case class TUI() {
    def run(stock: Stock): Unit = {
        println(BLUE("> Enter your command: (h for help)"))
        print("> ")
        val input = readLine().toLowerCase()

        if (input.equalsIgnoreCase("exit")) {
            println(YELLOW("Exiting the program."))
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
                    println(PURPLE(s"${stock.toString()}"))
                    println(PURPLE(s"\nAmount of Cards: ${stock.getLength()}"))
                    stock
                case "list" =>
                    list(stock)
                    stock
                case "h" =>
                    println(YELLOW(s"""COMMANDS:
                            |add\t-   Type in cards to add to your stock
                            |remove\t-   Type in cards to remove from current stock
                            |play\t-   Starts the match (17 cards in stock needed!)
                            |fill\t-   Fills your stock with the default cards
                            |show\t-   Displays the current stock
                            |list\t-   Shows a list of all cards you can still add
                            |exit\t-   Exit this program""".stripMargin))
                    stock
                case _ =>
                    println(RED("Unknown command. Please try again."))
                    stock
            }
            run(updatedStock)
        }
    }

    def add(stock:Stock): Stock = {
        println(CYAN("Please type out a Card you want to add to the Stock:"))
        print("> ")
        val cardName = readLine()
        val updatedStock = stock.addCard(stock.getCard(cardName))
        if (updatedStock == stock) {
            if (updatedStock.getLength() == 17) {
                println(RED("Cannot add this Card to the Stock. The Stock is already full!"))
            } else {
                println(RED("Cannot add this Card to the Stock. Maybe it's already in it?"))
            }
            updatedStock
        } else {
            println(GREEN("Card added successfully!"))
            updatedStock
        }
    }

    def remove(stock:Stock): Stock = {
        println(CYAN("Please type out a Card you want to remove from the Stock:"))
        print("> ")
        val cardName = readLine()
        val updatedStock = stock.removeCard(stock.getCard(cardName))
        if (updatedStock == stock) {
            println(RED("Cannot remove this Card from the Stock. Maybe it's not in it?"))
            updatedStock
        } else {
            println(GREEN("Card removed successfully!"))
            updatedStock
        }
    }

    def play(stock:Stock): Unit = {
        if (stock.getLength() < 17) {
            println(RED(s"There is not enough cards in the Stock! ${stock.getLength()} out of required 17."))
        } else {
            println(YELLOW("Playing the game... (not implemented yet)"))
            var p1 = new Player()
            println(YELLOW(s"Player 1 Deck:\n${p1.deckToString()}\n"))
            p1 = p1.shuffleDeck()
            println(GREEN(s"Player 1 shuffled Deck:\n${p1.deckToString()}\n"))
            p1 = p1.drawCardFromDeck()
            println(YELLOW(s"Player 1 Deck after drawing:\n${p1.deckToString()}\n"))
            println(PURPLE(s"Player 1 Hand:\n${p1.handToString()}\n"))
            p1 = p1.drawCardFromDeck()
            p1 = p1.drawCardFromDeck()
            println(PURPLE(s"Player 1 Hand after drawing 2 more from Hand:\n${p1.handToString()}\n"))
            p1 = p1.discardFromHand(1)
            println(YELLOW(s"Player 1 Deck after discarding from Hand:\n${p1.deckToString()}\n"))
            println(PURPLE(s"Player 1 Hand after discarding from Hand:\n${p1.handToString()}\n"))
            println(CYAN(s"Player 1 Discard after discarding from Hand:\n${p1.discardToString()}\n"))
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
        println(GREEN("Stock filled successfully!"))
        debug_stock
    }

    def list(stock:Stock): Stock = {
        val all_cards = List[Card](Card.Burggraben, Card.Kapelle, Card.Keller, Card.Dorf, Card.Holzfaeller,
                        Card.Werkstatt, Card.Buerokrat, Card.Dieb, Card.Festmahl, Card.Geldverleiher, Card.Miliz,
                        Card.Schmiede, Card.Spion, Card.Thronsaal, Card.Umbau, Card.Bibliothek, Card.Hexe,
                        Card.Jahrmarkt, Card.Laboratorium, Card.Markt, Card.Mine, Card.Ratsversammlung,
                        Card.Abenteurer, Card.Garten)
        val notIncluded = all_cards.filterNot(card => stock.contains(card))
        println(YELLOW("Liste der noch verfuegbaren Karten:"))
        println(CYAN(notIncluded.map(_.name).mkString(" | ")))
        stock
    }
}

package de.htwg.se.dominion

import scala.io.StdIn._
import de.htwg.se.dominion.Stock
import de.htwg.se.dominion.ConsoleColors._

case class TUI() {
    def run(stock: Stock): Unit = {
        println(s"${BLUE}> Enter your command:${CLEARCOLOR} (h for help)")
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
                case "list" =>
                    list(stock)
                    stock
                case "h" =>
                    println(s"""COMMANDS:
                            |${YELLOW}add${CLEARCOLOR}\t-   Type in cards to add to your stock
                            |${YELLOW}remove${CLEARCOLOR}\t-   Type in cards to remove from current stock
                            |${YELLOW}play${CLEARCOLOR}\t-   Starts the match (17 cards in stock needed!)
                            |${YELLOW}fill${CLEARCOLOR}\t-   Fills your stock with the default cards
                            |${YELLOW}show${CLEARCOLOR}\t-   Displays the current stock
                            |${YELLOW}list${CLEARCOLOR}\t-   Shows a list of all cards you can still add
                            |${YELLOW}exit${CLEARCOLOR}\t-   Exit this program""".stripMargin)
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
            println(s"${YELLOW}Player 1 Deck:\n${p1.deckToString()}${CLEARCOLOR}\n")
            p1 = p1.shuffleDeck()
            println(s"${GREEN}Player 1 shuffled Deck:\n${p1.deckToString()}${CLEARCOLOR}\n")
            p1 = p1.drawCardFromDeck()
            println(s"${YELLOW}Player 1 Deck after drawing:\n${p1.deckToString()}${CLEARCOLOR}\n")
            println(s"${PURPLE}Player 1 Hand:\n${p1.handToString()}${CLEARCOLOR}\n")
            p1 = p1.drawCardFromDeck()
            p1 = p1.drawCardFromDeck()
            p1 = p1.discardFromHand(1)
            println(s"${YELLOW}Player 1 Deck after discarding from Hand:\n${p1.deckToString()}${CLEARCOLOR}\n")
            println(s"${PURPLE}Player 1 Hand after discarding from Hand:\n${p1.handToString()}${CLEARCOLOR}\n")
            println(s"${CYAN}Player 1 Discard after discarding from Hand:\n${p1.discardToString()}${CLEARCOLOR}\n")
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

    def list(stock:Stock): Stock = {
        val all_cards = List[Card](Card.Burggraben, Card.Kapelle, Card.Keller, Card.Dorf, Card.Holzfaeller,
                        Card.Werkstatt, Card.Buerokrat, Card.Dieb, Card.Festmahl, Card.Geldverleiher, Card.Miliz,
                        Card.Schmiede, Card.Spion, Card.Thronsaal, Card.Umbau, Card.Bibliothek, Card.Hexe,
                        Card.Jahrmarkt, Card.Laboratorium, Card.Markt, Card.Mine, Card.Ratsversammlung,
                        Card.Abenteurer, Card.Garten)
        val notIncluded = all_cards.filterNot(card => stock.contains(card))
        println("Liste der noch verfuegbaren Karten:")
        println(notIncluded.map(_.name).mkString(" | "))
        stock
    }
}

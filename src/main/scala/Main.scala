import scala.io.StdIn._
import de.htwg.se.dominion.Card
import de.htwg.se.dominion.Stock

@main def main(): Unit = {
    val s = new Stock()

    s.setupStock()
    s.printStock()

    println("Please type out a Card you want to add to the Stock:")

    if (s.addCard(readLine())) {
        println("Card added successfully.")
    } else {
        println("Failed to add card.")
    }

    s.printStock()
}
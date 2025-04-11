import scala.io.StdIn._
import de.htwg.se.dominion.Card
import de.htwg.se.dominion.Stock

@main def main(): Unit = {
    val s = new Stock()
    
    println(s.toString())

    println("Please type out a Card you want to add to the Stock:")

    if (s.addCard(readLine())) {
        println("Card added successfully.")
    } else {
        println("Failed to add card.")
    }

    println(s.toString())
}
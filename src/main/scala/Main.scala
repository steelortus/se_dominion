import scala.io.StdIn._
import de.htwg.se.dominion.Card
import de.htwg.se.dominion.Stock

@main def main(): Unit = {
    var s = new Stock()
    
    println(s.toString())

    println("Please type out a Card you want to add to the Stock:")

    while(s.getLength() < 8) {
        s = s.addCard(readLine())
        s.toString()
    }

    println(s.toString())

    println("Please type out a Card you want to remove from the Stock:")

    while(s.getLength() > 7) {
        s = s.removeCard(readLine())
        s.toString()
    }

    println(s.toString())
}
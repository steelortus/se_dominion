import io.StdIn._
import scala.collection.mutable.ListBuffer
import de.htwg.se.dominion.Card
import de.htwg.se.dominion.Stock


val stock = ListBuffer[Card]()

@main def main(): Unit = {
  val s = new Stock()

  s.setupStock()
  s.printStock()

  println("Please type out a Card you want to add to the Stock:")

  if (s.addCard(getCard(readLine()))) {
    println("Card added successfully.")
  } else {
    println("Failed to add card.")
  }

  s.printStock()
}

def getCard(name: String): Card = {
  var selectedCard: Option[Card] = None
  var inputName = name

  while (selectedCard.isEmpty) {
    selectedCard = Card.values.find(_.toString.equalsIgnoreCase(inputName))
    if (selectedCard.isEmpty) {
      println(s"Card '$inputName' not found. Please try again:")
      inputName = readLine()
    }
  }
  selectedCard.get
}
import io.StdIn._
import scala.collection.mutable.ListBuffer
import de.htwg.se.dominion.Card


val stock = ListBuffer[Card]()

@main def main(): Unit = {
  println(Card.Kupfer.getCost)
  println("Please type out a Card you want to add to the Stock:")
  if (addCard(getCard(readLine()))) {
    println("Card added successfully.")
  } else {
    println("Failed to add card.")
  }
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

def addCard(card:Card): Boolean = {
  if (stock.length < 10) {
    stock += card
    true
  } else {
    println("Cannot add more than 10 cards to the stock.")
    false
  }
}
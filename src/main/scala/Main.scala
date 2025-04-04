import io.StdIn._
import scala.collection.mutable.ListBuffer
// Name, Kosten, Geldwert, Punktewert, Anzahl auf dem Stapel.
val cards = List(("Kupfer", 0, 1, 0, 50), ("Silber", 3, 2, 0, 50), ("Gold", 6, 3, 0, 50), ("Anwesen", 2, 0, 1, 50),
                  ("Herzogtum", 5, 0, 3, 25), ("Provinz", 8, 0, 6, 12), ("Fluch", 0, 0, -1, 50), ("Burggraben", 2, 0, 0, 10),
                  ("Miliz", 4, 0, 0, 9), ("Holzfaeller", 3, 0, 0, 10), ("Markt", 5, 0, 0, 10), ("Laborant", 4, 0, 0, 10),
                  ("Wachstube", 4, 0, 0, 10), ("Keller", 2, -1, -1, -1), ("Garten", -1, -1, -1, -1))
val stock = ListBuffer[(String, Int, Int, Int, Int)]()


@main def main(): Unit = {
  println("Please type out a Card you want to add to the Stock:")
  if (addCard(getCard(readLine()))) {
    println("Card added successfully.")
  } else {
    println("Failed to add card.")
  }
}

def getCard(name: String): (String, Int, Int, Int, Int) = {
  var selectedCard: Option[(String, Int, Int, Int, Int)] = None
  var inputName = name

  while (selectedCard.isEmpty) {
    selectedCard = cards.find(_._1 == inputName)
    if (selectedCard.isEmpty) {
      println(s"Card '$inputName' not found. Please try again:")
      inputName = readLine()
    }
  }
  selectedCard.get
}

def addCard(card: (String, Int, Int, Int, Int)): Boolean = {
  if (stock.length < 10) {
    stock += card
    true
  } else {
    println("Cannot add more than 10 cards to the stock.")
    false
  }
}


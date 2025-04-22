package de.htwg.se.dominion

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec {
    "A Card" should {
        val s = new Stock()
        val regx = "([a-zA-Z]+ - Cost: [0-8]{1}, Value: [0123]{1}, Points: (-1|[01369])+, Amount: [0-9]+\n?)+"
        val card = Card.Kupfer
        "have a getName" in {
            card.getName should be("Kupfer")
            card.getName should not be("Peter")
            card.getName should not be("NotACard")
        }
        "have a getCost" in {
            card.getCost should be(0)
            card.getCost should not be(1)
            card.getCost should not be(-1)
        }
        "have a getValue" in {
            card.getValue should be(1)
            card.getValue should not be(0)
            card.getValue should not be(-1)
        }
        "have a getPoints" in {
            card.getPoints should be(0)
            card.getPoints should not be(1)
            card.getPoints should not be(-1)
        }
        "have a getAmount" in {
            card.getAmount should be(50)
            card.getAmount should not be(0)
            card.getAmount should not be(100)
        }
        "have an auto setup Stock" in {
            s.stock.length should be(7)
            s.stock.contains(Card.Kupfer) should be(true)
            s.stock.contains(Card.Silber) should be(true)
            s.stock.contains(Card.Gold) should be(true)
            s.stock.contains(Card.Anwesen) should be(true)
            s.stock.contains(Card.Herzogtum) should be(true)
            s.stock.contains(Card.Provinz) should be(true)
            s.stock.contains(Card.Fluch) should be(true)

            s.toString should fullyMatch regex regx

            s.stock.contains(Card.Holzfaeller) should be(false)
            s.stock.contains(Card.Miliz) should be(false)
        }
        var updatedStock = stock
        "add a Card to the Stock" in {
            updatedStock = updatedStock.addCard(Card.Holzfaeller)
            updatedStock.stock.contains(Card.Holzfaeller) should be(true)
            updatedStock.getLength() should be(8)
            updatedStock.toString should fullyMatch regex regx

            updatedStock = updatedStock.addCard(Card.Miliz)
            updatedStock.stock.contains(Card.Miliz) should be(true)
            updatedStock.getLength() should be(9)
        }
        "not add a Card to the Stock if it already exists in the Stock" in {
            var sTest = new Stock()
            sTest = sTest.addCard(Card.Kupfer)
            sTest theSameInstanceAs s should be(true)
            sTest.stock.length should be(7)
            
            sTest = sTest.addCard(Card.Silber)
            sTest theSameInstanceAs s should be(true)
            sTest.stock.length should be(7)

            sTest = sTest.addCard(Card.Gold)
            sTest theSameInstanceAs s should be(true)
            sTest.stock.length should be(7)

            sTest.toString should fullyMatch regex regx
        }
        "not add a Card to the Stock if it exceeds the limit" in {
            updatedStock = stock
            updatedStock = updatedStock.addCard(Card.Garten) should be(true)
            updatedStock.stock.contains(Card.Garten) should be(true)

            updatedStock = updatedStock.addCard(Card.Markt) should be(true)
            updatedStock = updatedStock.addCard(Card.Jahrmarkt) should be(true)
            updatedStock = updatedStock.addCard(Card.Laboratorium) should be(true)
            updatedStock = updatedStock.addCard(Card.Burggraben) should be(true)
            updatedStock = updatedStock.addCard(Card.Kapelle) should be(true)
            updatedStock = updatedStock.addCard(Card.Keller) should be(true)
            updatedStock = updatedStock.addCard(Card.Bibliothek) should be(true)
            updatedStock = updatedStock.stock.length should be(17)

            var updatedStock2 = updatedStock
            updatedStock2 = updatedStock2.addCard(Card.Abenteurer) should be(false)
            updatedStock2 theSameInstanceAs updatedStock should be(true)
            updatedStock2.stock.length should be(17)
            updatedStock2.toString should fullyMatch regex regx
        }

        "addCard with String input" in {
            val s2 = new Stock()
            s2.addCard("Dorf") should be(true)
            s2.addCard("Kupfer") should be(false)
            s2.addCard("Peter") should be(false)
            s2.addCard("Garten") should be(true)
            s2.addCard("Markt") should be(true)
            s2.addCard("jahrmarkt") should be(true)
            s2.addCard("LABORAtorium") should be(true)
            s2.addCard("burggraben") should be(true)
            s2.addCard("Kapelle") should be(true)
            s2.addCard("Keller") should be(true)
            s2.addCard("Bibliothek") should be(true)
            s2.addCard("Miliz") should be(true)
            s2.stock.length should be(17)
            s2.addCard("Hexe") should be(false)
            s2.stock.length should be(17)

            s2.toString should fullyMatch regex regx
        }
        "getCard returns the Card or NotACard" in {
            s.getCard("Kupfer") should be(Card.Kupfer)
            s.getCard("Silber") should be(Card.Silber)
            s.getCard("Peter") should be(Card.NotACard)
            s.getCard("NotACard") should be(Card.NotACard)
            s.getCard("Garten") should be(Card.Garten)
            s.getCard("Markt") should be(Card.Markt)
        }
        "cards should be in Stock" in {
            s.stock should contain (Card.Garten)
            s.stock should contain (Card.Markt)
            s.stock should contain (Card.Jahrmarkt)
            s.stock.contains(Card.Dieb) should be(false)
            s.stock.contains(Card.Abenteurer) should be(false)
        }
        "remove a Card from the Stock" in {
            s.removeCard(Card.Holzfaeller) should be(true)
            s.stock.contains(Card.Holzfaeller) should be(false)
            s.stock.length should be(16)

            s.removeCard("Miliz") should be(true)
            s.stock.contains(Card.Miliz) should be(false)
            s.stock.length should be(15)
        }
        "not remove a Card from the Stock if it's mandatory" in {
            s.removeCard(Card.Kupfer) should be(false)
            s.stock.length should be(15)

            s.removeCard(Card.Silber) should be(false)
            s.stock.length should be(15)

            s.removeCard(Card.Gold) should be(false)
            s.stock.length should be(15)

            s.removeCard(Card.Anwesen) should be(false)
            s.stock.length should be(15)

            s.removeCard(Card.Herzogtum) should be(false)
            s.stock.length should be(15)

            s.removeCard(Card.Provinz) should be(false)
            s.stock.length should be(15)

            s.removeCard(Card.Fluch) should be(false)
            s.stock.length should be(15)

            s.toString should fullyMatch regex regx
        }
        "not remove a Cad if it's not in the Stock" in {
            s.removeCard(Card.Holzfaeller) should be(false)
            s.stock.length should be(15)

            s.removeCard("Miliz") should be(false)
            s.stock.length should be(15)

            s.toString should fullyMatch regex regx
        }
    }
}
package de.htwg.se.dominion.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder

class CardSpec extends AnyWordSpec {
    "A Card" should {
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
    }
    "A Stock" should {
        var s = new Stock()
        val regx = "([a-zA-Z]+ - Cost: [0-8]{1}, Value: [0123]{1}, Points: (-1|[01369])+, Amount: [0-9]+\n?)+"
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
        "add a Card to the Stock" in {
            s = s.addCard(Card.Holzfaeller)
            s.stock.contains(Card.Holzfaeller) should be(true)
            s.getLength() should be(8)
            s.toString should fullyMatch regex regx

            s = s.addCard(Card.Miliz)
            s.stock.contains(Card.Miliz) should be(true)
            s.getLength() should be(9)
        }
        "not add a Card to the Stock if it already exists in the Stock" in {
            val regularStock = new Stock()
            var updatedStock = new Stock()

            updatedStock = updatedStock.addCard(Card.Kupfer)
            updatedStock == regularStock should be(true)
            updatedStock.stock.length should be(7)
            
            updatedStock = updatedStock.addCard(Card.Silber)
            updatedStock == regularStock should be(true)
            updatedStock.stock.length should be(7)

            updatedStock = updatedStock.addCard(Card.Gold)
            updatedStock == regularStock should be(true)
            updatedStock.stock.length should be(7)

            updatedStock.toString should fullyMatch regex regx
        }

        "not add a Card to the Stock if it exceeds the limit" in {
            s = s.addCard(Card.Garten)
            s.stock.contains(Card.Garten) should be(true)

            s = s.addCard(Card.Markt)
            s = s.addCard(Card.Jahrmarkt)
            s = s.addCard(Card.Laboratorium)
            s = s.addCard(Card.Burggraben)
            s = s.addCard(Card.Kapelle)
            s = s.addCard(Card.Keller)
            s = s.addCard(Card.Bibliothek)
            s.stock.length should be(17)

            s.toString should fullyMatch regex regx

            var updatedStock = s
            updatedStock = updatedStock.addCard(Card.Abenteurer)
            updatedStock == s should be(true)
            updatedStock.stock.length should be(17)
            updatedStock.toString should fullyMatch regex regx
        }

        "addCard with String input" in {
            var filledStock = new Stock()
            filledStock = filledStock.addCard("Dorf")
            filledStock.stock.contains(Card.Dorf) should be(true)
            filledStock.getLength() should be (8)

            var invalidStock = filledStock.addCard("Peter")
            invalidStock == filledStock should be(true)
            invalidStock.getLength() should be (8)

            filledStock = filledStock.addCard("Garten")
            filledStock = filledStock.addCard("Markt")
            filledStock = filledStock.addCard("jahrmarkt")
            filledStock = filledStock.addCard("LABORAtorium")
            filledStock = filledStock.addCard("burggraben")
            filledStock = filledStock.addCard("Kapelle")
            filledStock = filledStock.addCard("Keller")
            filledStock = filledStock.addCard("Bibliothek")
            filledStock = filledStock.addCard("Miliz")
            filledStock.stock.length should be(17)

            invalidStock = filledStock.addCard("Abenteurer")
            invalidStock == filledStock should be(true)
            invalidStock.stock.length should be(17)

            filledStock.toString should fullyMatch regex regx
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
            s = s.removeCard(Card.Holzfaeller)
            s.stock.contains(Card.Holzfaeller) should be(false)
            s.stock.length should be(16)

            s = s.removeCard("Miliz")
            s.stock.contains(Card.Miliz) should be(false)
            s.stock.length should be(15)
        }
        "not remove a Card from the Stock if it's mandatory" in {
            val originalStock = s

            s = s.removeCard(Card.Kupfer)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s = s.removeCard(Card.Silber)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s = s.removeCard(Card.Gold)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s = s.removeCard(Card.Anwesen)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s = s.removeCard(Card.Herzogtum)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s = s.removeCard(Card.Provinz)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s = s.removeCard(Card.Fluch)
            s == originalStock should be(true)
            s.stock.length should be(15)

            s.toString should fullyMatch regex regx
        }
        "not remove a Card if it's not in the Stock" in {
            s = s.removeCard(Card.Holzfaeller)
            s.stock.length should be(15)

            s = s.removeCard("Miliz")
            s.stock.length should be(15)

            s.toString should fullyMatch regex regx
        }
        "have a method to check, if a card is contained in the stock" in {
            s = new Stock()
            s.contains("Kupfer") should be(true)
            s.contains("Anwesen") should be(true)
            s.contains("Gold") should be(true)
            s.contains("Dorf") should be(false)
        }
        "return a player after executing a Card" in {
            val builder = new TurnHandlerBuilder()
            val players = builder.setNumberOfPlayers(2).setTurn(0).getResult()
            Card.NotACard.execute(players) should be(players)
        }
        "draw 2 Cards if Dorf is executed" in {
            val builder = new TurnHandlerBuilder()
            val players = builder.setNumberOfPlayers(2).setTurn(0).getResult()
            val updatedPlayer = Card.Dorf.execute(players)
            updatedPlayer.getPlayer().hand.length should be(7)
        }
        "print a Sell String of the Stock" in {
            s = new Stock()
            val stockString = "([a-zA-Z]+ - Cost: [0-8]{1}, Amount: [0-9]+\n?)+" 
            s.toSellString() should fullyMatch regex stockString
        }
    }
}
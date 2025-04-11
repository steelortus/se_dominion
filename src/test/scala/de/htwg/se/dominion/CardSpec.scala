package de.htwg.se.dominion

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec {
    "A Card" should {
        val s = new Stock()
        val card = Card.Kupfer
        "have a getName" in {
            card.getName should be("Kupfer")
        }
        "have a getCost" in {
            card.getCost should be(0)
        }
        "have a getValue" in {
            card.getValue should be(1)
        }
        "have a getPoints" in {
            card.getPoints should be(0)
        }
        "have a getAmount" in {
            card.getAmount should be(50)
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

            s.stock.contains(Card.Holzfaeller) should be(false)
            s.stock.contains(Card.Miliz) should be(false)
        }
        "add a Card to the Stock" in {
            s.addCard(Card.Holzfaeller) should be(true)
            s.stock.contains(Card.Holzfaeller) should be(true)
            s.stock.length should be(8)

            s.addCard(Card.Miliz) should be(true)
            s.stock.contains(Card.Miliz) should be(true)
            s.stock.length should be(9)
        }
        "not add a Card to the Stock if it already exists in the Stock" in {
            s.addCard(Card.Kupfer) should be(false)
            s.stock.length should be(9)
            
            s.addCard(Card.Silber) should be(false)
            s.stock.length should be(9)

            s.addCard(Card.Gold) should be(false)
            s.stock.length should be(9)
        }
        "not add a Card to the Stock if it exceeds the limit" in {
            s.addCard(Card.Garten) should be(true)
            s.addCard(Card.Markt) should be(true)
            s.addCard(Card.Jahrmarkt) should be(true)
            s.addCard(Card.Laboratorium) should be(true)
            s.addCard(Card.Burggraben) should be(true)
            s.addCard(Card.Kappele) should be(true)
            s.addCard(Card.Keller) should be(true)
            s.addCard(Card.Bibliothek) should be(true)
            s.stock.length should be(17)

            s.addCard(Card.Abenteurer) should be(false)
            s.stock.length should be(17)
            s.addCard(Card.Dieb) should be(false)
            s.stock.length should be(17)
        }
    }
}
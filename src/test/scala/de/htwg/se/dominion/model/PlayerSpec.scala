package de.htwg.se.dominion.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock

class PlayerSpec extends AnyWordSpec {
    "A Player" should {
        var debug_stock = Stock()
        debug_stock = debug_stock.addCard(Card.Garten)
        debug_stock = debug_stock.addCard(Card.Markt)
        debug_stock = debug_stock.addCard(Card.Jahrmarkt)
        debug_stock = debug_stock.addCard(Card.Dieb)
        debug_stock = debug_stock.addCard(Card.Abenteurer)
        debug_stock = debug_stock.addCard(Card.Dorf)
        debug_stock = debug_stock.addCard(Card.Hexe)
        debug_stock = debug_stock.addCard(Card.Laboratorium)
        debug_stock = debug_stock.addCard(Card.Bibliothek)
        val stock = debug_stock.addCard(Card.Holzfaeller)

        val player = Player()
        val regx = "(((Deck|Hand|Discard):\n?)?([a-zA-Z]+ - Value: [0123]{1}, Points: (-1|[01369])+\n?)*\n?)+"
        "have a deck" in {
            player.deck.length should be(10)

            player.deck.contains(Card.Kupfer) should be(true)
            player.deck.contains(Card.Anwesen) should be(true)
            player.deck.contains(Card.Herzogtum) should be(false)
            player.deck.contains(Card.Provinz) should be(false)

            player.deckToString() should fullyMatch regex regx
        }
        "have a hand" in {
            player.hand.length should be(0)

            player.hand.contains(Card.Kupfer) should be(false)
            player.hand.contains(Card.Anwesen) should be(false)
            player.hand.contains(Card.Herzogtum) should be(false)
            player.hand.contains(Card.Provinz) should be(false)

            player.handToString() should fullyMatch regex regx
        }
        "have a discard" in {
            player.discard.length should be(0)

            player.discard.contains(Card.Kupfer) should be(false)
            player.discard.contains(Card.Anwesen) should be(false)
            player.discard.contains(Card.Herzogtum) should be(false)
            player.discard.contains(Card.Provinz) should be(false)

            player.discardToString() should fullyMatch regex regx
        }
        "shuffle the deck" in {
            val shuffledPlayer = player.shuffleDeck()

            shuffledPlayer.deck.length should be(10)
            shuffledPlayer.deck should not be player.deck

            shuffledPlayer.deckToString() should fullyMatch regex regx
        }
        "draw a card from the deck" in {
            val drawnPlayer = player.drawCardFromDeck()

            drawnPlayer.deck.length should be(9)
            drawnPlayer.hand.length should be(1)

            player.deckToString() should fullyMatch regex regx
            drawnPlayer.handToString() should fullyMatch regex regx
        }
        "discard a card from the deck" in {
            val discardedPlayer = player.discardFromDeck(0)

            discardedPlayer.deck.length should be(9)
            discardedPlayer.discard.length should be(1)

            discardedPlayer.deckToString() should fullyMatch regex regx
            discardedPlayer.discardToString() should fullyMatch regex regx
        }

        "discard a specific card from the deck" in {
            val discardedPlayer = player.discardFromDeck(8)

            discardedPlayer.deck.length should be(9)
            discardedPlayer.discard.length should be(1)

            discardedPlayer.deckToString() should fullyMatch regex regx
            discardedPlayer.discardToString ()should fullyMatch regex regx
        }
        
        "not discard a Card that is outside of the Deck" in {
            val discardedPlayer = player.discardFromDeck(10)

            discardedPlayer.deck.length should be(10)
            discardedPlayer.discard.length should be(0)

            discardedPlayer.deckToString() should fullyMatch regex regx
            discardedPlayer.discardToString() should fullyMatch regex regx
        }

        "discard a card from the hand" in {
            val drawnPlayer = player.drawCardFromDeck()
            val discardedPlayer = drawnPlayer.discardFromHand(0)

            discardedPlayer.deck.length should be(9)
            discardedPlayer.hand.length should be(0)
            discardedPlayer.discard.length should be(1)
            
            discardedPlayer.deckToString() should fullyMatch regex regx
            discardedPlayer.handToString() should fullyMatch regex regx
            discardedPlayer.discardToString() should fullyMatch regex regx
        }

        "discard a specific card from the hand" in {
            var drawnPlayer = player.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()

            drawnPlayer.hand.length should be(5)
            drawnPlayer.deck.length should be(5)

            val discardedPlayer = drawnPlayer.discardFromHand(2)
            discardedPlayer.deck.length should be(5)
            discardedPlayer.hand.length should be(4)
            discardedPlayer.discard.length should be(1)

            discardedPlayer.deckToString() should fullyMatch regex regx
            discardedPlayer.handToString() should fullyMatch regex regx
            discardedPlayer.discardToString() should fullyMatch regex regx
        }

        "not discard a Card that is outside of the Hand" in {
            var drawnPlayer = player.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()

            drawnPlayer.hand.length should be(5)
            drawnPlayer.deck.length should be(5)

            val discardedPlayer = drawnPlayer.discardFromHand(10)
            discardedPlayer.deck.length should be(5)
            discardedPlayer.hand.length should be(5)
            discardedPlayer.discard.length should be(0)

            discardedPlayer.deckToString() should fullyMatch regex regx
            discardedPlayer.handToString() should fullyMatch regex regx
            discardedPlayer.discardToString() should fullyMatch regex regx
        }

        "refill the Deck if it's empty" in {
            var drawnPlayer = player.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()
            drawnPlayer = drawnPlayer.drawCardFromDeck()

            val emptyDeckPlayer = drawnPlayer.discardFromHand(0)

            emptyDeckPlayer.deck.length should be(0)

            val refilledPlayer = emptyDeckPlayer.refillDeck()
            refilledPlayer.deck.length should be(1)
            refilledPlayer.discard.length should be(0)

            val drawFromEmptyPlayer = emptyDeckPlayer.drawCardFromDeck()
            drawFromEmptyPlayer.deck.length should be(0)
            drawFromEmptyPlayer.discard.length should be(0)
        }

        "should have a toString() that prints out the deck, hand and discard" in {
            player.toString() should fullyMatch regex regx
            player.deckToString() should fullyMatch regex regx
            player.handToString() should fullyMatch regex regx
            player.discardToString() should fullyMatch regex regx
        }
        "should be able to draw 5 Cards" in {
            val fivedrawn = player.drawFiveCardsFromDeck()
            fivedrawn.hand.length should be(5)
            fivedrawn.deck.length should be(5)
        }
        "not be able to refill deck if it's not empty" in {
            val refilled = player.refillDeck()
            refilled == player should be(true)
        }
        "discard all Cards from hand" in {
            val fiveDrawn = player.drawFiveCardsFromDeck()
            val discarded = fiveDrawn.discardAllFromHand()

            discarded.deck.length shouldBe(5)
            discarded.hand.length shouldBe(0)
            discarded.discard.length shouldBe(5)
        }
        "count the money currently in Hand" in {
            val fiveDrawn = player.drawFiveCardsFromDeck()
            fiveDrawn.getMoneyInHand() shouldBe(5)

            val drawOnce = player.drawCardFromDeck()
            drawOnce.getMoneyInHand() shouldBe(1)
        }

        "purchase a card from Stock" in {
            val p1 = player.drawFiveCardsFromDeck()
            val result = p1.purchaseCard(Card.Silber, stock)

            result.discard.contains(Card.Silber) should be(true)
            result.hand.contains(Card.Silber) should be(false)
            result.deck.contains(Card.Silber) should be(false)
        }

        "not purchase a card that is not in Stock" in {
            val p1 = player.drawFiveCardsFromDeck()
            val result = p1.purchaseCard(Card.Werkstatt, stock)

            result.discard.contains(Card.Werkstatt) should be(false)
            result.hand.contains(Card.Werkstatt) should be(false)
            result.deck.contains(Card.Werkstatt) should be(false)
        }

        "add a Card to the hand" in {
            val p1 = player.addCardToHand(Card.Silber)
            p1.hand.contains(Card.Silber) should be(true)
        }

        "add a Card to the deck" in {
            val p1 = player.addCardToDeck(Card.Silber)
            p1.deck.contains(Card.Silber) should be(true)
        }

        "not return a card if it's not in the discard" in {
            val p1 = player.returnCard(Card.Silber)
            p1.discard.contains(Card.Silber) should be(false)
            p1 == player should be(true)
        }

        "be able to purchase a Card with a card String" in {
            val result = player.purchaseCard("Kupfer", stock)
            result.discard.contains(Card.Kupfer) should be(true)
        }
    }
}
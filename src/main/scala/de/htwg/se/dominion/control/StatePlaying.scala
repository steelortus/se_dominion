package de.htwg.se.dominion
package control

import control.State
import model.ConsoleColors.*
import model.Stock
import model.Card
import model.Player
import model.TurnHandler
import util.Event
import util.ErrorEvent
import util.Observable
import util.Command
import util.UndoManager
import control.commands._

case class StatePlaying(stock: Stock) extends State {
    override def addCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock = {
        stock
    }

    override def removeCard(card: String, stock: Stock, um: UndoManager[Stock]): Stock = {
        stock
    }

    override def play(stock: Stock, th: TurnHandler): Boolean = {
        var p1 = new Player()
        println(YELLOW(s"Player 1 Deck:\n${p1.deckToString()}\n"))
        p1 = p1.shuffleDeck()
        println(GREEN(s"Player 1 shuffled Deck:\n${p1.deckToString()}\n"))
        p1 = p1.drawCardFromDeck()
        println(YELLOW(s"Player 1 Deck after drawing:\n${p1.deckToString()}\n"))
        println(PURPLE(s"Player 1 Hand:\n${p1.handToString()}\n"))
        p1 = p1.drawCardFromDeck()
        p1 = p1.drawCardFromDeck()
        println(PURPLE(s"Player 1 Hand after drawing 2 more from Hand:\n${p1.handToString()}\n"))
        p1 = p1.discardFromHand(1)
        println(YELLOW(s"Player 1 Deck after discarding from Hand:\n${p1.deckToString()}\n"))
        println(PURPLE(s"Player 1 Hand after discarding from Hand:\n${p1.handToString()}\n"))
        println(CYAN(s"Player 1 Discard after discarding from Hand:\n${p1.discardToString()}\n"))

        println(YELLOW("\nGame over! (not implemented yet)\n\n"))
        var p2 = new Player()
        println(YELLOW(s"Player 2 Deck:\n${p2.deckToString()}\n"))
        p2 = p2.shuffleDeck()
        println(GREEN(s"Player 2 shuffled Deck:\n${p2.deckToString()}\n"))
        p2 = p2.drawFiveCardsFromDeck()
        println(YELLOW(s"Player 2 Deck after drawing five times:\n${p2.deckToString()}\n"))
        println(PURPLE(s"Player 1 Hand:\n${p2.handToString()}\n"))
        println(RED(s"Player 2 Money in Hand: ${p2.getMoneyInHand()}\n"))
        p2 = p2.discardAllFromHand()
        println(PURPLE(s"Player 2 Hand after discarding from Hand:\n${p2.handToString()}\n"))
        println(CYAN(s"Player 2 Discard after discarding from Hand:\n${p2.discardToString()}\n"))
        false
    }

    override def fillStock(stock: Stock, um: UndoManager[Stock]): Stock = {
        stock
    }

    override def listCards(stock: Stock): String = {
        stock.toString()
    }

    override def purchase(stock: Stock, card: Card, th: TurnHandler, um: UndoManager[TurnHandler]): TurnHandler = {
        val updatedPlayer = um.doStep(stock, PurchaseCommand(card, th.getPlayer()))
        th

        /*if (stock.contains(card)) {
            val player = th.getCurrentPlayer()
            val playerAfterPurchase = player.purchaseCard(card, player.getMoneyInHand())
            if (playerAfterPurchase != player) {
                th.setCurrentPlayer(playerAfterPurchase)
            } else {
                throw new ErrorEvent("Not enough money to purchase this card.")
            }
        } else {
            throw new ErrorEvent("Card not available in stock.")
        }*/
    }
}
package de.htwg.se.dominion.fileio

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import scala.xml._
import java.io._

class FileIOXml extends FileIOInterface:

    override def save(stock: StockInterface, th: TurnHandlerInterface): Unit = {
        val xml =
            <dominion>
                <stock>{stock.stock.map(card => <card>{card.toString}</card>)}</stock>
                <stockAmount>{stock.stockAmount.map(a => <amount>{a}</amount>)}</stockAmount>
                <turn>{th.turn}</turn>
                <players>
                    {th.players.map(p =>
                    <player>
                        <hand>{p.hand.map(c => <card>{c.toString}</card>)}</hand>
                        <deck>{p.deck.map(c => <card>{c.toString}</card>)}</deck>
                        <discard>{p.discard.map(c => <card>{c.toString}</card>)}</discard>
                        <actions>{p.actions}</actions>
                        <purchases>{p.purchases}</purchases>
                    </player>
                    )}
                </players>
            </dominion>
        XML.save("dominion_save.xml", xml, "UTF-8", xmlDecl = true, null)
    }

    override def load(): (StockInterface, TurnHandlerInterface) = {
        val file = XML.loadFile("dominion_save.xml")
        val stockCards = (file \ "stock" \ "card").map(n => Card.valueOf(n.text)).toList
        val amounts = (file \ "stockAmount" \ "amount").map(n => n.text.toInt).toList
        val stock = Stock(stockCards, amounts)
        val turn = (file \ "turn").text.toInt
        val players = (file \ "player").map { pNode =>
            val hand = (pNode \ "hand" \ "card").map(n => Card.valueOf(n.text)).toList
            val deck = (pNode \ "deck" \ "card").map(n => Card.valueOf(n.text)).toList
            val discard = (pNode \ "discard" \ "card").map(n => Card.valueOf(n.text)).toList
            val actions = (pNode \ "actions").text.toInt
            val purchases = (pNode \ "purchases").text.toInt
            new de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player(deck, hand, discard, purchases, actions)
        }.toList
        val th = new TurnHandler(players.length, turn, players)
        (stock, th)
    }
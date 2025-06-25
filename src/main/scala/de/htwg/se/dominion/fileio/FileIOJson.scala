package de.htwg.se.dominion.fileio

import de.htwg.se.dominion.model.Card
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import play.api.libs.json._
import java.io._

class FileIOJson extends FileIOInterface:

    override def save(stock: StockInterface, th: TurnHandlerInterface): Unit = {
        val pw = new PrintWriter(new File("dominion_save.json"))
        pw.write(Json.prettyPrint(stockToJson(stock, th)))
    }

    override def load(): (StockInterface, TurnHandlerInterface) = {
        val source = scala.io.Source.fromFile("dominion_save.json")
        val json = try source.mkString finally source.close()
        val js = Json.parse(json)
        val stock = jsonToStock(js)
        val th = jsonToTurnHandler(js)
        (stock, th)
    }

    private def stockToJson(stock: StockInterface, th: TurnHandlerInterface): JsValue = {
        Json.obj(
            "stock" -> stock.stock.map(_.toString),
            "stockAmount" -> stock.stockAmount,
            "turn" -> th.turn,
            "players" -> th.players.map(p =>
                Json.obj(
                    "hand" -> p.hand.map(_.toString),
                    "deck" -> p.deck.map(_.toString),
                    "discard" -> p.discard.map(_.toString),
                    "actions" -> p.actions,
                    "purchases" -> p.purchases
                )
            )
        )
    }

    private def jsonToStock(json: JsValue): StockInterface = {
        val cards = (json \ "stock").head.as[List[String]].map(Card.valueOf)
        val amounts = (json \ "stockAmount").head.as[List[Int]]
        Stock(cards, amounts)
    }

    private def jsonToTurnHandler(json: JsValue): TurnHandlerInterface = {
        val turn = (json \ "turn").head.as[Int]
        val players = (json \ "players").head.as[List[JsValue]].map { pJson =>
            val hand = (pJson \ "hand").head.as[List[String]].map(Card.valueOf)
            val deck = (pJson \ "deck").head.as[List[String]].map(Card.valueOf)
            val discard = (pJson \ "discard").head.as[List[String]].map(Card.valueOf)
            val actions = (pJson \ "actions").head.as[Int]
            val purchases = (pJson \ "purchases").head.as[Int]
            new de.htwg.se.dominion.model.playerComponent.playerComponentImplementation.Player(deck, hand, discard, purchases, actions)
        }
        new TurnHandler(players.size, turn, players)
    }
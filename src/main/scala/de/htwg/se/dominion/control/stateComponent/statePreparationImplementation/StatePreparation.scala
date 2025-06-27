package de.htwg.se.dominion
package control.stateComponent.statePreparationImplementation

import control.stateComponent.State
import de.htwg.se.dominion.model.stockComponent.StockInterface
import model.Card
import model.playerComponent.PlayerInterface
import model.turnHandlerComponent.TurnHandlerInterface
import util.Event
import util.ErrorEvent
import util.Observable
import util.Command
import util.UndoManager
import control.commands._
import fileio.FileIOInterface

case class StatePreparation(stock: StockInterface) extends State {

    override def addCard(card: String, stock: StockInterface, um: UndoManager[StockInterface]): StockInterface = {
        val updatedStock = um.doStep(stock, AddCardCommand(stock.getCard(card)))
        updatedStock
    }

    override def removeCard(card: String, stock: StockInterface, um: UndoManager[StockInterface]): StockInterface = {
        val updatedStock = um.doStep(stock, RemoveCardCommand(stock.getCard(card)))
        updatedStock
    }

    override def play(stock: StockInterface, th: TurnHandlerInterface): Boolean = {
        if (stock.getLength() < 17) {
            false
        } else {
            true
        }
    }

    override def fillStock(stock: StockInterface, um: UndoManager[StockInterface]): StockInterface = {
        val updatedStock = um.doStep(stock, FillStockCommand())
        updatedStock
    }

    override def listCards(stock: StockInterface): String = {
        val all_cards = List[Card](Card.Burggraben, Card.Kapelle, Card.Keller, Card.Dorf, Card.Holzfaeller,
                        Card.Werkstatt, Card.Buerokrat, Card.Dieb, Card.Festmahl, Card.Geldverleiher, Card.Miliz,
                        Card.Schmiede, Card.Spion, Card.Thronsaal, Card.Umbau, Card.Bibliothek, Card.Hexe,
                        Card.Jahrmarkt, Card.Laboratorium, Card.Markt, Card.Mine, Card.Ratsversammlung,
                        Card.Abenteurer, Card.Garten)
        val notIncluded = all_cards.filterNot(card => stock.contains(card))
        val s1 = "Liste der noch verfuegbaren Karten:"
        val s2 = notIncluded.map(_.name).mkString(" | ")
        s"$s1\n\n$s2"
    }

    override def purchase(stock: StockInterface, card: Card, th: TurnHandlerInterface, um: UndoManager[TurnHandlerInterface]): TurnHandlerInterface = {
        th
    }

    override def load(fileIO: FileIOInterface): (StockInterface, TurnHandlerInterface) = {
        fileIO.load()
    }

    override def save(fileIO: FileIOInterface, stock: StockInterface, th: TurnHandlerInterface): Unit = {
        fileIO.save(stock, th)
    }
}
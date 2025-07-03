package de.htwg.se.dominion.modules

import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.control.stateComponent.State
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation
import de.htwg.se.dominion.control.stateComponent.statePlayingImplementation.StatePlaying
import de.htwg.se.dominion.control.controlComponent.ControllerInterface
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.fileio.FileIOInterface
import de.htwg.se.dominion.fileio.FileIOJson
import de.htwg.se.dominion.fileio.FileIOXml
import de.htwg.se.dominion.model.Card

object DefaultSettings {
    val firstStock = new Stock()
    given stock: StockInterface = firstStock
    given th: TurnHandlerInterface = new TurnHandlerBuilder().setNumberOfPlayers(0).setTurn(0).getResult()
    given state: State = new StatePreparation(firstStock)
    given fileIO: FileIOInterface = new FileIOJson()
    given controller: ControllerInterface = new Controller()
}

object TestSettings {
    val trulyFullStock = new Stock ++ List(Card.Garten, Card.Burggraben, Card.Dorf, Card.Holzfaeller, Card.Werkstatt, Card.Kapelle, Card.Thronsaal, Card.Schmiede, Card.Hexe, Card.Spion)
    given fullStock: StockInterface = trulyFullStock
    given newTh: TurnHandlerInterface = TurnHandlerBuilder().setNumberOfPlayers(2).setTurn(0).getResult()
    given testPlayState: State = StatePlaying(Stock())
    given testPlayStateFullStock: State = StatePlaying(trulyFullStock)
    given newFileIO: FileIOInterface = new FileIOXml()
}
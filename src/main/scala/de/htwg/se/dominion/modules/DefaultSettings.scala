package de.htwg.se.dominion.modules

import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.control.stateComponent.State
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation
import de.htwg.se.dominion.control.controlComponent.ControllerInterface
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.fileio.FileIOInterface
import de.htwg.se.dominion.fileio.FileIOJson
import de.htwg.se.dominion.fileio.FileIOXml

object DefaultSettings {
    val firstStock = new Stock()
    given stock: StockInterface = firstStock
    given th: TurnHandlerInterface = new TurnHandlerBuilder().setNumberOfPlayers(0).setTurn(0).getResult()
    given state: State = new StatePreparation(firstStock)
    given fileIO: FileIOInterface = new FileIOJson()
    given controller: ControllerInterface = new Controller()
}
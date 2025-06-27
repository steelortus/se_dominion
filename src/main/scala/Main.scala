import scala.io.StdIn._
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.view._
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation

import de.htwg.se.dominion.modules.DefaultSettings.{controller}

@main def main(): Unit = {
    val t = new TUI()
    //GUI.initWith(controller)
    t.run()

}
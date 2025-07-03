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
    new Thread(() => {
        val t = new TUI()
        t.run()
    }, "TUI-Thread").start()

    GUI.initWith(controller)
    Thread.currentThread().join()
}
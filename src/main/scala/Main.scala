import scala.io.StdIn._
import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.view._
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation

import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.dominion.modules.DefaultController.given

@main def main(): Unit = {
    //val stock = new Stock()
    //val state = new StatePreparation(stock)
    //val builder = new TurnHandlerBuilder()
    //val th = builder.setNumberOfPlayers(0).setTurn(0).getResult()
    //val controller = new Controller(stock, state, th)
    val t = new TUI()
    
    //new Thread(() => GUI.initWith(controller)).start()

    t.run()
}
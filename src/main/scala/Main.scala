import scala.io.StdIn._
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.view._
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation

@main def main(): Unit = {
    val stock = new Stock()
    val state = new StatePreparation(stock)
    val builder = new TurnHandlerBuilder()
    val th = builder.setNumberOfPlayers(0).setTurn(0).getResult()
    val controller = new Controller(stock, state, th)
    val t = new TUI(controller)
    
    new Thread(() => GUI.initWith(controller)).start()

    t.run()
}
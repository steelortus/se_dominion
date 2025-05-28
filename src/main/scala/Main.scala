import scala.io.StdIn._
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.model.TurnHandler
import de.htwg.se.dominion.model.TurnHandlerBuilder
import de.htwg.se.dominion.view.TUI
import de.htwg.se.dominion.control.Controller
import de.htwg.se.dominion.control.StatePreparation

// hallo :)

@main def main(): Unit = {
    val stock = new Stock()
    val state = new StatePreparation(stock)
    val builder = new TurnHandlerBuilder()
    val th = builder.setNumberOfPlayers(0).setTurn(0).getResult()
    val controller = new Controller(stock, state, th)
    val t = new TUI(controller)
    t.run()
}
import scala.io.StdIn._
import de.htwg.se.dominion.model.Stock
import de.htwg.se.dominion.view.TUI
import de.htwg.se.dominion.control.Controller

@main def main(): Unit = {
    val stock = new Stock()
    val controller = new Controller(stock)
    val t = new TUI(controller)
    t.run()
}
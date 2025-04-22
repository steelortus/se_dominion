import scala.io.StdIn._
import de.htwg.se.dominion.Stock
import de.htwg.se.dominion.TUI

@main def main(): Unit = {
    val stock = new Stock()
    val t = new TUI()
    t.run(stock)
}
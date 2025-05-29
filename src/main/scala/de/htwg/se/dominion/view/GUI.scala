import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.text.Text

import de.htwg.se.dominion.control.Controller
import de.htwg.se.dominion.util.Observer
import de.htwg.se.dominion.util.Event
import de.htwg.se.dominion.util.ErrorEvent

object GUI extends JFXApp3 with Observer {
    override def update(e: Event): Unit = {
        e match {
            case Event.preparation =>
                println("Preparation phase started.")
            case Event.stockFull =>
                println("Stock is full.")
            case Event.cardAdded =>
                println("Card added to stock.")
            case Event.cardRemoved =>
                println("Card removed from stock.")
            case Event.playing =>
                println("Game is now in playing state.")
            case Event.selectNumberOfPlayers =>
                println("Please select the number of players.")
            case _ =>
                println("Event not handled: " + e)
        }
    }

    override def update(e: ErrorEvent): Unit = {
        e match {
            case ErrorEvent.stockFull =>
                println("Error: Stock is full, cannot add more cards.")
            case ErrorEvent.couldNotAddCard =>
                println("Error: Could not add card to stock.")
            case ErrorEvent.couldNotRemoveCard =>
                println("Error: Could not remove card from stock.")
            case ErrorEvent.cantStart =>
                println("Error: Cannot start the game, please check the stock.")
            case ErrorEvent.invalidCommand =>
                println("Error: Invalid command entered.")
            case ErrorEvent.invalidNumberOfPlayers =>
                println("Error: Invalid number of players selected.")
            case ErrorEvent.invalidState =>
                println("Error: Invalid state for the operation.")
            case _ =>
                println("Error not handled: " + e)
        }
    }

    override def start(): Unit = {
        stage = new JFXApp3.PrimaryStage {
        //    initStyle(StageStyle.Unified)
            title = "ScalaFX Hello World"
            scene = new Scene {
                fill = Color.rgb(38, 38, 38)
                content = new HBox {
                    padding = Insets(50, 80, 50, 80)
                    children = Seq(
                        new Text {
                            text = "Scala"
                            style = "-fx-font: normal bold 100pt sans-serif"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(Red, DarkRed))
                        },
                        new Text {
                            text = "FX"
                            style = "-fx-font: italic bold 100pt sans-serif"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(White, DarkGray)
                            )
                            effect = new DropShadow {
                            color = DarkGray
                            radius = 15
                            spread = 0.25
                            }
                        }
                    )
                }
            }
        }
    }
}
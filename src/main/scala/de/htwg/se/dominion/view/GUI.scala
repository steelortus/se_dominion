package de.htwg.se.dominion.view

import de.htwg.se.dominion.control._
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util.{Observer, Event, ErrorEvent}
import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.{Dimension, Image}
import javax.swing.ImageIcon
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object GUI extends SimpleSwingApplication with Observer {
    var controller: Controller = _

    def initWith(c: Controller): Unit = {
        controller = c
        controller.add(this)
        main(Array())
    }

    val statusLabel = new Label("Dominion started!")

    val stockPanel = new FlowPanel(FlowPanel.Alignment.Left)()
    val selectionPanel = new FlowPanel(FlowPanel.Alignment.Left)()
    val startGameButton = new Button("Start the game") { visible = false }
    val undoButton = new Button("undo")
    val redoButton = new Button("redo")

    val playerButtons = new FlowPanel(FlowPanel.Alignment.Center)(
        Seq(2, 3, 4).map { n =>
            new Button(s"$n players") {
                reactions += { case ButtonClicked(_) => controller.createPlayers(n) }
            }
        }*
    )
    playerButtons.visible = false

    listenTo(startGameButton)
    reactions += {
        case ButtonClicked(`startGameButton`) =>
            Future { controller.play() }
    }

    val unredoButtons = new FlowPanel(FlowPanel.Alignment.Right)(
        undoButton,
        redoButton
    )
    unredoButtons.visible = true

    listenTo(undoButton, redoButton)
    reactions += {
        case ButtonClicked(`undoButton`) => {
            controller.undo()
        }
        case ButtonClicked(`redoButton`) => {
            controller.redo()
        }
    }

    def top: Frame = new MainFrame {
        title = "Dominion GUI"
        preferredSize = new Dimension(900, 600)
        contents = new BorderPanel {
        layout(new BoxPanel(Orientation.Vertical) {
            contents += statusLabel
            contents += unredoButtons
        }) = BorderPanel.Position.North

        layout(new BoxPanel(Orientation.Vertical) {
            contents += new Label("Stock:")
            contents += new ScrollPane(stockPanel)
            contents += Swing.VStrut(10)
            contents += new Label("Cards to add:")
            contents += new ScrollPane(selectionPanel)
        }) = BorderPanel.Position.Center

        layout(new BoxPanel(Orientation.Vertical) {
            contents += startGameButton
            contents += playerButtons
            border = Swing.EmptyBorder(10, 0, 0, 0)
        }) = BorderPanel.Position.South
        }
        controller.updateState(Event.preparation)
    }

    def loadCardImage(cardName: String, onClick: () => Unit): Option[Label] = {
        val fileName = cardName.toLowerCase.replaceAll("\\s+", "") + ".jpg"
        val resourcePath = "/images/" + fileName
        val resource = Option(getClass.getResource(resourcePath))
        resource.map { url =>
            val img = new ImageIcon(url)
            val scaled = img.getImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH)
            val scimg = new ImageIcon(scaled)
            new Label {
                icon = scimg
                listenTo(mouse.clicks)
                reactions += {
                    case _: event.MouseClicked => onClick()
                }
            }
        }
    }

    def updateStockDisplay(): Unit = {
        stockPanel.contents.clear()
        controller.getStock().stock.foreach { card =>
        loadCardImage(card.getName, () => controller.removeCard(card.getName)).foreach(stockPanel.contents += _)
        }
        stockPanel.revalidate()
        stockPanel.repaint()
    }

    def updateCardSelection(): Unit = {
        selectionPanel.contents.clear()
        val lines = controller.listCards().split("\n").drop(2)
        lines.flatMap(_.split(" | ")).map(_.trim).filter(_.nonEmpty).foreach { name =>
            loadCardImage(name, () => controller.addCard(name)).foreach { img =>
                selectionPanel.contents += img
            }
        }
        selectionPanel.revalidate()
        selectionPanel.repaint()
    }

    override def update(e: Event): Unit = Swing.onEDT {
        println(s"[GUI] Received event: $e")
        e match {
        case Event.preparation =>
            statusLabel.text = "Preparation phase. Choose cards to add to the stock"
            updateStockDisplay()
            updateCardSelection()

        case Event.cardAdded | Event.cardRemoved =>
            updateStockDisplay()
            updateCardSelection()

        case Event.stockFull =>
            statusLabel.text = "Stock full. Start the game"
            startGameButton.visible = true
            updateStockDisplay()
            updateCardSelection()

        case Event.selectNumberOfPlayers =>
            statusLabel.text = "Choose number of players"
            startGameButton.visible = false
            playerButtons.visible = true

        case Event.playing =>
            statusLabel.text = "Game started!"
            playerButtons.visible = false

        case Event.undoPrep =>
            statusLabel.text = "undo"
            updateStockDisplay()
            updateCardSelection()

        case Event.redoPrep =>
            statusLabel.text = "redo"
            updateStockDisplay()
            updateCardSelection()
        }
    }

    override def update(e: ErrorEvent): Unit = Swing.onEDT {
        statusLabel.text = e match {
        case ErrorEvent.stockFull => "Stock full!"
        case ErrorEvent.couldNotAddCard => "Card could not be added"
        case ErrorEvent.couldNotRemoveCard => "Card can not be removed"
        case ErrorEvent.cantStart => "Can not start the game"
        case ErrorEvent.invalidCommand => "unknown command"
        case ErrorEvent.invalidNumberOfPlayers => "invalid number of players"
        case ErrorEvent.invalidState => "invalid state"
        }
    }
}

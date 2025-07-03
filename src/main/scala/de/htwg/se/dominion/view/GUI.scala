package de.htwg.se.dominion.view

import de.htwg.se.dominion.control.controlComponent.ControllerInterface
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util.{Observer, Event, ErrorEvent}
import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.{Dimension, Image}
import javax.swing.ImageIcon
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object GUI extends SimpleSwingApplication with Observer {
    var controller: ControllerInterface = _

    def initWith(c: ControllerInterface): Unit = {
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
    val stockScroll = new ScrollPane(stockPanel)
    val selectScroll = new ScrollPane(selectionPanel)
    stockScroll.horizontalScrollBar.unitIncrement = 48
    selectScroll.horizontalScrollBar.unitIncrement = 48

    val saveMenuItem = new MenuItem(Action("Save game") {
        if (controller.getStock().getLength() == 17 && controller.getTurn() > 0) {
            controller.save()
            statusLabel.text = "Game saved."
        } else {
            statusLabel.text = "Cannot save the game if it has not started yet. Fill the stock!"
        }
    })

    val loadMenuItem = new MenuItem(Action("Load game") {
        controller.load()
        statusLabel.text = "Game loaded."
    })

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

    val stockRows: Array[FlowPanel] = Array.fill(3)(new FlowPanel(FlowPanel.Alignment.Left)())
    val playerHandPanel = new FlowPanel(FlowPanel.Alignment.Left)()
    val gameArea = new BoxPanel(Orientation.Vertical) {
        contents ++= stockRows
        contents += Swing.VStrut(24)
        contents += new Label("Active player:")
        contents += playerHandPanel
        border = Swing.EmptyBorder(16, 16, 16, 16)
    }
    gameArea.visible = false

    def top: Frame = new MainFrame {
        title = "Dominion GUI"
        preferredSize = new Dimension(900, 600)

        menuBar = new MenuBar {
            contents += new Menu("Datei") {
                contents += saveMenuItem
                contents += loadMenuItem
            }
        }
        contents = new BorderPanel {
            layout(new BoxPanel(Orientation.Vertical) {
                contents += statusLabel
                contents += unredoButtons
            }) = BorderPanel.Position.North

            layout(new BoxPanel(Orientation.Vertical) {
                contents += statusLabel
                contents += startGameButton
                contents += playerButtons
                contents += new Label("Stock:")
                contents += stockScroll
                contents += selectScroll
                contents += gameArea
            }) = BorderPanel.Position.Center
        }
        
        controller.updateState(Event.preparation)
    }

    def updateGameArea(): Unit = {
        val stockList = controller.getStock().stock
        val cardsPerRow = 6
        stockRows.foreach(_.contents.clear())
        for ((card, idx) <- stockList.zipWithIndex) {
            val row = idx / cardsPerRow
            loadCardImage(card.getName, () => controller.purchase(card.getName)).foreach { img =>
                stockRows(row).contents += img
            }
        }
        stockRows.foreach { row => row.revalidate(); row.repaint() }

        playerHandPanel.contents.clear()
        val handCards = controller.getPlayerHand().split(",").map(_.trim).filter(_.nonEmpty)
        handCards.foreach { name =>
            loadCardImage(name, () => ()).foreach(playerHandPanel.contents += _)
        }
        playerHandPanel.revalidate()
        playerHandPanel.repaint()
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
            gameArea.visible = false

        case Event.cardAdded | Event.cardRemoved =>
            updateStockDisplay()
            updateCardSelection()
            gameArea.visible = false

        case Event.stockFull =>
            statusLabel.text = "Stock full. Start the game"
            startGameButton.visible = true
            updateStockDisplay()
            updateCardSelection()
            gameArea.visible = false

        case Event.selectNumberOfPlayers =>
            statusLabel.text = "Choose number of players"
            startGameButton.visible = false
            playerButtons.visible = true
            gameArea.visible = false

        case Event.playing =>
            statusLabel.text = "Game started!"
            playerButtons.visible = false
            startGameButton.visible = false
            selectionPanel.visible = false
            stockPanel.visible = false
            gameArea.visible = true
            updateGameArea()

        case Event.undoPrep =>
            statusLabel.text = "undo"
            updateStockDisplay()
            updateCardSelection()

        case Event.redoPrep =>
            statusLabel.text = "redo"
            updateStockDisplay()
            updateCardSelection()

        case _ =>
            statusLabel.text = "Unknown event"
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

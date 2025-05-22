package de.htwg.se.dominion.util

trait Command {
    def doStep(): Unit
    def undoStep(): Unit
    def redoStep(): Unit
}

class UndoManager {
    private var undoStack: List[Command] = List()
    private var redoStack: List[Command] = List()

    def doStep(command: Command): Unit = {
        undoStack = command :: undoStack
        command.doStep()
    }

    def undoStep(): Unit = {
        undoStack match {
            case Nil => t
            case head :: stack => {
                val result = head.undoStep(t)
                undoStack = stack
                redoStack = head :: redoStack
                result
            }
        }
    }

    def redoStep(): Unit = {
        redoStack match {
            case Nil => t
            case head :: stack => {
                val result = head.redoStep(t)
                redoStack = stack
                undoStack = head :: undoStack
                result
            }
        }
    }
}
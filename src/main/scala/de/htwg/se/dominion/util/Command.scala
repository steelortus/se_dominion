package de.htwg.se.dominion.util

trait Command[T] {
    def doStep(stock: T): T
    def undoStep(stock: T): T
    def redoStep(stock: T): T
}

class UndoManager[T] {
    private var undoStack: List[Command[T]] = List()
    private var redoStack: List[Command[T]] = List()

    def doStep(stock: T, command: Command[T]): T = {
        undoStack = command :: undoStack
        command.doStep(stock)
    }

    def undoStep(stock: T): T = {
        undoStack match {
            case Nil => stock
            case head :: stack => {
                val result = head.undoStep(stock)
                undoStack = stack
                redoStack = head :: redoStack
                result
            }
        }
    }

    def redoStep(stock: T): T = {
        redoStack match {
            case Nil => stock
            case head :: stack => {
                val result = head.redoStep(stock)
                redoStack = stack
                undoStack = head :: undoStack
                result
            }
        }
    }
}
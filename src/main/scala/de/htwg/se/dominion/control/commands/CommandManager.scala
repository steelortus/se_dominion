package de.htwg.se.dominion.control.commands

class CommandManager {
    private var undoStack: List[Command] = Nil
    private var redoStack: List[Command] = Nil


    def executeCommand(cmd: Command): Unit = {
        cmd.execute()
        undoStack = cmd::undoStack
        redoStack = Nil
    }

    def undo(): Unit = undoStack match {
        case head :: tail =>
            head.undo()
            undoStack = tail
            redoStack = head::redoStack
        case Nil =>
    }


    def redo(): Unit = redoStack match {
        case head :: tail =>
            head.execute()
            redoStack = tail
            undoStack = head::undoStack
        case Nil =>
    }
}

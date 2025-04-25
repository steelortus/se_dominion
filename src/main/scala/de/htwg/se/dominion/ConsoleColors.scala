package de.htwg.se.dominion

enum ConsoleColors(val code: String) {
    case CLEARCOLOR extends ConsoleColors("\u001B[0m")
    case RED extends ConsoleColors("\u001B[31m")
    case GREEN extends ConsoleColors("\u001B[32m")
    case YELLOW extends ConsoleColors("\u001B[33m")
    case BLUE extends ConsoleColors("\u001B[34m")
    case PURPLE extends ConsoleColors("\u001B[35m")
    case CYAN extends ConsoleColors("\u001B[36m")
    case WHITE extends ConsoleColors("\u001B[37m")

    def apply(text: String): String = s"$code$text${CLEARCOLOR.code}"
}
package de.htwg.se.dominion.fileio

import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface

trait FileIOInterface {
    def save(stock: StockInterface, th: TurnHandlerInterface): Unit
    def load(): (StockInterface, TurnHandlerInterface)
}

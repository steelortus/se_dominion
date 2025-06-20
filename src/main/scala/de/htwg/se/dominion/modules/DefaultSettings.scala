package de.htwg.se.dominion.modules

import com.google.inject.{AbstractModule, Guice, Inject}
import net.codingwell.scalaguice.ScalaModule

import de.htwg.se.dominion.model.stockComponent.StockInterface
import de.htwg.se.dominion.model.stockComponent.stockComponentImplementation.Stock
import de.htwg.se.dominion.model.turnHandlerComponent.TurnHandlerInterface
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.control.stateComponent.State
import de.htwg.se.dominion.control.stateComponent.statePreparationImplementation.StatePreparation
import de.htwg.se.dominion.control.controlComponent.ControllerInterface
import de.htwg.se.dominion.control.controlComponent.controlBaseImplentation.Controller

object DefaultController {
    val injector = Guice.createInjector(new SettingsModule())
    given controller: ControllerInterface = injector.getInstance(classOf[Controller])
}

class SettingsModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
        val stock = new Stock()
        bind[StockInterface].toInstance(stock)
        bind[TurnHandlerInterface].toInstance(new TurnHandlerBuilder().setNumberOfPlayers(0).setTurn(0).getResult())
        bind[State].toInstance(new StatePreparation(stock))
    }
}
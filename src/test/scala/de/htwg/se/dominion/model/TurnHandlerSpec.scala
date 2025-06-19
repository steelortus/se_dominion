package de.htwg.se.dominion.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandler
import de.htwg.se.dominion.model.turnHandlerComponent.turnHandlerImplementation.TurnHandlerBuilder
import de.htwg.se.dominion.model._
import de.htwg.se.dominion.util._

class TurnHandlerSpec extends AnyWordSpec {

    "A TurnHandler" should {

        "be initialized with the correct number of players and turn" in {
            val builder = new TurnHandlerBuilder()
            val th = builder.setNumberOfPlayers(2).setTurn(0).getResult()
            th.numberOfPlayers should be(2)
            th.turn should be(0)
        }

        "return the next player's turn correctly" in {
            val builder = new TurnHandlerBuilder()
            val th = builder.setNumberOfPlayers(2).setTurn(0).getResult()
            val nextTh = th.nextTurn()
            nextTh.turn should be(1)
        }

        "wrap around to the first player after the last player" in {
            val builder = new TurnHandlerBuilder()
            val th = builder.setNumberOfPlayers(2).setTurn(2).getResult()
            val nextTh = th.nextTurn()
            nextTh.turn should be(3)
            th.getPlayer() should be(th.players(0))
        }

        "return how many turns have been played" in {
            val builder = new TurnHandlerBuilder()
            val th = builder.setNumberOfPlayers(2).setTurn(47).getResult()
            th.totalTurnCount() should be(47)
            val nextTh = th.nextTurn()
            nextTh.totalTurnCount() should be(48)
        }
    }
}
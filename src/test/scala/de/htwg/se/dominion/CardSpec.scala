package de.hwtg.se.dominion

import scalatest.matchers.should.Matchers._
import scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec {
    "A Card" should {
        val card = Card.Kupfer
        "have a getName" in {
            card.getName should be("Kupfer")
        }
        "have a getCost" in {
            card.getCost should be(0)
        }
        "have a getValue" in {
            card.getValue should be(1)
        }
        "have a getPoints" in {
            card.getPoints should be(0)
        }
        "have a getAmount" in {
            card.getAmount should be(50)
        }
    }
}
// filepath: src/test/scala/de/htwg/se/dominion/test_ConsoleColorSpec.scala.scala
package de.htwg.se.dominion.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.dominion.model.ConsoleColors.*

class ConsoleColorSpec extends AnyWordSpec {
    "ConsoleColors" should {
        val message = "TestMessage"

        "apply red color correctly" in {
            val redMessage = RED(message)
            redMessage should fullyMatch regex """\u001B\[31mTestMessage\u001B\[0m"""
        }

        "apply green color correctly" in {
            val greenMessage = GREEN(message)
            greenMessage should fullyMatch regex """\u001B\[32mTestMessage\u001B\[0m"""
        }

        "apply blue color correctly" in {
            val blueMessage = BLUE(message)
            blueMessage should fullyMatch regex """\u001B\[34mTestMessage\u001B\[0m"""
        }

        "apply yellow color correctly" in {
            val yellowMessage = YELLOW(message)
            yellowMessage should fullyMatch regex """\u001B\[33mTestMessage\u001B\[0m"""
        }

        "apply purple color correctly" in {
            val purpleMessage = PURPLE(message)
            purpleMessage should fullyMatch regex """\u001B\[35mTestMessage\u001B\[0m"""
        }

        "apply cyan color correctly" in {
            val cyanMessage = CYAN(message)
            cyanMessage should fullyMatch regex """\u001B\[36mTestMessage\u001B\[0m"""
        }

        "remove the color correctly" in {
            val clearedMessage = CLEARCOLOR(message)
            clearedMessage should fullyMatch regex """\u001B\[0mTestMessage\u001B\[0m"""
        }
    }
}
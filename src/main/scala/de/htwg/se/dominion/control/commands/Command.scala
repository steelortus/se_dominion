package de.htwg.se.dominion.control.commands

trait Command {
  def execute(): Unit
  def undo(): Unit
}

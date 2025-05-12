package de.htwg.se.dominion
package util

trait Observer {
    def update(e: Event): Unit
    def update(e: ErrorEvent): Unit
}

trait Observable {
    var subscribers: Vector[Observer] = Vector()
    def add(s: Observer) = subscribers = subscribers :+ s
    def remove(s: Observer) = subscribers = subscribers.filterNot(o => o == s)
    def notifyObservers(e: Event) = subscribers.foreach(o => o.update(e))
    def notifyObservers(e: ErrorEvent) = subscribers.foreach(o => o.update(e))
}

enum Event {
    case preparation
    case stockFull
    case cardAdded
    case cardRemoved
    case playing
}

enum ErrorEvent {
    case stockFull
    case couldNotAddCard
    case couldNotRemoveCard
    case cantStart
    case invalidCommand
}
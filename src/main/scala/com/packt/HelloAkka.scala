package com.packt

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by deepak on 20/3/17.
  */

case class WhoToGreet(who: String)

class Greeter extends Actor {
  override def receive = {
    case WhoToGreet(who) => println(s"hello $who")
  }
}

object HelloAkka {
  def main(args: Array[String]): Unit = {
    //create the "hello actor" actor system
    val system = ActorSystem("hello-akka")

    //create the greeter actor
    val greeter = system.actorOf(Props[Greeter], "greeter")

    //send whotogreet
    greeter ! WhoToGreet("deepak")
  }

}

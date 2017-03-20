package com.packt.supervision

import akka.actor.{Actor, ActorSystem, Props}
import com.packt.supervision.MusicController.Stop
import com.packt.supervision.MusicController.Play
import com.packt.supervision.MusicPlayer.{StartMusic, StopMusic}

/**
  * Created by deepak on 20/3/17.
  */
//music controller msg
object MusicController {

  sealed trait ControllerMsg

  case object Play extends ControllerMsg

  case object Stop extends ControllerMsg

  def props = Props[MusicController]

}

class MusicController extends Actor {
  def receive = {
    case Play => println("music started")
    case Stop => println("music stoped")
  }
}

//music player message
object MusicPlayer {

  sealed trait PlayMsg

  case object StopMusic extends PlayMsg

  case object StartMusic extends PlayMsg

}

class MusicPlayer extends Actor {
  override def receive = {
    case StartMusic =>
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! Play
    case StopMusic =>
      println("I dont want to stop music")
    case _ =>
      println("Unknown message")
  }
}


object ActorCreation extends App {

  //carete the creation actor system
  val system = ActorSystem("creation")

  //create the music player actor
  val player = system.actorOf(Props[MusicPlayer], "player")
  player ! StartMusic
}

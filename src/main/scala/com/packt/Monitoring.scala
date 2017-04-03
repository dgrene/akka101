package com.packt

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import kamon.Kamon

/**
  * Created by deepak on 23/3/17.
  */
class Ares(athena:ActorRef) extends Actor{
  override def preStart(): Unit = {
    context.watch(athena)
  }

  override def postStop(): Unit = {
    println("Ares Post stop...")
  }

  override def receive = {
    case Terminated =>
      context.stop(self)
  }
}
class Athena extends  Actor{
  override def receive = {
    case msg =>
      println(s"Athena received ${msg}")
      context.stop(self)
  }
}
object Monitoring extends App{
  Kamon.start()
  val system = ActorSystem("Monitoring")
  val athena = system.actorOf(Props[Athena],"Athena")
  athena ! Terminated

}

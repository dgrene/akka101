package com.packt

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}
import com.packt.Aphrodite.{RestartException, ResumeException, StopException}
import com.sun.org.apache.xml.internal.utils.StopParseException

import scala.concurrent.duration._

/**
  * Created by deepak on 22/3/17.
  */
class Aphrodite extends Actor {


  override def preStart() = {
    println("Aphodite prestart")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("aprodite restart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("post restart")
    super.postRestart(reason)
  }

  override def postStop(): Unit = {
    println("post stop")
  }

  override def receive = {
    case "Resume" => throw ResumeException
    case "Stop" => throw StopException
    case "Restart" => throw RestartException
    case _ => throw new Exception
  }
}

object Aphrodite {

  case object ResumeException extends Exception

  case object StopException extends Exception

  case object RestartException extends Exception

}

class Hera extends Actor {
  var childRef: ActorRef = _
  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 second) {
      case ResumeException => Resume
      case RestartException => Restart
      case StopException => Stop
      case _: Exception => Escalate
    }

  override def preStart(): Unit = {
    //create Aphrodite actor
    childRef = context.actorOf(Props[Aphrodite], "Aphrodite")
    Thread.sleep(100)
  }

  override def receive = {
    case msg => {
      println(s"Hera received ${msg}")
      childRef ! msg
      Thread.sleep(100)
    }
  }

}

object Supervison extends App {

  //create the supervison actor system
  val system = ActorSystem("supervison")

  //create hera actor
  val hera = system.actorOf(Props[Hera], "hera")
  hera ! "Stop"
  Thread.sleep(1000)
  println()
  system.terminate()

}

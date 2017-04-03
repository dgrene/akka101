/*
package com.packt


import akka.actor.{Actor, ActorRef}
import akka.util.Timeout
import concurrent.ExecutionContext
import com.packt.Checker.{BlackUser, CheckUser, WhiteUser}
import com.packt.Recorder.NewUser
import com.packt.Storage.AddUser

/**
  * Created by deepak on 20/3/17.
  */
case class User(userName: String)

object Recorder {

  sealed trait RecordingMsg

  //Recorder message
  case class NewUser(user: User) extends RecordingMsg

}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5 seconds)

  override def receive = {
    case NewUser(user) =>
      checker ! CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder : $user is in blacklist")
      }
  }
}

object Checker {

  sealed trait CheckerMsg

  //Checker message
  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckResponse

  //Checker Response message
  case class BlackUser(user: User) extends CheckerMsg

  case class WhiteUser(user: User) extends CheckerMsg

}

class Checker extends Actor {
  val blackList = List(User("adam", "adam@gmail.com"))

  override def receive = {
    case CheckUser(user) if blackList.contains(user) =>
      println(s"checker: $user in the blacklist")
      sender() ! BlackUser(user)
    case CheckUser(user) =>
      sender() ! WhiteUser(user)

  }
}

object Storage {

  sealed trait StorageMsg

  //Storage message
  case class AddUser(user: User) extends StorageMsg

}

class Storage extends Actor {
  override def receive = {
    case AddUser(user) =>
      println("add user")
  }
}

object TalkToActor extends App {

}
*/

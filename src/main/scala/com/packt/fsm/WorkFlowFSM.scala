package com.packt.fsm


import akka.actor.{ActorSystem, FSM, Props, Stash}


/**
  * Created by deepak on 10/4/17.
  */
object WorkFlowFSM {

  //FSM State
  sealed trait State

  case object Start extends State

  case object ReadyToProcess extends State

  case object Finish extends State

  //FSM Data
  sealed trait Data

  case object EmptyData extends Data

  //FSM Events
  sealed trait FSMEvents

  case object Process extends FSMEvents

  case object ProcessWfStep extends FSMEvents

  case object StartWF extends FSMEvents

  //Operation
  trait WorkFlowOperation
  object WorkFlowOperation{
    case object UpdateWorkFlowStatus extends WorkFlowOperation
    case object GetWorkFlowSteps extends WorkFlowOperation
    case object GetNextWorkFlowStep extends WorkFlowOperation
  }
  case class Operation(op:WorkFlowOperation,bmsMsg:String)


}

class WorkFlowFSM extends FSM[WorkFlowFSM.State, WorkFlowFSM.Data] with Stash {

  import WorkFlowFSM._
  //1. define Start With
  startWith(Finish, EmptyData)

  //2. define states
  when(Start) {
    case Event(Process, _) =>
      println("WorkFlow Ready to Process")
      goto(ReadyToProcess) using (EmptyData)
  }
  when(ReadyToProcess) {
    case Event(Operation(op,bmsMsg), _) =>
      println(s"WorkFlow Processing ${op} operation ")
      stay using (EmptyData)
  }
  when(Finish) {
    case Event(StartWF, _) =>
      println(s"WorkFlow Started")
      unstashAll()
      goto(Start) using (EmptyData)
    case Event(_, _) =>
      println(s"UnknownEvent")
      stash()
      stay using EmptyData
  }

  //3. initialize
  initialize()
}

object WFFSM extends App{
  import com.packt.fsm.WorkFlowFSM._
  val system = ActorSystem("FMS-WorkFlow")

  val workFlowFSM =system.actorOf(Props[WorkFlowFSM],"workFlow-fsm")

  workFlowFSM ! StartWF

  workFlowFSM ! Process


}
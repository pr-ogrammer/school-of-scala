import akka.actor.{Actor, Props}

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ruter on 06.10.16.
  */
object AskTell {

  class TellActor extends Actor {

    val recipient = context.actorOf(Props[ReceiveActor])

    def receive = {
      case "Start" =>
        recipient ! "Hello" // equivalent to recipient.tell("hello", self)

      case reply => println(reply)
    }
  }

  class AskActor extends Actor {

    val recipient = context.actorOf(Props[ReceiveActor])

    def receive = {
      case "Start" =>
        implicit val timeout = Timeout(3 seconds)
        val replyF = recipient ? "Hello" // equivalent to recipient.ask("Hello")
        replyF.onSuccess{
          case reply => println(reply)
        }
    }
  }

  class ReceiveActor extends Actor {

    def receive = {
      case "Hello" => sender ! "And Hello to you!"
    }
  }

}
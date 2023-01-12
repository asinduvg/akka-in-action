package oneactorisnoactor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.util.Random

object Worker {

  sealed trait Command

  final case class Parse(text: String, replyTo: ActorRef[Worker.Response]) extends Command

  sealed trait Response

  case object Done extends Response

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Parse(text, replyTo) =>
          fakeLengthyParsing(text)
          context.log.info(
            s"${context.self.path.name}: done")
          replyTo ! Worker.Done
          Behaviors.stopped
      }
    }

  private def fakeLengthyParsing(text: String) = {
    val endTime = System.currentTimeMillis() + Random.between(2000, 4000)
    while (endTime > System.currentTimeMillis()) {}
  }

}

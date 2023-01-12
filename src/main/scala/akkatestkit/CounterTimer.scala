package akkatestkit

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt

object CounterTimer {

  sealed trait Command

  final case object Increase extends Command

  final case class Pause(seconds: Int) extends Command

  private[fishing] final case object Resume extends Command

  def apply(): Behavior[Command] =
    resume(0)

  def resume(count: Int): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      Behaviors.withTimers { timers =>
        message match {
          case Pause(t) =>
            timers.startSingleTimer(Resume, t.second)
            pause(count)
        }
      }
    }

  def pause(count: Int): Behavior[Command] = {
    Behaviors.receive { (context, message) =>
      message match {
        case Resume =>
          context.log.info(s"resuming")
          resume(count)
      }
    }
  }

}

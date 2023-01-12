import akka.actor.testkit.typed.scaladsl.{FishingOutcomes, ScalaTestWithActorTestKit}
import akka.actor.typed.scaladsl.Behaviors
import akkatestkit.CounterTimer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration.DurationInt

class FishingSpec extends ScalaTestWithActorTestKit
  with AnyWordSpecLike
  with Matchers {

  "An automated resuming counter" must {
    "receive a resume after a pause" in {
      val probe = createTestProbe[CounterTimer.Command]()
      val counterMonitored = Behaviors.monitor(probe.ref, CounterTimer())
      val counter = spawn(counterMonitored)

      counter ! CounterTimer.Pause(1)
      probe.fishForMessage(3.seconds) {
        case CounterTimer.Increase =>
          FishingOutcomes.continueAndIgnore
        case CounterTimer.Pause(_) =>
          FishingOutcomes.continueAndIgnore
        case CounterTimer.Resume =>
          FishingOutcomes.complete
      }
    }
  }
}
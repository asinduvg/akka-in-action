import akka.actor.testkit.typed.CapturedLogEvent
import akka.actor.testkit.typed.Effect.{NoEffects, Spawned}
import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akkatestkit.{SimplifiedManager, SimplifiedWorker}
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpec
import org.slf4j.event.Level

class SyncTestingSpec extends AnyWordSpec with Matchers {

  "Typed actor synchronous testing" must {

    "spawning takes place" in {
      val testKit = BehaviorTestKit(SimplifiedManager()) // this is not a real actor system, stubbed context
      testKit.expectEffect(NoEffects)
      testKit.run(SimplifiedManager.CreateChild("adan"))
      testKit.expectEffect(Spawned(SimplifiedWorker(), "adan"))
    }

    "actor gets forwarded message from manager" in {
      val testKit = BehaviorTestKit(SimplifiedManager())
      val probe = TestInbox[String]()
      testKit.run(SimplifiedManager.Forward("hello", probe.ref))
      probe.expectMessage("hello")
    }

//    "record the log" in {
//      val testKit = BehaviorTestKit(SimplifiedManager())
//      testKit.run(SimplifiedManager.Log)
//      testKit.logEntries() shouldBe Seq(
//        CapturedLogEvent(Level.INFO, "it's done")
//      )
//    }

  }

}

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.scaladsl.Behaviors
import akkatestkit.SimplifiedManager
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AsyncForwardSpec extends AnyWordSpec with BeforeAndAfterAll with Matchers {

  val testKit = ActorTestKit()

  "a Simplified Manager" must {
    "actor gets forwarded message from manager" in {
      val manager = testKit.spawn(SimplifiedManager())
      val probe = testKit.createTestProbe[String]()
      manager ! SimplifiedManager.Forward("message-to-parse", probe.ref)
      probe.expectMessage("message-to-parse")
    }
  }

  "a monitor" must {
    "intercept the messages" in {
      val probe = testKit.createTestProbe[String]
      val behaviorUnderTest = Behaviors.receiveMessage[String] { _ =>
        Behaviors.ignore
      }
      val behaviorMonitored = Behaviors.monitor(probe.ref, behaviorUnderTest)
      val actor = testKit.spawn(behaviorMonitored)

      actor ! "checking"
      probe.expectMessage("checking")
    }
  }

  override def afterAll(): Unit = testKit.shutdownTestKit()

}

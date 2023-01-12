package oneactorisnoactor

import akka.actor.typed.ActorSystem

object ErrorKernelApp extends App {

  val guardian: ActorSystem[Guardian.Command] =
    ActorSystem(Guardian(), "error-kernel")
  guardian ! Guardian.Start(List("-one-", "--two--"))

}

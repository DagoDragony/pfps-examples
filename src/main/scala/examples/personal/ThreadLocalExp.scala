package examples.personal

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

object ThreadLocalExp {

  def main(args: Array[String]): Unit = {
    val threadLocal1 = new ThreadLocal[String]
    val inheritableThreadLocal = new InheritableThreadLocal[String]

    val f1: Future[Unit] = Future {
      test("f1", threadLocal1)
//      println(("differentThreadLocal: ", threadLocal2.get()))
//      inheritableThreadLocal.set("Here is your inheritance")
//      println(s"Parent: I said: ${inheritableThreadLocal.get()}")


      val tst = new Thread{
        println(s"My father said: ${inheritableThreadLocal.get()}")
        inheritableThreadLocal.set("Hi papa")
        println(s"I said: ${inheritableThreadLocal.get()}")
      }
      tst.run()
      tst.join()
      println(s"Parent: I heard: ${inheritableThreadLocal.get()}")
    }
    val f2: Future[Unit] = Future {
      test("f2", threadLocal1)
//      println(("differentThreadLocal: ", threadLocal2.get()))
    }

    val result = for {
      _ <- f1
      _ <- f2
    } yield ()

    Await.result(result, 5 seconds)

    ()
  }

  def test(name: String, tl: ThreadLocal[String]): Unit = {
    tl.set(name)
    println(tl.get())
  }
}

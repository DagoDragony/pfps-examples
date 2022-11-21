package examples.personal

import cats.Eval
import cats.effect.{IO, IOApp}
import cats.data.{IndexedStateT, State, StateT}

object StateExperiments extends IOApp.Simple {
  // experiment with lifted value to state
  // would it always return the same value?

  // static
  val a0 = State[Int, String](s => (s + 1, s.toString))
  val a1 = State.pure[Int, String]("test")
  val a2: State[String, String] = State.get[String]
  val a3: State[String, Unit] = State.set[String]("state")
  // modify state
  val a4: State[String, Unit] = State.modify[String](s => "state")
  // modify value
  val a5: State[String, Int] = State.inspect[String, Int](s => s.toInt)


  val (state, result) = a0.run(10).value
  val justTheState: Int = a0.runS(10).value
  val justTheResult: String = a0.runA(10).value

//  val i   = IO.pure(10)
//  val sth = StateT.liftK[IO, Int](i)

  final case class Seed(long: Long) {
    def next = Seed(long * 6364136223846793005L + 1442695040888963407L)
  }

  val nextLong: State[Seed, Long] = State(seed => (seed.next, seed.long))
  def nextBoolean(seed: Seed): (Seed, Boolean) = (seed.next, seed.long >= 0L)

  val result: IndexedStateT[Eval, Seed, Seed, (Long, Long, Long)] = for {
    r1 <- nextLong
    r2 <- nextLong
    r3 <- nextLong
  } yield (r1, r2, r3)


  // how to finish program with random numbers?
  // what final program should look like?
  // how to lift State to StateT
  val sth = StateT.liftK[IO, Seed](nextLong)
  override def run = for {
    id1 <- StateT.liftK[IO, Seed](nextLong)
    id2 <- nextLong
    _ <- nextLong.flatMap(IO.println())
  } yield ()
}

package examples.personal

import cats.effect.{IO, IOApp}
import cats.data.{State, StateT}

object StateExperiments extends IOApp.Simple {
  // experiment with lifted value to state
  // would it always return the same value?

  final case class Seed(long: Long) {
    def next = Seed(long * 6364136223846793005L + 1442695040888963407L)
  }

  val nextLong: State[Seed, Long] = State(seed => (seed.next, seed.long))

  def nextBoolean(seed: Seed): (Seed, Boolean) = (seed.next, seed.long >= 0L)

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

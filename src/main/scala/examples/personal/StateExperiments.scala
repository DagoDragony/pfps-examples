package examples.personal

import cats.Eval
import cats.syntax._
import cats.effect.{IO, IOApp}
import cats.data.{IndexedStateT, Reader, ReaderT, State, StateT}

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

  val result2 = for {
    r1 <- nextLong
    r2 <- nextLong
    r3 <- nextLong
  } yield (r1, r2, r3)

  val randomNumbers = for {
    id1 <- nextLong
    id2 <- nextLong
    id3 <- nextLong
  } yield (id1, id2, id3)

  def printlnIO(string: String) = IO.delay(println(string))
  def readlnIO() = IO.delay(scala.io.StdIn.readLine())


  val getSth: Reader[MyObject, String] = Reader(_.value1)
  def test1: IO[Unit] = for {
    _ <- printlnIO("My program started")
    answer <- readlnIO()
    _ <- printlnIO("Your random long:")
    // TODO: should it be runA.value in IO pure
    randomLong <- nextLong
    _ <- StateT.liftF[IO, Seed, Unit](printlnIO(randomLong.toString))
    // TODO: again delay?
    someValue <- IO.delay(getSth.lift.run(MyObject("t1", "t2")))
  } yield ()

  trait Console[F[_]] {
    def println(line: String): F[Unit]
    def readln(): F[String]
  }
  object Console {
    def apply[F[_]](implicit console: Console[F]) = console
  }


  override def run = test1

  case class MyObject(value1: String, value2: String) {}

}

object StateStack extends IOApp.Simple {
  type Stack = List[Int]

  def pop(s0: Stack): (Stack, Int) = s0 match {
    case x :: xs => (xs, x)
    case Nil => sys.error("stack is empty")
  }
  override def run: IO[Unit] = ???
}

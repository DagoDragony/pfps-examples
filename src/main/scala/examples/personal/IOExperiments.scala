package examples.personal

import cats.effect.{IO, IOApp}


object IOExperiments extends IOApp.Simple {

  val testP = for {
    _ <- IO.pure(println("Hello"))
    a <- IO.delay(scala.io.StdIn.readLine())
    _ <- IO.pure(println("World"))
  } yield()

  override def run: IO[Unit] = testP
}

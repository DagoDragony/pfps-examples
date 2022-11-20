package examples.personal

import cats.data.EitherT

import scala.concurrent.Future

object TTypes {
//  import scala.concurrent.ExecutionContext.Implicits.global
//  val a: Future[Either[String, Int]] = ???
//  def b(value: Int): Future[Either[String, Int]] = ???
//
//  // doesn't work as a is Either, not a
//  //  for {
//  //    a1 <- a
//  //    b1 <- b(a1)
//  //  } yield ()
//
//  val t2: EitherT[Future, String, Unit] = for {
//    a1 <- EitherT(a)
//    b1 <- EitherT(b(a1))
//  } yield ()
}

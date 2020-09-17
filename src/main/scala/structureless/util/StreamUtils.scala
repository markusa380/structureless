package structureless.util

import org.mongodb.scala.Observable
import cats.effect.ConcurrentEffect

import fs2.Stream
import fs2.interop.reactivestreams._

import org.reactivestreams._

object StreamUtils {

  implicit class StreamFromMongoObservable[D](o: Observable[D]) {
    def toStream[F[_]: ConcurrentEffect]: Stream[F, D] =
      new Publisher[D] {
        override def subscribe(subscriber: Subscriber[_ >: D]): Unit = o.subscribe(
          n => subscriber.onNext(n),
          e => subscriber.onError(e),
          () => subscriber.onComplete()
        )
      }.toStream[F]
  }
}

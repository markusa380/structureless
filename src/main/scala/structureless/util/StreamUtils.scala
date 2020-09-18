package structureless.util

import org.mongodb.scala.{Observable => MObservable, Subscription => MSubscription, Observer => MObserver}
import cats.effect.ConcurrentEffect

import fs2.Stream
import fs2.interop.reactivestreams._

import org.reactivestreams._
import org.mongodb.scala.Observer

object StreamUtils {

  implicit class StreamFromMongoObservable[D](mObservable: MObservable[D]) {
    def toStream[F[_]: ConcurrentEffect]: Stream[F, D] =
      new Publisher[D] {

        override def subscribe(subscriber: Subscriber[_ >: D]): Unit = mObservable.subscribe {
          new MObserver[D] {
            override def onSubscribe(mSubscription: MSubscription): Unit = {
              subscriber.onSubscribe(
                new Subscription {
                  override def request(req: Long): Unit = mSubscription.request(req)
                  override def cancel(): Unit = mSubscription.unsubscribe()
                }
              )
            }
            override def onNext(result: D): Unit = subscriber.onNext(result)
            override def onError(e: Throwable): Unit = subscriber.onError(e)
            override def onComplete(): Unit = subscriber.onComplete()
          }
        }
      }.toStream[F]
  }
}

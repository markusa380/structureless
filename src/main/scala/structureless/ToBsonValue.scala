package structureless

import org.bson.BsonValue

trait ToBsonValue[A] {
  def apply(a: A): BsonValue
}

object ToBsonValue {
  def apply[A](implicit t: ToBsonValue[A]) = t
}

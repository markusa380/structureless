package structureless

import org.bson.BsonValue

trait FromBsonValue[A] {
  def apply(bson: BsonValue): ParseResult[A]
}

object FromBsonValue {
  def apply[A](implicit f: FromBsonValue[A]) = f
}

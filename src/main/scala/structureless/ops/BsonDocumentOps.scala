package structureless.ops

import structureless._
import org.bson.BsonDocument

trait BsonDocumentOps {

  implicit class ToBsonDocumentOps[A: ToBsonDocument](a: A) {
    def toBsonDocument: BsonDocument = ToBsonDocument[A].apply(a)
  }
}

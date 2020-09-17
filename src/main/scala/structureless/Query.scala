package structureless

import structureless.FromBsonDocument

import shapeless.HList
import shapeless.ops.record._

import org.bson.conversions.Bson
import org.mongodb.scala.model.Filters

import scala.util.matching.Regex
import org.bson.types.ObjectId
import org.mongodb.scala.MongoCollection
import org.bson.BsonDocument

import fs2.Stream
import fs2.interop.reactivestreams._
import cats.effect.Effect
import org.mongodb.scala.Observer

trait Query[D] { self =>
  def compile(implicit fromBsonDocument: FromBsonDocument[D]): CompiledQuery[D]

  /**
   * Constructs a composite `and` query from this query and another query
   *
   * @param that The other query object
   * @return A composite `and` `Query` object
   */
  def and(that: Query[D]): Query[D] = new Query[D] {
    def compile(implicit fromBsonDocument: FromBsonDocument[D]): CompiledQuery[D] = new CompiledQuery[D] {
      val fromBsonDocument: FromBsonDocument[D] = fromBsonDocument
      val bson = Filters.and(self.compile(fromBsonDocument).bson, that.compile(fromBsonDocument).bson)
    }
  }
}

trait CompiledQuery[D] {
  val fromBsonDocument: FromBsonDocument[D]
  val bson: Bson

  def runOn[F[_]: Effect](col: MongoCollection[BsonDocument]): Stream[F, D] = for {
    x <- ???
  } yield x

}

object Query {}

/*
trait Query[D] { self =>

  /**
 * Build the query
 *
 * @return
 */
  def build: Bson

  /**
 * Constructs a composite `and` query from this query and another query
 *
 * @param that The other query object
 * @return A composite `and` `Query` object
 */
  def and(that: Query[D]): Query[D] = new Query[D] {
    def build: Bson = {
      val thisBuilt = self.build
      val thatBuilt = that.build
      Filters.and(thisBuilt, thatBuilt)
    }
  }
}

object Query {

  def apply[D <: HList] = new QueryBuilder[D]

  class QueryBuilder[D <: HList] {

    /**
 * For safety we need some kind of evidence that a field in question
 * is actually a field in the given record D, with value of type String.
 */
    type IsStringField[K] = Selector.Aux[D, K, String]

    /**
 * Constructs a search query.
 *
 * @param value The value that should be searched for
 * @return A `Query` object that builds the search query
 */
    def search[K <: String: ValueOf: IsStringField](value: String) = new Query[D] {

      val values = value.toLowerCase.split("\\s+")

      def findAnywhere(regex: String) = "(?=.*" + regex + ")"

      def build: Bson = {
        val key = valueOf[K]

        Filters.regex(
          key,
          "^" + values.map(Regex.quote).map(findAnywhere).mkString,
          options = "i"
        )
      }
    }

    def equals[K <: String: ValueOf: IsStringField](value: String) = new Query[D] {
      def build: Bson = {
        val key = valueOf[K]

        Filters.eq(key, value)
      }
    }

    def idEquals(value: String) = new Query[D] {
      def build: Bson = {
        Filters.eq("_id", new ObjectId(value))
      }
    }
  }
}
 */

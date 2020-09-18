package structureless

import org.scalatest._
import flatspec._
import matchers._

import shapeless.record._
import shapeless.ops.record._

import org.bson.conversions.Bson
import org.mongodb.scala.model.Filters
import org.bson.types.ObjectId

class QuerySpec extends AnyFlatSpec with should.Matchers {

  behavior.of("A QueryBuilder")

  it should "create a valid equals query on a String field" in new Wiring {
    val expectedResult = Filters.eq(
      fieldName = "username",
      value = "bob"
    )

    Query[TestRecordA]
      .equals["username"]("bob")
      .compile
      .bson
      .shouldEqual(expectedResult)
  }

  // TODO - Seems like you cannot compare two ObjectId fields
  ignore should "create a valid equals query on Id field" in new Wiring {
    val expectedResult = Filters.eq(
      fieldName = "_id",
      value = testObjectId
    )

    Query[TestRecordA]
      .idEquals(testObjectId)
      .compile
      .bson
      .shouldEqual(expectedResult)
  }

  it should "create a valid search query on a String field" in new Wiring {

    val expectedResult = Filters.regex(
      fieldName = "username",
      pattern = "^(?=.*\\Qbob\\E)",
      options = "i"
    )

    Query[TestRecordA]
      .search["username"]("bob")
      .compile
      .bson
      .shouldEqual(expectedResult)
  }

  trait Wiring {
    type TestRecordA = Record.`"userId" -> String, "username" -> String`.T

    val testRecordAIdUpdater = Updater[TestRecordA, Id]
    type IdTestRecord = testRecordAIdUpdater.Out

    type TestRecordB = Record.`"userId" -> String, "age" -> Int`.T

    val testObjectId = new ObjectId().toHexString()
  }
}

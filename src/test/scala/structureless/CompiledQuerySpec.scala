package structureless

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.time.{Seconds, Span}
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import com.dimafeng.testcontainers.{Container, ForAllTestContainer, MongoDBContainer}
import com.dimafeng.testcontainers.GenericContainer.DockerImage

import org.mongodb.scala.{MongoClient, MongoDatabase}
import org.bson.BsonDocument

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.Await

import java.{util => ju}

import shapeless._
import shapeless.record._
import shapeless.ops.record._
import shapeless.syntax.singleton._

import structureless.implicits._

import cats.effect._

import scala.concurrent.ExecutionContext

class CompiledQuerySpec extends AnyFlatSpec with Matchers with ForAllTestContainer with TimeLimitedTests {

  val timeLimit = Span(60, Seconds)

  val mongoDb = MongoDBContainer("mongo:4.0.10")

  override def container: Container = mongoDb

  "The test suite" should "start a container successfully" in {
    val replicaSetUrl = mongoDb.replicaSetUrl
    println("The URL of the test MongoDB is: " + replicaSetUrl)
    replicaSetUrl shouldNot be(empty)
  }

  behavior.of("A CompiledQuery")

  it should "stream out all queried items from a collection" in new Wiring {

    val testRecords = Seq(
      ("name" ->> "Bob") :: ("age" ->> 12) :: HNil,
      ("name" ->> "Jim") :: ("age" ->> 14) :: HNil,
      ("name" ->> "Bob") :: ("age" ->> 11) :: HNil,
      ("name" ->> "Marc") :: ("age" ->> 15) :: HNil
    )

    val query = Query[TestRecord].equals["name"]("Bob")

    val expectedRecords = Seq(
      ("name" ->> "Bob") :: ("age" ->> 12) :: HNil,
      ("name" ->> "Bob") :: ("age" ->> 11) :: HNil
    )

    testRecords.foreach(doc => insertTestDocument(doc))

    val resultList: List[TestRecord] = query.compile
      .runOn[IO](colletion)
      .compile
      .toList
      .unsafeRunSync()

    resultList shouldEqual expectedRecords
  }

  trait Wiring {

    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    val url = mongoDb.replicaSetUrl
    val client = MongoClient(url)
    val databaseName = "test"
    val database = client.getDatabase(databaseName)
    val collectionName = "collection_" + ju.UUID.randomUUID().toString
    val colletion = database.getCollection[BsonDocument](collectionName)

    type TestRecord = Record.`"name" -> String, "age" -> Int`.T

    def insertTestDocument[D <: HList](document: D)(implicit toBsonDoc: ToBsonDocument[D]) =
      Await.result(
        colletion
          .insertOne(toBsonDoc(document))
          .toFuture,
        1.second
      )
  }
}

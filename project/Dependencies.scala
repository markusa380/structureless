import sbt._

object Dependencies {

  lazy val catsVersion = "2.1.1"
  lazy val catsEffectVersion = "2.1.0"
  lazy val shapelessVersion = "2.3.3"
  lazy val fs2Version = "2.2.1"
  lazy val mongoDriverVersion = "2.7.0"
  lazy val scalaTestVersion = "3.1.1"
  lazy val testcontainersVersion = "0.38.3"

  lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion
  lazy val shapeless = "com.chuusai" %% "shapeless" % shapelessVersion
  lazy val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
  lazy val fs2React = "co.fs2" %% "fs2-reactive-streams" % fs2Version
  lazy val mongoDriver = "org.mongodb.scala" %% "mongo-scala-driver" % mongoDriverVersion
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  lazy val testcontainers = "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersVersion % "test"
  lazy val testcontainersScalaTest = "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersVersion % "test"
  lazy val testcontainersMongoDb = "com.dimafeng" %% "testcontainers-scala-mongodb" % testcontainersVersion % "test"

}

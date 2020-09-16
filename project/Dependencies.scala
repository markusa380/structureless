import sbt._

object Dependencies {

  lazy val fs2Version = "2.2.1"
  lazy val mongoDriverVersion = "2.7.0"

  lazy val cats = "org.typelevel" %% "cats-core" % "2.1.1"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "2.1.0"

  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % "test"

  lazy val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
  lazy val fs2React = "co.fs2" %% "fs2-reactive-streams" % fs2Version

  lazy val mongoDriver = "org.mongodb.scala" %% "mongo-scala-driver" % mongoDriverVersion

  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.12.3"
}

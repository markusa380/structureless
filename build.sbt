import Dependencies._

ThisBuild / organization := "com.github.markusa380"
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / resolvers += "jitpack".at("https://jitpack.io")

lazy val root = (project in file("."))
  .settings(
    name := "structureless",
    libraryDependencies := Seq(
      shapeless,
      cats,
      catsEffect,
      mongoDriver
    )
  )

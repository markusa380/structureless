package object structureless {

  import shapeless.record._
  import shapeless.labelled._

  type Id = FieldType["_id", String]
  type ParseResult[A] = Either[ParseFailure, A]
}

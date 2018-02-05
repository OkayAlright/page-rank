package rankerTests

object testProperties {
  val simpleGraphPath = "./src/test/scala/simpleSpecParserTests/resources/exampleGraph.txt"

  val exampleGraph1 = List(("url_1", "url_4"),
    ("url_2", "url_1"),
    ("url_3", "url_2"),
    ("url_3", "url_1"),
    ("url_4", "url_3"),
    ("url_4", "url_1"))

  val exampleGraph2 = List(("url_2", "url_1"),
    ("url_3", "url_1"),
    ("url_3", "url_2"),
    ("url_3", "url_1"))
}

package simpleSpecParserTests

object testProperties {
  val simpleGraphPath = "./src/test/scala/simpleSpecParserTests/resources/exampleGraph.txt"


  val expectedGraph = List(("url_1", "url_4"),
    ("url_2", "url_1"),
    ("url_3", "url_2"),
    ("url_3", "url_1"),
    ("url_4", "url_3"),
    ("url_4", "url_1"))
}

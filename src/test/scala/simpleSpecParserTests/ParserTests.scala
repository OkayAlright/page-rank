package simpleSpecParserTests

import java.io.File
import java.nio.file.{FileSystems, Path}

import org.scalatest._

class ParserTests extends FlatSpec{
  "Parsers.simpleSpecParser" should "return a List[(String, String)] of specific length and content from example graph" in {
    print(new File(".").getAbsolutePath())
    val parser = new Parsers.simpleSpecParser()
    val specFile: Path = FileSystems.getDefault.getPath(testProperties.simpleGraphPath)
    val expectedGraph = List(("url_1", "url_4"),
                             ("url_2", "url_1"),
                             ("url_3", "url_2"),
                             ("url_3", "url_1"),
                             ("url_4", "url_3"),
                             ("url_4", "url_1"))
    assert(parser.parserToyGraph(specFile) == expectedGraph)
  }
}

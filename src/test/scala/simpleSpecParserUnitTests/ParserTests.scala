package simpleSpecParserTests

import java.io.File
import java.nio.file.{FileSystems, Path}

import org.scalatest._

import scala.io.Source

class ParserTests extends FlatSpec{
  "Parsers.simpleSpecParser" should "return a List[(String, String)] of specific length and content from example graph" in {
    println(new File(".").getAbsolutePath())
    val parser = new Parsers.simpleSpecParser()
    val specFile: Path = FileSystems.getDefault.getPath(testProperties.simpleGraphPath)

    assert(parser.parserToyGraph(specFile) == testProperties.expectedGraph, "Make sure it is parsing correctly.")
  }

  "Parsers.simpleSpecParser" should "produce identical graphs graphs from all 3 method signatures" in {
    val parser = new Parsers.simpleSpecParser()
    val specFile: Path = FileSystems.getDefault.getPath(testProperties.simpleGraphPath)
    val specAsLines = Source.fromFile(specFile.toString).getLines.toList
    val specAsString = specAsLines.mkString("\n")

    val pairsFromPath = parser.parserToyGraph(specFile)
    val pairsFromString = parser.parserToyGraph(specAsLines)
    val pairsFromList = parser.parserToyGraph(specAsLines)

    assert(pairsFromList == pairsFromPath && pairsFromPath == pairsFromString,
      "Check to make sure all signatures of the simpleSpecParser method return the same result in input is from the same source")
  }


}

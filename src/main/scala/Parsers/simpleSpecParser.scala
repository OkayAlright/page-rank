package Parsers

import java.nio.file.Path
import java.util.logging.Logger
import scala.io.Source


class simpleSpecParser {

  val sc = sparkWrapper.sparkContext.sc
  val logger = Logger.getLogger(this.getClass().toString)

  def parserToyGraph(toyGraphSpec : List[String]): List[(String, String)] = {
    logger.info("Parsing spec: "+toyGraphSpec)
    val parallelSpec = sc.parallelize(toyGraphSpec)
    parallelSpec.map(line => {
         val splitLine: Array[String] = line.split(" ")
                                            .filter(piece => !piece.isEmpty)
         (splitLine(0), splitLine(1))
    }).collect()
      .toList
  }

  def parserToyGraph(toyGraphSpec : String): List[(String, String)] = {
    logger.info("Parsing simple-spec String...")
    parserToyGraph(toyGraphSpec.split("\n").toList)
  }

  def parserToyGraph(toyGraphSpec : Path): List[(String, String)] = {
    logger.info("Parsing simple-spec file...")
    parserToyGraph(Source.fromFile(toyGraphSpec.toString).getLines.toList)
  }
}

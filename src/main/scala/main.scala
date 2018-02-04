import java.nio.file.{FileSystems, Path}
import java.util.logging.Logger

import Parsers.simpleSpecParser
import htmlSourceUtilities.Crawler
import org.apache.spark.rdd.RDD
import sparkRanker.Ranker

object SimplePageRanker{

  val logger = Logger.getLogger(this.getClass().toString)

  def main(args: Array[String]): Unit = {
    val crawler: Crawler = new htmlSourceUtilities.Crawler()
    val ranker: Ranker = new sparkRanker.Ranker()
    val parser: simpleSpecParser = new Parsers.simpleSpecParser()

    val config: Map[String, String] = parseArgs(args)
    var ranking: List[(String, Double)] = List.empty

    if(config.contains("useToyNotation")){
      val specFile: Path = FileSystems.getDefault.getPath(config("uri"))
      val parsedSpec: List[(String, String)] = parser.parserToyGraph(specFile)
      ranking = ranker.rankLinkCollection(parsedSpec)
    } else {
      var depth: Integer = 3
      if(config.contains("depth")){
        try {
          depth = config("depth").toInt
        } catch {
          case _: Throwable => {
            logger.info("depth arg not parsable to integer, using default value of 3")
          }
        }
      }
      val linkDiagram: RDD[(String, String)] = crawler.crawler(config("uri"), depth)
      ranking = ranker.rankLinkCollection(linkDiagram.collect.toList)
    }
    sparkWrapper.sparkContext.sc.stop()
    displayRanking(ranking)
  }

  def displayRanking(ranks: List[(String, Double)]): Unit = {
    ranks.foreach(pair => {
      val url: String = pair._1
      val rank: Double = pair._2
      println(s"Page: $url has rank $rank")
    })
  }


  def parseArgs(args: Array[String]): Map[String, String] = {
    val usage: String = "Usage: $ pageRanker [--useToyNotation] (fullURL |filePath)"

    if(args.length == 0){
      println("No arguments supplied...\n"+usage)
      System.exit(1)
    }

    def consumeAndSetArgs(toBeProcessed: List[String], config: Map[String, String]): Option[Map[String, String]] = {
      toBeProcessed match {
        case Nil => Option(config)
        case "--useToyNotation" :: tail => consumeAndSetArgs(tail, Map("useToyNotation" -> "True")++config)
        case "--depth" :: num :: tail => consumeAndSetArgs(tail, Map("depth" -> num))
        case uri :: Nil => consumeAndSetArgs(toBeProcessed.tail, Map("uri" -> uri)++config)
        case garbage :: tail => {
          println("Invalid argument " + garbage + "\n"+usage)
          None
        }
      }
    }
    val config: Option[Map[String, String]] = consumeAndSetArgs(args.toList, Map.empty)

    if(!config.isDefined){
      System.exit(1)
    }
    config.getOrElse(Map.empty)
  }
}
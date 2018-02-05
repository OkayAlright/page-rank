import java.io._
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
    var output: String = ""

    if(config.contains("useToyNotation")){
      logger.info("Processing Toy Notation...")
      val specFile: Path = FileSystems.getDefault.getPath(config("uri"))
      val parsedSpec: List[(String, String)] = parser.parserToyGraph(specFile)

      ranking = ranker.rankLinkCollection(parsedSpec)
      output = ranking.map(pair => "Page: "+pair._1+" Value: "+pair._2).mkString("\n")

    } else {
      logger.info("Processing a real crawl...")
      var depth: Integer = 3

      try {
        depth = config("depth").toInt
      } catch {
        case _: Throwable => {
          logger.info("depth arg not parsable to integer, using default value of 3")
        }
      }

      val linkDiagram: RDD[(String, String)] = crawler.crawler(config("uri"), depth)

      ranking = ranker.rankLinkCollection(linkDiagram.collect.toList)
      output = ranking.map(pair => "Page: "+pair._1+" Value: "+pair._2).mkString("\n")
    }

    if(config.contains("outputDest")) {
      logger.info("Attempting to write out to "+config("outputDest"))
      try {

        val file: File = new File(config("outputDest"))
        file.createNewFile()
        val outputFile: PrintWriter = new PrintWriter(file)

        outputFile.write(output)
        outputFile.close()

      } catch {
        case e: Throwable => {
          logger.warning("Issue opening file. Will continue without writing out to file...")
        }
      }
    } else {
      logger.warning("No output destination set, skipping output...")
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

  def displayUsage(): Unit ={
    val usage: String = "Usage: $ pageRanker [--outputDest <path>] ((--depth <num> fullURL) | --useToyNotation filePath)"
    println(usage)
  }

  def parseArgs(args: Array[String]): Map[String, String] = {

    if(args.length == 0){
      println("No arguments supplied...\n")
      displayUsage()
      System.exit(1)
    }

    def consumeAndSetArgs(toBeProcessed: List[String], config: Map[String, String]): Option[Map[String, String]] = {
      toBeProcessed match {
        case Nil => Option(config)
        case "--useToyNotation" :: tail => consumeAndSetArgs(tail, Map("useToyNotation" -> "True")++config)
        case "--outputDest" :: path :: tail => consumeAndSetArgs(tail, Map("outputDest" -> path)++config)
        case "--depth" :: num :: tail => consumeAndSetArgs(tail, Map("depth" -> num)++config)
        case uri :: Nil => consumeAndSetArgs(toBeProcessed.tail, Map("uri" -> uri)++config)
        case garbage :: tail => {
          println("Invalid argument " + garbage)
          displayUsage()
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
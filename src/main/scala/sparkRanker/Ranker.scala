package sparkRanker

import java.util.logging.Logger
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class Ranker {

  val sc: SparkContext = sparkWrapper.sparkContext.sc
  val logger: Logger = Logger.getLogger(this.getClass().toString)

  def settleProbabilities(pageRanks: RDD[(String, Double)],
                          linkPairings: RDD[(String, Iterable[String])],
                          settlingIterations: Integer): RDD[(String, Double)] = {
    if(settlingIterations <= 0){
      pageRanks.sortBy(_._2, false)
    } else {
      logger.info("Settling probabilities on iteration "+settlingIterations)
      val contributions: RDD[(String, Double)] = linkPairings.join(pageRanks).values.flatMap{ case (urls, rank) =>
        val size = urls.size
        urls.map(url => (url, rank / size))
      }.filter(item => item._1 != "")

      settleProbabilities(contributions.reduceByKey(_ + _).mapValues(0.15 + 0.85 * _), linkPairings, settlingIterations - 1)
    }
  }

  def dedupLinkDiagram(listToDedup: List[(String, String)]): List[(String, String)] = {
    listToDedup.toSet.toList
  }

  def rankLinkCollection(linkDiagram: List[(String, String)], settlingIterations: Integer = 10): List[(String, Double)] = {
    logger.info("Ranking pages...")
    val linkDiagramAsRDD: RDD[(String, Iterable[String])] = sc.parallelize(dedupLinkDiagram(linkDiagram)).distinct().groupByKey()
    val ranks = linkDiagramAsRDD.mapValues(link => 1.0)
    settleProbabilities(ranks, linkDiagramAsRDD, settlingIterations).collect().toList
  }
}

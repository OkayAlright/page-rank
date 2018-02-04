package htmlSourceUtilities

import java.util.logging.Logger

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class Crawler {

  val sc: SparkContext = sparkWrapper.sparkContext.sc
  val logger = Logger.getLogger(this.getClass().toString)

  def crawler(link: String, depth: Integer): RDD[(String, String)] = {
    logger.info("Depth set to "+depth)
    crawl(List(link), depth)
  }

  def crawl(links: List[String], depth: Integer): RDD[(String, String)] = {
    if(depth <= 0){
      sc.parallelize(links).map(link => {
        (link, link)
      })
    } else {
      val allLinks: RDD[(String, String)] = sc.parallelize(links)
                                      .map(link => (link, pageGrabber.GetAllLinksFromURL(link)))
                                      .flatMap(nodeAndLeaves => nodeAndLeaves._2.map(piece => (nodeAndLeaves._1, piece)))
      val selfRef: RDD[(String, String)] = sc.parallelize(links)
                                             .map(link => (link, link))
      allLinks ++ crawl(allLinks.map(pair => pair._2).collect().toList, depth - 1) ++ selfRef
    }
  }
}

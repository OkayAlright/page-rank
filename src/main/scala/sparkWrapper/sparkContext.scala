package sparkWrapper

import org.apache.spark.{SparkConf, SparkContext}

object sparkContext {

  val conf = new SparkConf().setAppName("ranker").setMaster("local[2]")
  val sc = new SparkContext(conf)
}

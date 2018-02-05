package sparkWrapper

import org.apache.spark.{SparkConf, SparkContext}

object sparkContext {

  val conf = new SparkConf().setAppName("ranker").setMaster("local[2]").set("spark.testing.memory", "2147480000")
  val sc = new SparkContext(conf)
}

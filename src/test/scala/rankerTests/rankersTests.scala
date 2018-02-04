package rankerTests

import org.scalatest._

class rankersTests extends FlatSpec {
  val exampleGraph1 = List(("url_1", "url_4"),
                           ("url_2", "url_1"),
                           ("url_3", "url_2"),
                           ("url_3", "url_1"),
                           ("url_4", "url_3"),
                           ("url_4", "url_1"))

  val exampleGraph2 = List(("url_2", "url_1"),
                           ("url_3", "url_1"),
                           ("url_3", "url_2"),
                           ("url_3", "url_1"))


  "sparkRanker" should "return List[(String, Double)] with some contents" in {
      val ranker = new sparkRanker.Ranker()
      val ranks : List[(String, Double)] = ranker.rankLinkCollection(exampleGraph1)
      assert(ranks.nonEmpty)
  }

  "sparkRanker" should "ranks of two different graphs are in fact different" in {
    val ranker = new sparkRanker.Ranker()
    val ranks1 = ranker.rankLinkCollection(exampleGraph1)
    val ranks2 = ranker.rankLinkCollection(exampleGraph2)
    assert(ranks1 != ranks2)
  }

  "sparkRanker" should "example graph 1 should have url_1 as the highest rank" in {
    val ranker = new sparkRanker.Ranker()
    val ranks = ranker.rankLinkCollection(exampleGraph1).sortBy(_._2).reverse
    assert(ranks(0)._1 == "url_1")
  }

  "sparkRanker" should "example graph 1 should have url_2 as the lowest rank" in {
    val ranker = new sparkRanker.Ranker()
    val ranks = ranker.rankLinkCollection(exampleGraph1).sortBy(_._2)
    assert(ranks(0)._1 == "url_2")
  }
}

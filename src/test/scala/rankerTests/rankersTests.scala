package rankerTests

import org.scalatest._

class rankersTests extends FlatSpec {

  "sparkRanker.Ranker" should "return List[(String, Double)] with some contents" in {
      val ranker = new sparkRanker.Ranker()
      val ranks : List[(String, Double)] = ranker.rankLinkCollection(testProperties.exampleGraph1)
      assert(ranks.nonEmpty, "Check to make sure the Ranker method actually returns anything")
  }

  "sparkRanker.Ranker" should "ranks of two different graphs are in fact different" in {
    val ranker = new sparkRanker.Ranker()
    val ranks1 = ranker.rankLinkCollection(testProperties.exampleGraph1)
    val ranks2 = ranker.rankLinkCollection(testProperties.exampleGraph2)
    assert(ranks1 != ranks2, "Check that two different graphs produce two different ranking charts.")
  }

  "sparkRanker.Ranker" should "example graph 1 should have url_1 as the highest rank" in {
    val ranker = new sparkRanker.Ranker()
    val ranks = ranker.rankLinkCollection(testProperties.exampleGraph1).sortBy(_._2).reverse
    assert(ranks(0)._1 == "url_1", "Check to see that url_1 is the highest ranked node in the reference graph.")
  }

  "sparkRanker.Ranker" should "example graph 1 should have url_2 as the lowest rank" in {
    val ranker = new sparkRanker.Ranker()
    val ranks = ranker.rankLinkCollection(testProperties.exampleGraph1).sortBy(_._2)
    assert(ranks(0)._1 == "url_2", "Check to see that url_2 is the lowest ranked node in the reference graph.")
  }

  "sparkRanker.Ranker" should "return different values if the settling depth changes" in {
    val ranker = new sparkRanker.Ranker()
    val rankSettle1 = ranker.rankLinkCollection(testProperties.exampleGraph1, 1)
    val rankSettle10 = ranker.rankLinkCollection(testProperties.exampleGraph1, 10)
    val graphNodes1 = rankSettle1.map(pair => pair._1)
    val graphNodes10 = rankSettle10.map(pair => pair._1)

    assert(graphNodes1 == graphNodes10, "Check to see is the graphs are identical in vertexs")
    assert(rankSettle1 != rankSettle10, "Check to see that the values of the graph have changes given more time to settle")
  }
}

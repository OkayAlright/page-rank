package htmlSourceUtilTests

import org.scalatest._

class htmlUtilTests extends FlatSpec {

  "pageGrabber.getPageContentsFromURL" should "return a non-empty page if url is valid" in {
    val pageContents = htmlSourceUtilities.pageGrabber.getPageContentsFromURL(testProperties.staticURL)
    assert(pageContents.isDefined)
  }

  "pageGrabber.getPageContentsFromURL" should "return an Option[None] if url is invalid" in {
    val pageContents = htmlSourceUtilities.pageGrabber.getPageContentsFromURL(testProperties.badURL)
    assert(pageContents.isEmpty)
  }

  "pageGrabber.getAllAnchorTagsRaw" should "return a List[String]" in {
    val pageContents = htmlSourceUtilities.pageGrabber.getPageContentsFromURL(testProperties.staticURL)
    val pageLinks = htmlSourceUtilities.pageGrabber.getAllAnchorTagsRaw(pageContents.get)
    assert(pageLinks.nonEmpty)
  }

  "pageGrabber.getCorrectedAnchorTags" should "have a List[String] where each item starts with 'http'" in {
    val pageContents = htmlSourceUtilities.pageGrabber.getPageContentsFromURL(testProperties.staticURL)
    val correctedLinks = htmlSourceUtilities.pageGrabber.getCorrectedAnchorTags(pageContents.get, testProperties.staticURL)
    assert(correctedLinks.forall(item => item.startsWith("http")))
  }

  "pageGrabberGetAllLinksFromURL" should "return List[String] with valid URL" in {
    val pageContents = htmlSourceUtilities.pageGrabber.GetAllLinksFromURL(testProperties.staticURL)
    assert(pageContents.nonEmpty)
  }


  "pageGrabber.GetAllLinksFromURL" should "return an Option[None] with invalid URL" in {
    val pageContents = htmlSourceUtilities.pageGrabber.GetAllLinksFromURL(testProperties.badURL)
    assert(pageContents.isEmpty)
  }

  "pageGrabber.GetAllLinksFromURL" should "return a link roster of length 1 from static site" in {
    val pageContents = htmlSourceUtilities.pageGrabber.GetAllLinksFromURL(testProperties.staticURL)
    assert(pageContents.length == 1)
  }

  "Crawler.crawler" should "return an RDD of length 3 on depth 1 using static site" in {
    val crawler = new htmlSourceUtilities.Crawler()
    assert(crawler.crawler(testProperties.staticURL, 1).collect.length == 3)
  }

  "Crawler.crawler" should "return an RDD of length greater than 3 on depth 2 using static site" in {
    val crawler = new htmlSourceUtilities.Crawler()
    assert(crawler.crawler(testProperties.staticURL, 2).collect.length >= 3)
  }

}

package htmlSourceUtilities

import java.util.logging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{ Element, ElementQuery}


object pageGrabber {

  val browser= JsoupBrowser()
  val logger: Logger = Logger.getLogger(this.getClass().toString)


  def getPageContentsFromURL(url: String): Option[browser.DocumentType] = {
    if(url == ""){
      return None
    }
    try {
      logger.info("Getting info from url: "+url)
      Some(browser.get(url))
    } catch {
      case unknownHostError: java.net.UnknownHostException =>
        logger.warning("ERROR URL not found: " + url)
        None
      case _ : Throwable =>
        logger.warning("Other error with url: " + url)
        None
    }
  }

  def getAllAnchorTagsRaw(page: browser.DocumentType): List[Element] = {
    try {
      val anchors: ElementQuery[Element] = page >> "a"
      anchors.toList
    } catch {
      case noAnchors: NullPointerException => List()
    }
  }

  def getCorrectedAnchorTags(page: browser.DocumentType, baseURL: String): List[String] = {
    val rawAnchors: List[Element] = getAllAnchorTagsRaw(page)

    if(baseURL == ""){
      return List.empty
    } else {
      rawAnchors.map(link => {
        try {
          val unformattedLink: String = link.attr("href")
          if(unformattedLink.isEmpty){
            ""
          } else if (!unformattedLink.contains("http")) {
            if(unformattedLink.substring(0,1) == "/"){
              val hostURL: String = "^.+?[^/:](?=[?/]|$)".r.findFirstIn(baseURL).getOrElse(baseURL)
              hostURL + unformattedLink
            } else {
              val hostURL: String = baseURL.split("/").dropRight(1).mkString("/")+"/"
              hostURL + unformattedLink
            }
          } else {
            unformattedLink
          }
        } catch {
          case _: java.util.NoSuchElementException => ""
        }
      })
    }
  }

  def GetAllLinksFromURL(url: String): List[String] ={
    val page = getPageContentsFromURL(url)
    if(page.isDefined) {
      getCorrectedAnchorTags(page.get, url)
    } else {
      List()
    }
  }
}
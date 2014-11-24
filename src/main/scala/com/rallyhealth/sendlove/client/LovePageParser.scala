package com.rallyhealth.sendlove.client

import com.rallyhealth.sendlove.models.Love
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import play.api.libs.json.Json
import scala.collection.JavaConversions._

class LovePageParser {
  def parse(body: String): Seq[Love] = {
    val htmlString = (Json.parse(body) \ "body" toString()).stripPrefix("\"").stripSuffix("\"").replaceAll("\\\\", "")
    val doc = Jsoup.parseBodyFragment("<table>" + htmlString + "</table>")
    val loves = doc.select("tr") map parseTableRow
    loves
  }

  def parseTableRow(row: Element): Love = {
    /*
     * <tr class="roweven love_27674">
     *  <td class="headFrom"><a href="dougernaut" target="_blank">dougernaut</a></td>
     *  <td class="headTo"><a href="some.user" target="_blank">some.user</a></td>
     *  <td class="headFor">unblocking through weekend affiliation help! pony</td>
     *  <td class="headWhen">1 hr ago</td>
     * </tr>
     */
    Love(
      id = row.classNames().find(_.matches("love_[0-9]+")).get.stripPrefix("love_"),
      fromUsername = row.select(".headFrom a").text,
      toUsername = row.select(".headTo a").text,
      content = row.select(".headFor").text,
      age = row.select(".headWhen").text
    )
  }

}


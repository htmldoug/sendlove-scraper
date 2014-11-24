package com.rallyhealth.sendlove.client

import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}
import play.api.libs.ws.{DefaultWSClientConfig, WSRequestHolder}

/**
 * @param phpsessid PHPSESSID cookie value (grab from an authenticated session b/c I don't want to deal with passwords)
 */
class SendLoveClient(phpsessid: String) {

  private val base = "https://audaxhealth.sendlove.us"
  private val config = new NingAsyncHttpClientConfigBuilder(DefaultWSClientConfig(acceptAnyCertificate = Some(true))).build()
  private val client = new NingWSClient(config)

  def close() = client.close()

  /**
   * Gets all loves, one page at a time. May be filtered to loves to & from a specific user.
   *
   * @param page starting at 1
   * @param onlyInvolvingEmail the email address to restrict to, case sensitive
   */
  def getLoves(page: Int = 1, onlyInvolvingEmail: Option[String] = None): WSRequestHolder = {
    val filterOrEmptyString = onlyInvolvingEmail.map("&username=" + _ + "&just_user=true").getOrElse("")

    client.url(base + s"/love/toforAjax.php?tell=listLove$filterOrEmptyString&page=$page&when=true")
      .withHeaders(cookieHeader)
      .withMethod("GET")
  }

  def getEmailAddresses() = {
    client.url(base + "/love/helper/getemails.php")
      .withHeaders(cookieHeader)
      .withMethod("GET")
  }

  private def cookieHeader = "Cookie" -> s"PHPSESSID=$phpsessid"
}

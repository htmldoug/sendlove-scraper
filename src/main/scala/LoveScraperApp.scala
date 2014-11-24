import com.github.tototoshi.csv.CSVWriter
import com.rallyhealth.sendlove.client.{LovePageParser, SendLoveClient}
import java.io.File
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object LoveScraperApp extends App {
  val emailCaseSensitive = "some.user@example.com"
  val phpsessid = "1234567890abcdefghijklmnop" // grab from Chrome network inspector

  val client = new SendLoveClient(phpsessid)
  val parser = new LovePageParser
  val writer = {
    val filename = s"target/${emailCaseSensitive.replaceAll("@.*", "")}.csv"
    println(s"Exporting to: $filename")
    CSVWriter.open(new File(filename), append = false)
  }
  writer.writeRow(Seq("page", "loveId", "from", "to", "content", "age"))

  try {
    for (i <- Stream.from(1)) {
      println("Reading page " + i)
      val eventualLoves = client.getLoves(onlyInvolvingEmail = Some(emailCaseSensitive), page = i).execute().map(rsp => parser.parse(rsp.body))
      val loves = Await.result(eventualLoves, 60.seconds) // shaky site performance. go slow and don't DOS it.

      if (loves.isEmpty) throw new EndOfLovesException()

      loves.foreach(love => writer.writeRow(Seq(i, love.id, love.fromUsername, love.toUsername, love.content, love.age)))
      writer.flush()
    }
  } catch {
    case e: EndOfLovesException => println("End of loves.")
  } finally {
    writer.flush()
    writer.close()
    client.close()
  }

  class EndOfLovesException() extends Exception

}

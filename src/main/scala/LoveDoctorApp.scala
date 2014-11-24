import com.rallyhealth.sendlove.client.LoveCsvReader
import java.io.File

/**
 * Produces reports from csv love dumps.
 */
object LoveDoctorApp extends App {

  val target = "some.user"
  val username = target // sometimes different.
  val readLoves = LoveCsvReader.read(new File(s"target/$target.csv"))
    .filterNot(_.age == "2 yrs ago")
    .filterNot(_.age == "1 yr ago")
    .filter(_.toUsername == username)

  val grouped = readLoves.groupBy(_.fromUsername)

  for ((sender, loves) <- grouped.toSeq.sortBy(_._2.size)(implicitly[Ordering[Int]].reverse)) {
    println(s"$sender (${loves.size})")
    for (love <- loves) println(s"  ${love.content} -- $sender ${love.age}")
  }
}

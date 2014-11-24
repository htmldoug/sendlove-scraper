name := "sendlove-scraper"

version := "1.0"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ws" % "2.3.1",
  "com.typesafe.play" %% "play-json" % "2.3.1",
  "junit" % "junit" % "4.8.1" % "test",
  "org.jsoup" % "jsoup" % "1.8.1",
  "com.github.tototoshi" %% "scala-csv" % "1.1.1"
)

resolvers += "Secured Central Repository" at "https://repo1.maven.org/maven2"

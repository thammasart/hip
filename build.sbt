name := "hip"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.27",
  "org.apache.poi" % "poi" % "3.8"
)

play.Project.playJavaSettings


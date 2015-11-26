case class MininetHost( hostname : String )


object Hostname {
  val default = MininetHost("none")
  var host : Option[MininetHost] = None
  lazy val hostname = host.getOrElse(default).hostname
}


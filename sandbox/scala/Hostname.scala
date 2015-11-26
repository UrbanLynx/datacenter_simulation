  object Hostname {
    var host : MininetHost = null
    lazy val hostname = host.hostname
  }

  case class MininetHost( hostname : String )


object Slave {
  def main(args : Array[String]) {
    val ip = args(0)
    println("Slave got arg for hostname: " + ip)
    Hostname.host = MininetHost(ip)
    println("hostname initialized to : " + Hostname.hostname)
  }

  object Hostname {
    var host : MininetHost = null
    lazy val hostname = host.hostname
  }

  case class MininetHost( hostname : String )

}



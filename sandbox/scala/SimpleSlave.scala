
object SimpleSlave {

  def main(args : Array[String]) {
    val ip = args(0)
    println("Slave got arg for hostname: " + ip)
    Hostname.host = MininetHost(ip)
    println("hostname initialized to : " + Hostname.hostname)
  }

}



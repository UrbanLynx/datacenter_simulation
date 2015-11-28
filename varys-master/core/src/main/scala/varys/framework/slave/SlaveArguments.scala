package varys.framework.slave

import java.lang.management.ManagementFactory

import varys.util.IntParam
import varys.Utils

/**
 * Command-line parser for the slave.
 */
private[varys] class SlaveArguments(
    args: Array[String]) {
  
  // for mininet simulation, localHostName will return machine host name, not mininet host name
  // instead simulation master will pass hostname to each simulation slave
  //var ip = Utils.localHostName()
  var ip : String = null

      // Attempt to use IPs instead of hostname did not work 
      // (simulation master could not connect to Varys master)
      // and anyway, mininet hosts would all have the same localHostIP using this method
      //var ip = Utils.localHostIP()

  var port = 0
  var webUiPort = 16017
  var commPort = 1607
  var master: String = null
  var workDir: String = null
  
  // Check for settings in environment variables 
  if (System.getenv("VARYS_SLAVE_PORT") != null) {
    port = System.getenv("VARYS_SLAVE_PORT").toInt
  }
  if (System.getenv("VARYS_SLAVE_WEBUI_PORT") != null) {
    webUiPort = System.getenv("VARYS_SLAVE_WEBUI_PORT").toInt
  }
  if (System.getenv("VARYS_SLAVE_COMM_PORT") != null) {
    commPort = System.getenv("VARYS_SLAVE_COMM_PORT").toInt
  }
  if (System.getenv("VARYS_SLAVE_DIR") != null) {
    workDir = System.getenv("VARYS_SLAVE_DIR")
  }
  
  parse(args.toList)

  def parse(args: List[String]): Unit = args match {
    case ("--ip" | "-i") :: value :: tail =>
      ip = value
      parse(tail)

    case ("--port" | "-p") :: IntParam(value) :: tail =>
      port = value
      parse(tail)

    case ("--work-dir" | "-d") :: value :: tail =>
      workDir = value
      parse(tail)
      
    case "--webui-port" :: IntParam(value) :: tail =>
      webUiPort = value
      parse(tail)

    case "--comm-port" :: IntParam(value) :: tail =>
      commPort = value
      parse(tail)

    case ("--help" | "-h") :: tail =>
      printUsageAndExit(0)

    case value :: tail =>
      if (ip != null) {  // Edited from master != null for mininet simulation 
        printUsageAndExit(1)
      }
      if ( master == null ) {
        master = value
      } else {
        ip = value
      }
      parse(tail)

    case Nil =>
      if (master == null) {  // No positional argument was given
        printUsageAndExit(1)
      }

    case _ =>
      printUsageAndExit(1)
  }

  /**
   * Print usage and exit JVM with the given exit code.
   */
  def printUsageAndExit(exitCode: Int) {
    System.err.println(
      "Usage: Slave [options] <master>\n" +
      "\n" +
      "Master must be a URL of the form varys://hostname:port\n" +
      "\n" +
      "Options:\n" +
      "  -d DIR, --work-dir DIR   Directory to run coflows in (default: VARYS_HOME/work)\n" +
      "  -i IP, --ip IP           IP address or DNS name to listen on\n" +
      "  -p PORT, --port PORT     Port to listen on (default: random)\n" +
      "  --webui-port PORT        Port for web UI (default: 16017)\n" + 
      "  --comm-port PORT        Port for Slave-To-Slave communication (default: 1607)")
    System.exit(exitCode)
  }

}

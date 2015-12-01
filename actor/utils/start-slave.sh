#!/bin/sh

# RUN THIS ON HOST OS of mininet, all mininet hosts need not be running varys.
# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi
#d=$(date +"%y%m%d%H%M%S")
#$file = "~/slave_$d.log"
#echo "$d"

export VARYS_LOCAL_IP=$1
# Building And Starting master
echo "BUILDING AND STARTING SLAVE"
cd ~/group6/actor
/usr/lib/jvm/java-7-openjdk-amd64/bin/java -classpath /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/compilefontconfig.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/java-atk-wrapper.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/javazic.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rhino.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar:./target/classes:./lib/repository/commons-cli/commons-cli/1.3.1/commons-cli-1.3.1.jar:./lib/repository/joda-time/joda-time/2.9/joda-time-2.9.jar:./lib/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar:/home/aborase/group6/actor/lib/varys-core_2.10-0.2.0-SNAPSHOT.jar:./lib/repository/org/scala-lang/scala-library/2.10.4/scala-library-2.10.4.jar:./lib/repository/com/google/guava/guava/11.0.1/guava-11.0.1.jar:./lib/repository/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:./lib/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:./lib/repository/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:./lib/repository/com/google/protobuf/protobuf-java/2.4.1/protobuf-java-2.4.1.jar:./lib/repository/com/typesafe/akka/akka-actor_2.10/2.2.3/akka-actor_2.10-2.2.3.jar:./lib/repository/com/typesafe/config/1.0.2/config-1.0.2.jar:./lib/repository/com/typesafe/akka/akka-remote_2.10/2.2.3/akka-remote_2.10-2.2.3.jar:./lib/repository/io/netty/netty/3.6.6.Final/netty-3.6.6.Final.jar:./lib/repository/org/uncommons/maths/uncommons-maths/1.2.2a/uncommons-maths-1.2.2a.jar:./lib/repository/com/typesafe/akka/akka-slf4j_2.10/2.2.3/akka-slf4j_2.10-2.2.3.jar:./lib/repository/net/liftweb/lift-json_2.10/2.5.1/lift-json_2.10-2.5.1.jar:./lib/repository/org/scala-lang/scalap/2.10.0/scalap-2.10.0.jar:./lib/repository/org/scala-lang/scala-compiler/2.10.0/scala-compiler-2.10.0.jar:./lib/repository/org/scala-lang/scala-reflect/2.10.0/scala-reflect-2.10.0.jar:./lib/repository/com/thoughtworks/paranamer/paranamer/2.4.1/paranamer-2.4.1.jar:./lib/repository/io/netty/netty-all/4.0.23.Final/netty-all-4.0.23.Final.jar:./lib/repository/org/fusesource/sigar/1.6.4/sigar-1.6.4.jar:./lib/repository/com/esotericsoftware/kryo/kryo/2.22/kryo-2.22.jar:./lib/repository/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar:./lib/repository/com/github/romix/akka/akka-kryo-serialization_2.10/0.3.1/akka-kryo-serialization_2.10-0.3.1.jar:./lib/repository/com/typesafe/akka/akka-kernel_2.10/2.2.1/akka-kernel_2.10-2.2.1.jar:./lib/repository/org/apache/spark/spark-core_2.10/1.1.0/spark-core_2.10-1.1.0.jar:./lib/repository/org/apache/hadoop/hadoop-client/1.0.4/hadoop-client-1.0.4.jar:./lib/repository/org/apache/hadoop/hadoop-core/1.0.4/hadoop-core-1.0.4.jar:./lib/repository/xmlenc/xmlenc/0.52/xmlenc-0.52.jar:./lib/repository/org/apache/commons/commons-math/2.1/commons-math-2.1.jar:./lib/repository/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar:./lib/repository/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:./lib/repository/commons-lang/commons-lang/2.4/commons-lang-2.4.jar:./lib/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:./lib/repository/commons-digester/commons-digester/1.8/commons-digester-1.8.jar:./lib/repository/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar:./lib/repository/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar:./lib/repository/commons-el/commons-el/1.0/commons-el-1.0.jar:./lib/repository/hsqldb/hsqldb/1.8.0.10/hsqldb-1.8.0.10.jar:./lib/repository/oro/oro/2.0.8/oro-2.0.8.jar:./lib/repository/org/codehaus/jackson/jackson-mapper-asl/1.0.1/jackson-mapper-asl-1.0.1.jar:./lib/repository/org/codehaus/jackson/jackson-core-asl/1.0.1/jackson-core-asl-1.0.1.jar:./lib/repository/net/java/dev/jets3t/jets3t/0.7.1/jets3t-0.7.1.jar:./lib/repository/commons-codec/commons-codec/1.3/commons-codec-1.3.jar:./lib/repository/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:./lib/repository/org/apache/curator/curator-recipes/2.4.0/curator-recipes-2.4.0.jar:./lib/repository/org/apache/curator/curator-framework/2.4.0/curator-framework-2.4.0.jar:./lib/repository/org/apache/curator/curator-client/2.4.0/curator-client-2.4.0.jar:./lib/repository/org/apache/zookeeper/zookeeper/3.4.5/zookeeper-3.4.5.jar:./lib/repository/jline/jline/0.9.94/jline-0.9.94.jar:./lib/repository/org/eclipse/jetty/jetty-plus/8.1.14.v20131031/jetty-plus-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/orbit/javax.transaction/1.1.1.v201105210645/javax.transaction-1.1.1.v201105210645.jar:./lib/repository/org/eclipse/jetty/jetty-webapp/8.1.14.v20131031/jetty-webapp-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-xml/8.1.14.v20131031/jetty-xml-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-servlet/8.1.14.v20131031/jetty-servlet-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-jndi/8.1.14.v20131031/jetty-jndi-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/orbit/javax.mail.glassfish/1.4.1.v201005082020/javax.mail.glassfish-1.4.1.v201005082020.jar:./lib/repository/org/eclipse/jetty/orbit/javax.activation/1.1.0.v201105071233/javax.activation-1.1.0.v201105071233.jar:./lib/repository/org/eclipse/jetty/jetty-security/8.1.14.v20131031/jetty-security-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-util/8.1.14.v20131031/jetty-util-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-server/8.1.14.v20131031/jetty-server-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/orbit/javax.servlet/3.0.0.v201112011016/javax.servlet-3.0.0.v201112011016.jar:./lib/repository/org/eclipse/jetty/jetty-continuation/8.1.14.v20131031/jetty-continuation-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-http/8.1.14.v20131031/jetty-http-8.1.14.v20131031.jar:./lib/repository/org/eclipse/jetty/jetty-io/8.1.14.v20131031/jetty-io-8.1.14.v20131031.jar:./lib/repository/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar:./lib/repository/org/slf4j/jul-to-slf4j/1.7.5/jul-to-slf4j-1.7.5.jar:./lib/repository/org/slf4j/jcl-over-slf4j/1.7.5/jcl-over-slf4j-1.7.5.jar:./lib/repository/org/slf4j/slf4j-log4j12/1.7.5/slf4j-log4j12-1.7.5.jar:./lib/repository/com/ning/compress-lzf/1.0.0/compress-lzf-1.0.0.jar:./lib/repository/org/xerial/snappy/snappy-java/1.0.5.3/snappy-java-1.0.5.3.jar:./lib/repository/net/jpountz/lz4/lz4/1.2.0/lz4-1.2.0.jar:./lib/repository/com/twitter/chill_2.10/0.3.6/chill_2.10-0.3.6.jar:./lib/repository/com/twitter/chill-java/0.3.6/chill-java-0.3.6.jar:./lib/repository/commons-net/commons-net/2.2/commons-net-2.2.jar:./lib/repository/org/spark-project/akka/akka-remote_2.10/2.2.3-shaded-protobuf/akka-remote_2.10-2.2.3-shaded-protobuf.jar:./lib/repository/org/spark-project/akka/akka-actor_2.10/2.2.3-shaded-protobuf/akka-actor_2.10-2.2.3-shaded-protobuf.jar:./lib/repository/org/spark-project/protobuf/protobuf-java/2.4.1-shaded/protobuf-java-2.4.1-shaded.jar:./lib/repository/org/spark-project/akka/akka-slf4j_2.10/2.2.3-shaded-protobuf/akka-slf4j_2.10-2.2.3-shaded-protobuf.jar:./lib/repository/org/json4s/json4s-jackson_2.10/3.2.10/json4s-jackson_2.10-3.2.10.jar:./lib/repository/org/json4s/json4s-core_2.10/3.2.10/json4s-core_2.10-3.2.10.jar:./lib/repository/org/json4s/json4s-ast_2.10/3.2.10/json4s-ast_2.10-3.2.10.jar:./lib/repository/com/fasterxml/jackson/core/jackson-databind/2.3.1/jackson-databind-2.3.1.jar:./lib/repository/com/fasterxml/jackson/core/jackson-annotations/2.3.0/jackson-annotations-2.3.0.jar:./lib/repository/com/fasterxml/jackson/core/jackson-core/2.3.1/jackson-core-2.3.1.jar:./lib/repository/colt/colt/1.2.0/colt-1.2.0.jar:./lib/repository/concurrent/concurrent/1.3.4/concurrent-1.3.4.jar:./lib/repository/org/apache/mesos/mesos/0.18.1/mesos-0.18.1-shaded-protobuf.jar:./lib/repository/com/clearspring/analytics/stream/2.7.0/stream-2.7.0.jar:./lib/repository/com/codahale/metrics/metrics-core/3.0.0/metrics-core-3.0.0.jar:./lib/repository/com/codahale/metrics/metrics-jvm/3.0.0/metrics-jvm-3.0.0.jar:./lib/repository/com/codahale/metrics/metrics-json/3.0.0/metrics-json-3.0.0.jar:./lib/repository/com/codahale/metrics/metrics-graphite/3.0.0/metrics-graphite-3.0.0.jar:./lib/repository/org/tachyonproject/tachyon-client/0.5.0/tachyon-client-0.5.0.jar:./lib/repository/org/tachyonproject/tachyon/0.5.0/tachyon-0.5.0.jar:./lib/repository/commons-io/commons-io/2.4/commons-io-2.4.jar:./lib/repository/org/spark-project/pyrolite/2.0.1/pyrolite-2.0.1.jar:./lib/repository/net/sf/py4j/py4j/0.8.2.1/py4j-0.8.2.1.jar:./lib/repository/org/scala-lang/modules/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar:./lib/repository/com/esotericsoftware/minlog/minlog/1.2/minlog-1.2.jar:./lib/repository/com/esotericsoftware/reflectasm/reflectasm/1.07/reflectasm-1.07.jar:./lib/repository/org/ow2/asm/asm/4.0/asm-4.0.jar:./lib/repository/org/objenesis/objenesis/1.2/objenesis-1.2.jar:./lib/repository/junit/junit/4.8.2/junit-4.8.2.jar:./lib/repository/org/mongodb/mongo-java-driver/3.0.4/mongo-java-driver-3.0.4.jar Simulation.Slave.Main > ~/slave.log 2>&1 &

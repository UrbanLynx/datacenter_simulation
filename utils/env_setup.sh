#!/bin/sh


# Environment setup script for mininet

echo "Cloning varys"
cd ~
git clone https://github.com/coflow/varys
echo "Varys cloned at ~/varys"

echo "############### Setting up mininet dependencies ###################"
sudo apt-get update
sudo apt-get install -y git vim-nox python-setuptools libpython-all-dev flex bison traceroute
echo "###################################################################"
echo ""

echo "############# Setting up mininet in /home/<user>/ #################"
cd ~
git clone git://github.com/mininet/mininet
cd mininet
./util/install.sh -fnv
echo "###################################################################"
echo ""

echo "################ Installing Floodlight Controller #################"
sudo apt-get install build-essential default-jdk ant python-dev eclipse
cd ~
git clone git://github.com/floodlight/floodlight.git
cd floodlight
ant
sudo mkdir /var/lib/floodlight
sudo chmod 777 /var/lib/floodlight
echo "###################################################################"
echo ""

echo "################# Installing pox and ltprotocol ###################"
cd ~
git clone http://github.com/noxrepo/pox
cd ~
git clone git://github.com/dound/ltprotocol.git
cd ltprotocol 
sudo python setup.py install
echo "###################################################################"
echo ""

echo "############## Setting up Java on the machine #####################"
sudo apt-get install openjdk-7-jre-headless
sudo apt-get install openjdk-7-jdk
echo "###################################################################"
echo ""

echo "################ Installing scala ################################"
cd ~
sudo apt-get install scala
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
export SCALA_HOME="/usr/bin/scala"
echo "###################################################################"
echo ""

echo "################ Building varys ################################"
cd ~/varys/
./sbt/sbt package
echo "Varys built"
cd ~
echo "###################################################################"
echo ""

echo "############ Installing FNSS and python dependencies ##############"
sudo apt-get install python-numpy python-scipy
git clone https://github.com/fnss/fnss
cd fnss/core
sudo python setup.py install
cd ~
echo "###################################################################"
echo ""

echo "DONE!"


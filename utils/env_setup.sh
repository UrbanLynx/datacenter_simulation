#!/bin/sh


# Environment setup script for mininet

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
cd ~
echo "DONE!"
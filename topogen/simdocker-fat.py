#!/usr/bin/python

"""
This example shows how to create a simple network and
how to create docker containers (based on existing images)
to it.
"""

from mininet.net import Mininet
from mininet.node import Controller, Docker, RemoteController
from mininet.cli import CLI
from mininet.topo import Topo
from mininet.log import setLogLevel, info

import os
#import logging
import sys
import time

#logging.basicConfig(level=logging.DEBUG)
#logger = logging.getLogger( __name__ )

class fat_tree_topo(Topo):
    csw = []
    asw = []
    esw = []
    host = []
    kval = 4
    #ctrlsw = []
    #ctrlhost = []
    def __init__(self):
        kval = 4

        #Init Topo
        Topo.__init__(self)

    def setup_topo(self, kval):
        self.kval = kval
        # setup all network entities
        print("**** Setting up core switches ****\n")
        self.csw_setup(self.kval)

        print("**** Setting up aggregation switches ****\n")
        self.asw_setup(self.kval)

        print("**** Setting up edge switches ****\n")
        self.esw_setup(self.kval)

        print("**** Setting up  hosts ****\n")
        self.host_setup(self.kval)

    def csw_setup(self, kval):
        # setup control switch
        #self.ctrlsw.append(self.addSwitch('0'))

        # setup core switches
        for i in range(0, (kval*kval/4)):
            self.csw.append(self.addSwitch('1%d' % i))
        print(self.csw)

    def asw_setup(self, kval):
        # setup aggregation switches
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                self.asw.append(self.addSwitch('2%d%d' % (i, j)))
        print(self.asw)

    def esw_setup(self, kval):
        # setup edge switches
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                self.esw.append(self.addSwitch('3%d%d' % (i, j)))
        print(self.esw)

    def host_setup(self, kval):
        #setup controller host
        #self.ctrlhost.append(self.addHost('hctrl', ip='10.0.0.254/8', cls=Docker, dimage='aborase/simdocker:v2'))

        # setup end hosts
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                for k in range (0, kval/2):
                    self.host.append(self.addHost('h%d%d%d' % (i, j, k), ip='10.%d.%d.%d/8' % (i, j, (k+2)), cls=Docker, dimage="aborase/simdocker:v2.1"))

    def link_setup(self, kval):
        # setup links between core and aggregation swtches
        for i in range(0, (kval*kval/4)):
            for j in range(0, kval):
                x = ((j*kval/2) + (i/(kval/2)))
                self.addLink(self.csw[i], self.asw[x])

        # setup links between aggregation and edge switches
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                x = ((i*kval/2) + j)
                for k in range(0, kval/2):
                    y = ((i*kval/2) + k)
                    self.addLink(self.asw[x], self.esw[y])

        # setup links between edge switches and end hosts
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                x = ((i*kval/2) + j)
                for k in range(0, kval/2):
                    y = ((x*kval/2) + k)
                    self.addLink(self.esw[x], self.host[y])
                    # control network
                    #self.addLink(self.ctrlsw[0], self.host[y])

        # control host
        #self.addLink(self.ctrlhost[0], self.ctrlsw[0])

def enable_stp(kval):
    for i in range(0, (kval*kval/4)):
        cmd = "ovs-vsctl set Bridge 1%d stp_enable=true" % i
        os.system(cmd)
        print cmd

    for i in range(0, kval):
        for j in range(0, (kval/2)):
            cmd = "ovs-vsctl set Bridge 2%d%d stp_enable=true" % (i, j)
            os.system(cmd)
            print cmd
            cmd = "ovs-vsctl set Bridge 3%d%d stp_enable=true" % (i, j)
            os.system(cmd)
            print cmd

def config_host(net):
    # configure /etc/hosts file of each docker host
    print("**** Configuring each dockernet host ****\n")
    ent = []
    for h in net.hosts:
        time.sleep(1)
        name = h.cmd('hostname')
        #print(name[:12])
        ip = h.cmd('ifconfig | awk \'/inet addr/{print substr($2,6)}\' | grep -v \'127.0.0.1\'')
        #ip = h.cmd('ifconfig | grep -Eo \'inet (addr:)?([0-9]*\\.){3}[0-9]*\' | grep -Eo \'([0-9]*\\.){3}[0-9]*\' | grep -v \'127.0.0.1\'')
        # Collect all ip hostname pairs in a list
        ent.append(ip[:8] + ' ' +name[:12])
        # add entry for localhost in /etc/hosts
        h.cmd('echo \"127.0.0.1 localhost\" >> /etc/hosts')

    for e in ent:
        print e

    # Now add every ent entry to each host's /etc/hosts file
    for h in net.hosts:
        for e in ent:
            h.cmd('echo \"%s\" >> /etc/hosts' % e)

def start_varys(net):
    # start varys master on ctrlhost
    print("\n**** Starting varys-master daemon on control host h000 ****\n") 
    h000 = net.get('h000')
    #hctrl.cmd('cd; cd group6/varys-master; ./bin/varys-daemon.sh start varys.framework.master.Master --ip 10.0.0.2 --port 1606 --webui-port 16016')
    result = h000.cmd('cd; cd group6/aalo; ./bin/start-all.sh')
    print(result)
    # start varys slave on each host
    print("\n**** Starting varys-slave daemon on all the  hosts ****\n")
    for h in net.hosts:
        time.sleep(1)
        if h000 != h:
            name = h.cmd('hostname')
            print("starting varys-slave on host %s" % name[:12])
            result = h.cmd('cd; cd group6/aalo; ./bin/varys-daemon.sh start varys.framework.slave.Slave varys://10.0.0.2:1606')
            print(result)

def start_simulation(net):
    # start simulation master on ctrlhost
    print("\n**** Starting simulation-master daemon on control host h000 ****\n") 
    h000 = net.get('h000')
    ip = h000.cmd('ifconfig | awk \'/inet addr/{print substr($2,6)}\' | grep -v \'127.0.0.1\'')
    result = h000.cmd('cd; cd group6/actor; ./utils/start-master.sh %s' % ip[:8])
    print(result)
    # start simulation slave on each host
    print("\n**** Starting simulation-slave daemon on all the  hosts ****\n")
    for h in net.hosts:
        time.sleep(1)
        if h000 != h:
            name = h.cmd('hostname')
            ip = h.cmd('ifconfig | awk \'/inet addr/{print substr($2,6)}\' | grep -v \'127.0.0.1\'')
            print("starting simulation-slave on host %s" % name[:12])
            result = h.cmd('cd; cd group6/actor; ./utils/start-slave.sh %s' % ip[:8])
            print(result)

def setup_fattree_topo(kval):
    # setup fat-tree topology
    print("\n**** Setting up fat-tree topology ****\n")
    topo = fat_tree_topo()
    topo.setup_topo(kval)

    print("\n**** Setting up all the network links ****\n")
    topo.link_setup(kval)

    # instantiate mininet network
    print("\n**** Instantiating netwrok ****\n")
    net = Mininet(topo=topo, controller=None)
    net.addController('floodlight', controller=RemoteController, ip="127.0.0.1", port=6653)

    # start the netwrok
    net.start()
    print("\n**** Sleeping for 10 seconds ****\n")
    time.sleep(10)

    # configure the hosts
    config_host(net)

    #enable STP
    print("\n**** Sleeping for 5 seconds ****\n")
    time.sleep(5)
    enable_stp(kval)

    # check connectivity
    print("\n**** Sleeping for 5 seconds ****\n")
    time.sleep(5)
    net.pingAll()

    # start varys daemons
    print("\n**** Sleeping for 5 seconds ****\n")
    time.sleep(5)
    start_varys(net)

    # start simulation
    print("\n**** Sleeping for 5 seconds ****\n")
    time.sleep(5)
    start_simulation(net)

    # grab mininet CLI
    CLI(net)

    # dump logs
    #collect_data(net)

    net.stop()

if __name__ == '__main__':
    setLogLevel('info')
    if os.getuid() != 0:
        print("Run as a root \n")
    elif os.getuid() == 0:
        setup_fattree_topo(int(sys.argv[1]))

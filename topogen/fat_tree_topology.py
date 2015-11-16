#!/usr/bin/env python

from mininet.net import Mininet
from mininet.node import Node, Controller, RemoteController
from mininet.cli import CLI
from mininet.link import Link, Intf, TCLink
from mininet.topo import Topo
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel, info
from mininet.util import waitListening

import os
import logging
import sys

logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger( __name__ )

class fat_tree_topo(Topo):
    csw = []
    asw = []
    esw = []
    host = []
    kval = 4
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
        # setup end hosts
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                for k in range (0, kval/2):
                    self.host.append(self.addHost('h%d%d%d' % (i, j, k)))

    def link_setup(self, kval):
        # setup links between core and aggregation swtches
        for i in range(0, (kval*kval/4)):
            for j in range(0, kval):
                x = ((j*kval/2) + (i/(kval/2)))
                self.addLink(self.csw[i], self.asw[x], bw=400, delay='2ms', loss=2)

        # setup links between aggregation and edge switches
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                x = ((i*kval/2) + j)
                for k in range(0, kval/2):
                    y = ((i*kval/2) + k)
                    self.addLink(self.asw[x], self.esw[y], bw=100, delay='2ms', loss=2)

        # setup links between edge switches and end hosts
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                x = ((i*kval/2) + j)
                for k in range(0, kval/2):
                    y = ((x*kval/2) + k)
                    self.addLink(self.esw[x], self.host[y], bw=25, delay='2ms')

def enable_ssh(net):
    # start sshd on each host
    print("**** Starting ssh daemons on each host ****")
    for host in net.hosts:
        host.cmd('/usr/sbin/sshd -D &' )

    print("**** sshd is running at following addresses: ****")
    for host in net.hosts:
        print(host.name, host.IP())

def disable_ssh(net):
    # stop sshd on each host before we destroy network
    print("**** Stopping ssh daemons running on all hosts ****")
    for host in net.hosts:
        host.cmd('kill %/usr/sbin/sshd')

def ip_mac_setup(net, kval):
    for i in range(0, kval):
        for j in range(0, (kval/2)):
            for k in range(0, (kval/2)):
                host = net.get('h%d%d%d' % (i, j, k))
                host.setIP('10.%d.%d.%d/8' % (i, j, (k+2)))
                host.setMAC('00.00.00.0%d.0%d.0%d' % (i, j, (k+2)))

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

def setup_fattree_topo(kval):
    # setup fat-tree topology
    print("**** Setting up fat-tree topology ****\n")
    topo = fat_tree_topo()
    topo.setup_topo(kval)

    print("**** Setting up all the network links ****\n")
    topo.link_setup(kval)

    # instantiate mininet network
    print("**** Instantiating netwrok ****")
    net = Mininet(topo=topo, link=TCLink, controller=None)
    net.addController('floodlight', controller=RemoteController, ip="127.0.0.1", port=6653)

    print("**** Setting up IP and MAC addresses ****")
    ip_mac_setup(net, kval)

    # start the netwrok
    net.start()
   
    # start sshd on each host
    enable_ssh(net)

    # enable STP
    enable_stp(kval)

    dumpNodeConnections(net.hosts)
    #net.pingAll()

    # grab mininet CLI
    CLI(net)
    disable_ssh(net)
    net.stop()

if __name__ == '__main__':
    setLogLevel('info')
    if os.getuid() != 0:
        print("Run as a root \n")
    elif os.getuid() == 0:
        setup_fattree_topo(int(sys.argv[1]))


#!/usr/bin/python

#############################################################################
# Below python script will configure a master mininet VM, it does not have
# hosts and has only switches. All the links are limited to 10mbps.
# It also creates a GRE tunnel per switch for each low level mininet VM."
#
# USAGE: sudo python ./master_mn.py 192.168.0 13 1 3
# arg1: first 3 octets  of low-level mininet VM Ip's (192.168.0)
# arg2: fourth octet of first low-level mininet VM IP (13)
# arg3: number of low-level mininet VM
# arg4: number of level1 switches to be configured
#############################################################################

import sys
import os
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import CPULimitedHost, RemoteController
from mininet.link import TCLink
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel
from mininet.cli import CLI

class MasterMnTopo(Topo):
    "Master mininet VM topology"
    def build(self, n):
        # create level0 switch
        switch = self.addSwitch('s1')
        # create level1 switch
        for s in range(n):
            # XXX: below hosts are not needed, added to check connectivity
            # with low level hosts
	    host = self.addHost('h%s' % (s+1))
            # Each switch gets 50%/n of system CPU
            switch1 = self.addSwitch('s%s' % (s+2), cpu=.5/n)
            # 10 Mbps, 5ms delay, 10% loss, 1000 packet queue
            self.addLink(switch1, host)
            self.addLink(switch1, switch, bw=10, delay='1ms', loss=1, max_queue_size=5000, use_htb=True)

def MasterMnNet(n):
    print "########### Instanitiate a mininet of topology MasterMnTopo #############\n"
    topo = MasterMnTopo(n)
    net = Mininet(topo=topo, ipBase='10.0.0.0/8', autoSetMacs=True, host=CPULimitedHost, link=TCLink)
    net.start()
    return net

def CreateGRETunnel(net, remote, start, cnt, n):
    print "########### Create GRE tunnel between level1 & level2 switches ##########\n"
    for s in range(n):
        for low in range(cnt):
            ip=('%s.%s' % (remote,(start+low)))
            sw=('s%s' % (s+2))
            print ("-------   creating GRE Tunnel %s->%s -------" % (sw, ip))
            result = os.system('sudo ovs-vsctl add-port %s %s-gre1 -- set interface %s-gre1 type=gre options:remote_ip=%s' % (sw, sw, sw, ip))
            #print result

def setup(remote, start, cnt, n):
    print "#################### Setting up Master mininet VM ######################\n"
    net = MasterMnNet(n)
    # XXX: capturing CLI using below, it should be rem oved later on,
    # once bash driver serializes master and low work.
    CLI(net)
    CreateGRETunnel(net, remote, start, cnt, n)
    # Check basic connectivity
    # XXX: check connectivity with and between low-level hosts
    dumpNodeConnections(net.hosts)
    net.pingAll()
    CLI(net)
    net.stop()

if __name__ == '__main__':
    #selLogLevel('info')
    setup(sys.argv[1], int(sys.argv[2]), int(sys.argv[3]), int(sys.argv[4]))

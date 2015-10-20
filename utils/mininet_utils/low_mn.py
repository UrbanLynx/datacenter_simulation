#!/usr/bin/python

#####################################################################
# Below python script will configure a single lower level mininet VM.
# It has depth 1 and fanout 11. All the links are limited to 1mbps.
# Additionally it assigns unique IP address/macs etc to every mininet
# entity. It also creates a GRE tunnel to the master mininet VM."
#
# USAGE: sudo python ./low_mn.py 192.168.0.14 11 10
# arg1: IP of master mininet VM
# arg2: last octet of the starting hosts IP
# arg3: number of hosts to configure in this mininet vm
#####################################################################

import sys
import os
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import CPULimitedHost, RemoteController
from mininet.link import TCLink
from mininet.util import dumpNodeConnections
from mininet.cli import CLI

class LowMnTopo(Topo):
    "Low level mininet VM topology"
    def build(self, n):
        switch = self.addSwitch('s1')
        for h in range(n):
            # Each host gets 50%/n of system CPU
            host = self.addHost('h%s' % (h+1), cpu=.5/n)
            # 10 Mbps, 5ms delay, 10% loss, 1000 packet queue
            self.addLink(host, switch, bw=1, delay='1ms', loss=1, max_queue_size=1000, use_htb=True)

def LowMnNet(remote, n):
    print "############ Instanitiate a mininet of topology LowMnTopo ###############\n"
    topo = LowMnTopo(n)
    ctrl_port=6633
    net = Mininet(topo=topo, controller=lambda x: RemoteController( x, remote, ctrl_port), listenPort=6633, ipBase='10.0.0.0/8', autoSetMacs=True, host=CPULimitedHost, link=TCLink)
    net.start()
    return net

def AssignIp(net, start, n):
    print "############ Assign unique IpAddress's to every host in topo ############\n"
    for h in range(10):
	host = net.get('h%s' % (h+1))
        ip=('10.0.0.%s/8' % (start+h+1))
        host.setIP(ip)

def CreateGRETunnel(net, remote):
    print "############ Create GRE tunnel between s1 and all level1 switches #######\n"
    result = os.system('sudo ovs-vsctl add-port s1 s1-gre1 -- set interface s1-gre1 type=gre options:remote_ip=%s' % remote)
    #print result

def setup(remote, start, n):
    print "############## Setting up lower level mininet vm #######################\n"
    net = LowMnNet(remote, n)
    AssignIp(net, start, n)
    CreateGRETunnel(net, remote)
    # XXX: check connectivity with level1 switches as well
    dumpNodeConnections(net.hosts)
    net.pingAll()
    CLI(net)
    net.stop()

if __name__ == '__main__':
    #selLogLevel('info')
    setup(sys.argv[1], int(sys.argv[2]), int(sys.argv[3]))

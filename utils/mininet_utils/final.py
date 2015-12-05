#!/usr/bin/python

#####################################################################
#
# USAGE: sudo python final.py 10 10 3
# arg1: No of hosts under each layer 2 switch
# arg2: No of layer 2 switches
# arg3: No of layer 1 switches
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
	def build(self, n, nn, nnn):
		count = 0
		topswitch = [None]*10
		for topS in range(nnn):
			topswitch[topS] = self.addSwitch('ts%s' %(topS+1))
		for s in range(nn):
			switch = self.addSwitch('s%s' % (s+1))
			for h in range(n):
				count = count+1
				# Each host gets 50%/n of system CPU
				host = self.addHost('h%s' % (count), cpu=.5/n, ip='10.0.0.%s' %(count))
				# 10 Mbps, 5ms delay, 10% loss, 1000 packet queue
				self.addLink(host, switch, bw=1, delay='1ms', loss=1, max_queue_size=1000, use_htb=True)
			for c in range(nnn):
				self.addLink(switch, topswitch[c], bw=1, delay='1ms', loss=1, max_queue_size=1000, use_htb=True)
		
def LowMnNet(n, nn, nnn):
	print "############ Instanitiate a mininet of topology LowMnTopo ###############\n"
	topo = LowMnTopo(n, nn, nnn)
	ctrl_port=6633
	net = Mininet(topo=topo, ipBase='10.0.0.0/8', autoSetMacs=True, host=CPULimitedHost, link=TCLink)
 	net.start()
	return net

def setup(n, nn, nnn):
	print "############## Setting up lower level mininet vm #######################\n"
	net = LowMnNet(n, nn, nnn)
	# XXX: check connectivity with level1 switches as well
	dumpNodeConnections(net.hosts)
	#    net.pingAll()
	CLI(net)
	net.stop()

if __name__ == '__main__':
	#selLogLevel('info')
	setup(int(sys.argv[1]), int(sys.argv[2]), int(sys.argv[3]))

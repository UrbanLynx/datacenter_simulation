#!/usr/bin/python

"""
This example shows how to create a simple network and
how to create docker containers (based on existing images)
to it.
"""

from mininet.net import Mininet
from mininet.node import Controller, Docker, RemoteController
from mininet.cli import CLI
from mininet.link import TCLink
from mininet.topo import Topo
from mininet.log import setLogLevel, info

import os
#import logging
import sys
import time
import datetime
import fileinput

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
        #self.ctrlsw.append(self.addSwitch('00'))

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
        #self.ctrlhost.append(self.addHost('hctrl', ip='10.0.0.254/8', cls=Docker, dimage='aborase/simdocker:v4'))

        # setup end hosts
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                for k in range (0, kval/2):
                    self.host.append(self.addHost('h%d%d%d' % (i, j, k), ip='10.%d.%d.%d/8' % (i, j, (k+2)), cls=Docker, dimage="aborase/simdocker:v4_1mb"))

    def link_setup(self, kval):
        # setup links between core and aggregation swtches
        for i in range(0, (kval*kval/4)):
            for j in range(0, kval):
                x = ((j*kval/2) + (i/(kval/2)))
                self.addLink(self.csw[i], self.asw[x], bw=40)

        # setup links between aggregation and edge switches
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                x = ((i*kval/2) + j)
                for k in range(0, kval/2):
                    y = ((i*kval/2) + k)
                    self.addLink(self.asw[x], self.esw[y], bw=10)

        # setup links between edge switches and end hosts
        for i in range(0, kval):
            for j in range(0, (kval/2)):
                x = ((i*kval/2) + j)
                for k in range(0, kval/2):
                    y = ((x*kval/2) + k)
                    self.addLink(self.esw[x], self.host[y], bw=2.5)
                    # control network
                    #self.addLink(self.ctrlsw[0], self.host[y], bw=1)

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

    #os.system("ovs-vsctl set Bridge 00 stp_enable=true")

def config_host(net, hosts, ipaddr, kval):
    # configure /etc/hosts file of each docker host
    print("**** Configuring each dockernet host ****\n")
    i = 0
    for h in net.hosts:
        time.sleep(1)
        name = h.cmd('hostname')
        #print(name[:12])
        # Collect all ip hostname pairs in a list
        hosts.append(name[:12])
        # add entry for localhost in /etc/hosts
        h.cmd('echo \"127.0.0.1 localhost\" >> /etc/hosts')
        h.cmd('echo \"127.0.1.1 %s\" >> /etc/hosts' % name[:12])
        print(name[:12] + "  " + ipaddr[i])
        i += 1

    # Now add every ent entry to each host's /etc/hosts file
    for h in net.hosts:
        for i in range (0, len(hosts)):
            h.cmd('echo \"%s  %s\" >> /etc/hosts' % (ipaddr[i], hosts[i]))

def start_varys(net):
    # start varys master on ctrlhost
    print("\n**** Starting varys-master daemon on control host h000 ****\n") 
    h000 = net.get('h000')
    result = h000.cmd('cd; cd group6/aalo; ./bin/start-all.sh')
    print(result)
    # start varys slave on each host
    print("\n**** Starting varys-slave daemon on all the  hosts ****\n")
    #h210 = net.get('h210')
    for h in net.hosts:
        time.sleep(1)
        if h000 != h:
            result = h.cmd('cd; cd group6/aalo; ./bin/varys-daemon.sh start varys.framework.slave.Slave varys://10.0.0.2:1606')
            print(result)
        #if h210 == h:
        #    break

def start_simulation(net, kval):
    # start simulation master on ctrlhost
    print("\n**** Starting simulation-master daemon on control host h000 ****\n") 
    # setup end hosts
    h000 = net.get('h000')
    for i in range(0, kval):
        for j in range(0, (kval/2)):
            for k in range (0, kval/2):
                time.sleep(1)
                host = net.get('h%d%d%d' % (i, j, k))
                if host != h000:
                    result = host.cmd('cd; cd group6/actor; ./utils/start-slave.sh 10.%d.%d.%d' % (i, j, (k+2)))
                else:
                    result = host.cmd('cd; cd group6/actor; ./utils/start-master.sh 10.0.0.2')
                print(result)
def get_times(net):
    for h in net.hosts:
        t = h.cmd("date +%s%3N")
        print(t)

def collect_data(hosts):
    timestamp = datetime.datetime.now().strftime('%Y-%m-%d_%H-%M-%S')
    os.system("mkdir ~/varys_%s" % timestamp)
    os.system("mkdir ~/varys_%s/console" % timestamp)
    os.system("mkdir ~/varys_%s/data" % timestamp)
    os.system("sudo cp ~/group6/actor/config/tasks ~/varys_%s/tasks" % timestamp)

    for i in range (0, len(hosts)):
        if i != 0:
                os.system("docker cp %s:/root/group6/actor/logs/ANALYS_Slavedata.log ~/varys_%s/data/analysis_slave_%d.log" % (hosts[i], timestamp, i))
                os.system("docker cp %s:/root/group6/actor/logs/COMPLETE_Slavedata.log ~/varys_%s/data/complete_slave_%d.log" % (hosts[i], timestamp, i))
                os.system("docker cp %s:/root/slave.log ~/varys_%s/console/console_slave_%d.log" % (hosts[i], timestamp, i))
        else:
                os.system("docker cp %s:/root/group6/actor/logs/ANALYS_Masterdata.log ~/varys_%s/data/analysis_master_%d.log" % (hosts[i], timestamp, i))
                os.system("docker cp %s:/root/group6/actor/logs/COMPLETE_Masterdata.log ~/varys_%s/data/complete_master_%d.log" % (hosts[i], timestamp, i))
                os.system("docker cp %s:/root/master.log ~/varys_%s/console/console_master_%d.log" % (hosts[i], timestamp, i))

def setup_files(master):
    #os.system("docker cp ~/group6/actor/configs/hosts %s:/root/group6/actor/configs/hosts" % master)
    os.system("docker cp ~/group6/actor/configs/tasks %s:/root/group6/actor/configs/tasks" % master)
    os.system("docker cp ~/group6/actor/configs/simulation.json %s:/root/group6/actor/configs/simulation.json" % master)

def setup_fattree_topo(kval):
    hosts = []
    ipaddrs = ['10.0.0.2',
               '10.0.0.3',
               '10.0.1.2',
               '10.0.1.3', 
               '10.1.0.2',
               '10.1.0.3',
               '10.1.1.2',
               '10.1.1.3',
               '10.2.0.2', 
               '10.2.0.3',
               '10.2.1.2',
               '10.2.1.3',
               '10.3.0.2',
               '10.3.0.3',
               '10.3.1.2',
               '10.3.1.3']

    # setup fat-tree topology
    print("\n**** Setting up fat-tree topology ****\n")
    topo = fat_tree_topo()
    topo.setup_topo(kval)

    print("\n**** Setting up all the network links ****\n")
    topo.link_setup(kval)

    # instantiate mininet network
    print("\n**** Instantiating netwrok ****\n")
    net = Mininet(topo=topo, controller=None, link=TCLink)
    net.addController('floodlight', controller=RemoteController, ip="127.0.0.1", port=6653)

    # start the netwrok
    net.start()
    print("\n**** Sleeping for 10 seconds ****\n")
    time.sleep(5)
    get_times(net)
    # configure the hosts
    config_host(net, hosts, ipaddrs, kval)
    
    #enable STP
    print("\n**** Sleeping for 5 seconds ****\n")
    #time.sleep(5)
    enable_stp(kval)

    # TODO: enable STP for control network as well.
    # check connectivity
    print("\n**** Sleeping for 5 seconds ****\n")
    #time.sleep(5)
    net.pingAll()

    # start varys daemons
    print("\n**** Sleeping for 5 seconds ****\n")
    #time.sleep(5)
    setup_files(hosts[0])
    start_varys(net)

    # start simulation
    print("\n**** Sleeping for 5 seconds ****\n")
    #time.sleep(5)
    start_simulation(net, kval)

    # grab mininet CLI
    CLI(net)

    # dump logs
    collect_data(hosts)

    net.stop()

if __name__ == '__main__':
    setLogLevel('info')
    if os.getuid() != 0:
        print("Run as a root \n")
    elif os.getuid() == 0:
        setup_fattree_topo(int(sys.argv[1]))

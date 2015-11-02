# Source file to generate topology based on a custom configuration file
# This module uses fnss and mininet library

import fnss
import sys
import os

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.link import TCLink
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel
from mininet.node import OVSController

def build_topology():

	# We use fat tree topology for datacenters
	kval = 0
	edgeLinkCapacity = [10, 'Mbps']
	aggrLinkCapacity = [100,'Mbps']
	coreLinkCapacity = [1,  'Gbps']
	linkDelay		 = [10, 'ns']

	# Get the value from the network.config file
	lines = open('./network.config', 'r').readLines()
	for line in lines:
		val = line.split()
		if val[0] == "K_VALUE":
			kval = int(val[1])
		elif val[0] == "EDGE_LINK_SPEED":
			edgeLinkCapacity[0] = val[1]
			edgeLinkCapacity[1] = val[2]
		elif val[0] == "AGGR_LINK_SPEED":
			aggrLinkCapacity[0] = val[1]
			aggrLinkCapacity[1] = val[2]
		elif val[0] == "CORE_LINK_SPEED":
			coreLinkCapacity[0] = val[1]
			coreLinkCapacity[1] = val[2]
		elif val[0] == "LINK_DELAY":
			linkDelay[0] = val[1]
			linkDelay[1] = val[2]

	if kval == 0:
		print "ERROR: Wrong value of K for a fat tree topo, exiting"
		sys.exit(0)

	# Build the topology using fnss
	topology = fnss.fat_tree_topology(kval)

	# Get link types
	link_types = nx.get_edge_attributes(topology, 'type')

	edge_leaf_links = [link for link in link_types
                if link_types[link] == 'edge_leaf']

    aggregation_edge_links = [link for link in link_types
                if link_types[link] == 'aggregation_edge']

    core_edge_links = [link for link in link_types
                if link_types[link] == 'core_edge']

    # Set the link speeds
    fnss.set_capacities_constant(topology, edgeLinkCapacity[0], edgeLinkCapacity[1], edge_leaf_links)
    fnss.set_capacities_constant(topology, aggrLinkCapacity[0], aggrLinkCapacity[1], aggregation_edge_links)
	fnss.set_capacities_constant(topology, coreLinkCapacity[0], coreLinkCapacity[1], core_edge_links)

	# Set default weight of 1 to all links
	fnss.set_weights_constant(topology, 1)

	# Set link delay to be 10 ns
	fnss.set_delays_constant(topology, linkDelay[0], linkDelay[1])

	# Generate the topology.xml file
	fnss.write_topology(topology, 'topology.xml')

	# Create mininet topology from fnss with renaming to mininet format
	mn_topo = fnss.to_mininet(topology, relabel_nodes=True)

	net = Mininet(topo=mn_topo, link=TCLink, controller=OVSController)
	net.start()

	# Dump host connections
	dumpNodeConnections(net.hosts)

	# Test network connectivity
	net.pingAll()


# If the file is executed call the build_topology method
if __name__ == "__main__":

	print "Generating topology based on network.config"

	# Check if the network.config file exists
	if os.path.isfile('./network.config'):
		build_topology()
	else:
		print "ERROR: network.config not found, exiting"
		exit(0)

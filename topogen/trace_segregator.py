####################################################################################
# Segregate the entries from network.trace file to .trace file of respective hsost #
# first line of the .trace file has to contaim the number of hosts.                #
# Rest of the lines in network.trace are expected to be of the form:               #
# src_hosts  dst_host  time      data_size  task_num  coflow_id                    #
# h1         h2        00:00:00  20         5         10                           #	
####################################################################################

import os
import re

def segregate_traces():
	# Read the network.trace into an list of lines
	lines = open('./network.trace', 'r').readlines()
	num_hosts = int(lines[0])

	# Create per-host .trace files
	host = []
	host = [open('./h%d.trace' % (i+1), 'w') for i in range(num_hosts)]

	# Read lines and re-direct to corresponding hosts .trace file
	for i in range(1, len(lines)):
		x = lines[i].split(" ", 1)
		h = int(re.sub('h', '', x[0]))
		host[h-1].write(x[1])

	# Close all the opened files.
	for i in range(num_hosts):
		host[i].close()

# If the file is executed call the build_topology method
if __name__ == "__main__":
        print ("Segragte network traces into per-host .trace files\n")

        # Check if the network.config file exists
        if os.path.isfile('./network.trace'):
                segregate_traces()
                print ("Done\n")
        else:
                print ("ERROR: network.trace not found, exiting\n")
                exit(0)

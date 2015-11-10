"""
Segregate the events from network_job.json file to per host trace file.
It assumes network_job.json containing traces to be present in the current directory.
The individual hpst traces are dumped in the files of the format 10.3.1.3_job.json
"""

import sys
import os
import re
import json

def segregate_traces():
	# Initialize j_data and j_hmap
	with open('./network_jobs.json','r') as jfile:
		j_data = json.load(jfile)

	with open('./hostname_mapping.json', 'r') as mfile:
		j_hmap = json.load(mfile)

	# Create per-host json data
	num_hosts = len(j_hmap)

	jdata = [{ "transfers" : []} for i in range(num_hosts)]

	# Iterate through the transfer array and re-direct entries to respective
	# src host's j_data.
	jtransfers = j_data['transfers']
	for event in jtransfers:
		src = event['srcAddress']
		src_ip = j_hmap[src]
		dst = event['dstAddress']
		dst_ip = j_hmap[dst]
		event['srcAddress'] = src_ip
		event['dstAddress'] = dst_ip
		x = src_ip.split(".")
		host_idx = (4*int(x[1]) + 2*int(x[2]) + int(x[3]) - 1)
		jdata[host_idx-1]["transfers"].append(event)

	# dump all the host j_data to .json files.
	for h in range(num_hosts):
		filename = "10.%d.%d.%d_job.json" % (h/4, (h%4)/2, (h%4)%2+2)
		with open(filename, 'w') as outfile:
			json.dump(jdata[h], outfile, indent=4)

if __name__ == "__main__":
        # Check if the network_jobs.json and hostname_mapping.json file exists
        if os.path.isfile('./network_jobs.json') & os.path.isfile('./hostname_mapping.json'):
                segregate_traces()
                print ("SUCCESS: Network job segregation complete.\n")
        else:
                print ("ERROR: Network job or the mapping file not found, exiting.\n")
                exit(0)

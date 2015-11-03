"""
Segregate the events from network.trace file to .xml file of respective hsost
It assumes network.xml containing traces to be present in the current directory.
The network traces need to be of following xml format, the order of propoerties
in an event can vary.

<event-schedule>
        <property name="t_unit" type="string">min</property>
        <property name="t_start" type="int">0</property>
        <property name="t_end" type="int">0</property>
        <event time="0">
                <property name="source" type="string">h1</property>
                <property name="duration" type="int">200</property>
                <property name="data_speed" type="int">500</property>
                <property name="destination" type="string">h2</property>
                <property name="coflow_id" type="int">1</property>
        </event>
</event-schedule>

INPUT: # of hosts
"""

import sys
import os
import re
import xml.etree.ElementTree as ET

# for pretty indenting of xml tags
def xml_indent(elem, level=0):
	i = "\n" + (level * "        ")
	if len(elem):
		if not elem.text or not elem.text.strip():
			elem.text = i + "        "
		if not elem.tail or not elem.tail.strip():
			elem.tail = i
		for elem in elem:
			xml_indent(elem, level + 1)
		if not elem.tail or not elem.tail.strip():
			elem.tail = i
	else:
		if level and (not elem.tail or not elem.tail.strip()):
			elem.tail = i

def segregate_traces(num_hosts):
	# Create per-host xml trees
	head_h = [0 for i in range(num_hosts)]

	# Initialize xml-tree head for each host
	for i in range(num_hosts):
		head_h[i] = ET.Element("event-schedule")

	# initialize xml-tree for network.xml
	tree = ET.parse('./network.xml')
	head = tree.getroot()

	# Push the first three properties (t_unit, t_start, t_end) to all hosts
	for prop in head:
		for i in range(num_hosts):
			head_h[i].append(prop)
		if prop.attrib['name'] == 't_end':
			break

	# Iterate over all the events one by one and push to appropriate host
	for event in head.findall('event'):
		# Find the 'source'
		for prop in event.findall('property'):
			if prop.attrib['name'] == 'source':
				break
		host = prop.text
		host_num = int(re.sub('h', '', host))
		# 'source' property is not needed and hence can be removed
		event.remove(prop)
		head_h[host_num-1].append(event)
	
	# Dump the host xml-trees to respective .xml files
	for i in range(num_hosts):
		xml_indent(head_h[i])
		ET.ElementTree(head_h[i]).write('./h%d.xml' % (i+1), encoding='utf-8')

if __name__ == "__main__":
        # Check if the network.trace file exists
        if os.path.isfile('./network.xml'):
                segregate_traces(int(sys.argv[1]))
                print ("SUCCESS: Network trace segregation complete.\n")
        else:
                print ("ERROR: Network trace not found, exiting.\n")
                exit(0)

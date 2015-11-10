"""
Generate Event Schedule
=======================
This script generates a trace file similar to the trace documented in MREMU github repo for 16 hosts.
"""
import fnss
import random
import networkx as nx
mappers = ['h1','h2','h3','h4','h5','h6','h7','h8','h9','h10','h11','h12']
reducers = ['h13','h14','h15','h16']
event_schedule = fnss.EventSchedule(t_start=0, t_unit='min')
event_schedule.add(time=0.93, event={'source': mappers[0], 'destination': reducers[0], "finishTime": 4.29, "name": "attempt_201312301708_0016_m_000014_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.93, event={'source': mappers[0], 'destination': reducers[1], "finishTime": 4.29, "name": "attempt_201312301708_0016_m_000014_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.93, event={'source': mappers[0], 'destination': reducers[2], "finishTime": 4.29, "name": "attempt_201312301708_0016_m_000014_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.93, event={'source': mappers[0], 'destination': reducers[3], "finishTime": 4.29, "name": "attempt_201312301708_0016_m_000014_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.136, event={'source': mappers[1], 'destination': reducers[0], "finishTime": 4.49, "name": "attempt_201312301708_0016_m_000010_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.136, event={'source': mappers[1], 'destination': reducers[1], "finishTime": 4.49, "name": "attempt_201312301708_0016_m_000010_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.136, event={'source': mappers[1], 'destination': reducers[2], "finishTime": 4.49, "name": "attempt_201312301708_0016_m_000010_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.136, event={'source': mappers[1], 'destination': reducers[3], "finishTime": 4.49, "name": "attempt_201312301708_0016_m_000010_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.067, event={'source': mappers[2], 'destination': reducers[0], "finishTime": 4.405, "name": "attempt_201312301708_0016_m_000011_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.067, event={'source': mappers[2], 'destination': reducers[1], "finishTime": 4.405, "name": "attempt_201312301708_0016_m_000011_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.067, event={'source': mappers[2], 'destination': reducers[2], "finishTime": 4.405, "name": "attempt_201312301708_0016_m_000011_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.067, event={'source': mappers[2], 'destination': reducers[3], "finishTime": 4.405, "name": "attempt_201312301708_0016_m_000011_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.185, event={'source': mappers[3], 'destination': reducers[0], "finishTime": 4.587, "name": "attempt_201312301708_0016_m_000003_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.185, event={'source': mappers[3], 'destination': reducers[1], "finishTime": 4.587, "name": "attempt_201312301708_0016_m_000003_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.185, event={'source': mappers[3], 'destination': reducers[2], "finishTime": 4.587, "name": "attempt_201312301708_0016_m_000003_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.185, event={'source': mappers[3], 'destination': reducers[3], "finishTime": 4.587, "name": "attempt_201312301708_0016_m_000003_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.899, event={'source': mappers[4], 'destination': reducers[0], "finishTime": 4.728, "name": "attempt_201312301708_0016_m_000009_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.899, event={'source': mappers[4], 'destination': reducers[1], "finishTime": 4.728, "name": "attempt_201312301708_0016_m_000009_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.899, event={'source': mappers[4], 'destination': reducers[2], "finishTime": 4.728, "name": "attempt_201312301708_0016_m_000009_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.899, event={'source': mappers[4], 'destination': reducers[3], "finishTime": 4.728, "name": "attempt_201312301708_0016_m_000009_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[5], 'destination': reducers[0], "finishTime": 4.373, "name": "attempt_201312301708_0016_m_000004_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[5], 'destination': reducers[1], "finishTime": 4.373, "name": "attempt_201312301708_0016_m_000004_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[5], 'destination': reducers[2], "finishTime": 4.373, "name": "attempt_201312301708_0016_m_000004_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[5], 'destination': reducers[3], "finishTime": 4.373, "name": "attempt_201312301708_0016_m_000004_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[6], 'destination': reducers[0], "finishTime": 4.411, "name": "attempt_201312301708_0016_m_000005_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[6], 'destination': reducers[1], "finishTime": 4.411, "name": "attempt_201312301708_0016_m_000005_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[6], 'destination': reducers[2], "finishTime": 4.411, "name": "attempt_201312301708_0016_m_000005_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[6], 'destination': reducers[3], "finishTime": 4.411, "name": "attempt_201312301708_0016_m_000005_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[7], 'destination': reducers[0], "finishTime": 4.427, "name": "attempt_201312301708_0016_m_000012_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[7], 'destination': reducers[1], "finishTime": 4.427, "name": "attempt_201312301708_0016_m_000012_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[7], 'destination': reducers[2], "finishTime": 4.427, "name": "attempt_201312301708_0016_m_000012_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.064, event={'source': mappers[7], 'destination': reducers[3], "finishTime": 4.427, "name": "attempt_201312301708_0016_m_000012_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.812, event={'source': mappers[8], 'destination': reducers[0], "finishTime": 4.19, "name": "attempt_201312301708_0016_m_000008_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.812, event={'source': mappers[8], 'destination': reducers[1], "finishTime": 4.19, "name": "attempt_201312301708_0016_m_000008_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.812, event={'source': mappers[8], 'destination': reducers[2], "finishTime": 4.19, "name": "attempt_201312301708_0016_m_000008_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.812, event={'source': mappers[8], 'destination': reducers[3], "finishTime": 4.19, "name": "attempt_201312301708_0016_m_000008_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.162, event={'source': mappers[9], 'destination': reducers[0], "finishTime": 4.516, "name": "attempt_201312301708_0016_m_000013_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.162, event={'source': mappers[9], 'destination': reducers[1], "finishTime": 4.516, "name": "attempt_201312301708_0016_m_000013_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.162, event={'source': mappers[9], 'destination': reducers[2], "finishTime": 4.516, "name": "attempt_201312301708_0016_m_000013_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=1.162, event={'source': mappers[9], 'destination': reducers[3], "finishTime": 4.516, "name": "attempt_201312301708_0016_m_000013_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[10], 'destination': reducers[0], "finishTime": 4.327, "name": "attempt_201312301708_0016_m_000006_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[10], 'destination': reducers[1], "finishTime": 4.327, "name": "attempt_201312301708_0016_m_000006_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[10], 'destination': reducers[2], "finishTime": 4.327, "name": "attempt_201312301708_0016_m_000006_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.996, event={'source': mappers[10], 'destination': reducers[3], "finishTime": 4.327, "name": "attempt_201312301708_0016_m_000006_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.967, event={'source': mappers[11], 'destination': reducers[0], "finishTime": 4.337, "name": "attempt_201312301708_0016_m_000002_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.967, event={'source': mappers[11], 'destination': reducers[1], "finishTime": 4.337, "name": "attempt_201312301708_0016_m_000002_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.967, event={'source': mappers[11], 'destination': reducers[2], "finishTime": 4.337, "name": "attempt_201312301708_0016_m_000002_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)
event_schedule.add(time=0.967, event={'source': mappers[11], 'destination': reducers[3], "finishTime": 4.337, "name": "attempt_201312301708_0016_m_000002_0", "type": "MAP", 'data_speed': 500, 'coflow_id': 1}, absolute_time=True)

#print event_schedule.event[0]
# Write topology and event schedule to files
#fnss.write_topology(topology, 'topology.xml')
fnss.write_event_schedule(event_schedule, 'event_schedule.xml')

taskFilename = 'tasks_test';
masterLogFilename = 'master.log';
slaveLogFilenames = {'slave1.log', 'slave2.log', 'slave3.log'} % test files
computeCCT(taskFilename, masterLogFilename, slaveLogFilenames)
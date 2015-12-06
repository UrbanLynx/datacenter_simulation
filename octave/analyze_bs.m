clear

numSlaves = 13;
numTasksPerExperiment = 22;

taskFilename = 'tasks';
taskFilepath = ['../',taskFilename];

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

masterLogFilename = 'analysis_master_0.log';

allSlaveLogFilenames = {'analysis_slave_1.log', 'analysis_slave_2.log', 'analysis_slave_3.log', 'analysis_slave_4.log', ...
    'analysis_slave_5.log', 'analysis_slave_6.log', 'analysis_slave_7.log', 'analysis_slave_8.log', ...
    'analysis_slave_9.log', 'analysis_slave_10.log', 'analysis_slave_11.log', 'analysis_slave_12.log', ...
    'analysis_slave_13.log', 'analysis_slave_14.log', 'analysis_slave_15.log'} ;
slaveLogFilenames = allSlaveLogFilenames(1:numSlaves);

% compute CCT
    [CCT, taskStartTime, taskEndTime, sendData, recData] = ...
        computeCCT3(taskFilepath, masterLogFilename, slaveLogFilenames);
    
    shift = min(sendData(:,3))
    sendDataShifted = sendData;
    sendDataShifted(:,3) = sendData(:,3)-shift
    recDataShifted = recData;
    recDataShifted(:,3) = recData(:,3)-shift

    
CCT_big = CCT(1:2:end);
CCT_small = CCT(2:2:end);

% check that different experiments in differnt tasks do not overlap
checkNoOverlap = timeBetweenTasks(taskStartTime, taskEndTime); 

figure;
title('CCT of small coflow')
plot(0:10, CCT_small/1e3)
xlabel('# of unlabelled flows')
ylabel('sec')

figure;
title('CCT of big coflow')
plot(0:10, CCT_big/1e3)
xlabel('# of unlabelled flows')
ylabel('sec')

figure;
plot(checkNoOverlap/1e3, '*')
ylabel('sec')
title('Time between tasks')
figure;
plot(checkNoOverlap/1e3, '*')
ylabel('sec')
title('Time between tasks')

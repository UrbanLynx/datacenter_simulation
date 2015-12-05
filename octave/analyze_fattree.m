clear

numSlaves = 11;
numTasksPerExperiment = 11;

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
    [CCT, taskStartTime, taskEndTime] = ...
        computeCCT2(taskFilepath, masterLogFilename, slaveLogFilenames);

CCT

% check that different experiments in differnt tasks do not overlap
checkNoOverlap = timeBetweenTasks(taskStartTime, taskEndTime); 

for i_exp = 1:length(CCT)/numTasksPerExperiment
figure;
ind = (i_exp-1)*numTasksPerExperiment+[1:numTasksPerExperiment];
plot(0:numTasksPerExperiment-1, CCT(ind)/1e3, '*-')
xlabel('# of unlabelled flows')
title('CCT')
ylabel('sec')
end

figure;
plot(checkNoOverlap/1e3, '*')
ylabel('sec')
title('Time between tasks')

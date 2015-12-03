clear

% TODO: remove comma from log entries

taskFilename = 'tasks_test';

[numTasks, totalNumFlows, numSlaves, slaveIDs] = ...
    getSimulationInfo(taskFilename) %;

% --Master log entries: task number, coflowID
% --Slave log entries: slaveID that generated this file (so every entry
% will be the same, sanity check), SEND/RECEIVE, coflowID, timestamp

% Parse master log
masterLogFilename = 'master.log';
taskCoflowMap = parseMasterLog(masterLogFilename);

% Parse slave logs
% list all slave log file names in a cell array?
slaveLogFilenames = {'slave1.log', 'slave2.log', 'slave3.log'} % test files
[sendData, receiveData] = parseSlaveLogs(slaveLogFilenames, totalNumFlows);

% get stats for each coflow (entry in task file)
CCT = Inf(1, numTasks); % coflow completion time
for ii = 1:numTasks
   % get info for this entry in task file
   % taskNum is probably equal to ii but this code does not assume that
    [taskNum, startTime, M, mapperIDs, ...
       R, reducerIDs, reducerPortNums] = parseTask(taskFilename, ii);
   % get coflowID for this task
   ind = find(taskCoflowMap(:,1)==taskNum);
   coflowID = taskCoflowMap(ind,2);
   fprintf('coflowID %d\n', coflowID);
   
   % 
   numFlows = M*R;
   sendInd = find(sendData(:,1)==coflowID);
   if length(sendInd) ~= numFlows
      fprintf('Not all flows were sent for coflowID %d\n', coflowID);
   end
   
   recInd = find(receiveData(:,1)==coflowID);
   if length(recInd) ~= numFlows
       fprintf('Not all flows were received for coflowID %d\n', coflowID);
   end
   
   sendEntries = sendData(sendInd,:)%;
   recEntries = receiveData(recInd,:)%;
   
   latestSendTime = max(sendEntries(:,3));
   latestRecTime = max(recEntries(:,3));

   CCT(ii) = latestRecTime - latestSendTime;
   
end











function [CCT, taskStartTime, taskEndTime] = computeCCT2(taskFilename, masterLogFilename, slaveLogFilenames)

[numTasks, totalNumFlows, numSlaves, slaveIDs] = ...
    getSimulationInfo(taskFilename);

fprintf('%d tasks, %d flows, %d slaves\n', numTasks, totalNumFlows, numSlaves);

% --Master log entries: task number, coflowID
% --Slave log entries: slaveID that generated this file (so every entry
% will be the same, sanity check), SEND/RECEIVE, coflowID, timestamp

% Parse master log
taskCoflowMap = parseMasterLog(masterLogFilename);

% Parse slave logs
[sendData, receiveData] = parseSlaveLogs(slaveLogFilenames, totalNumFlows);

% get stats for each coflow (entry in task file)
CCT = Inf(1, numTasks); % coflow completion time
taskStartTime = NaN(1,numTasks);
taskEndTime = NaN(1,numTasks);
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
   
   if length(sendInd)==0 
       fprintf('No flows were sent for this coflow\n');
       continue
   end
   
  if length(recInd)==0 
       fprintf('No flows were received for this coflow\n');
       continue
   end
   
   sendEntries = sendData(sendInd,:);
   recEntries = receiveData(recInd,:);
   
   latestSendTime = min(sendEntries(:,3));
   latestRecTime = max(recEntries(:,3));
   
  taskStartTime(ii) = min([sendEntries(:,3); recEntries(:,3)]);
   taskEndTime(ii) = max([sendEntries(:,3); recEntries(:,3)]);

   CCT(ii) = latestRecTime - latestSendTime;
   
end











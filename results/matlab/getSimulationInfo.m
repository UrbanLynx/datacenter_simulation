function [numTasks, totalNumFlows, numSlaves, slaveIDs] = getSimulationInfo(taskFilename)

% open file
fID = fopen(taskFilename, 'r');
if ( fID == -1 )
    error('could not open file %s', taskFilename);
end

% number of tasks in this simulation
header_str = fgetl(fID);
header = sscanf(header_str, '%d');
numTasks = header(1);

% number of unique slaves in this simulation
slaveIDs = [];
totalNumFlows = 0;  % across all coflows
for k=1:numTasks
    % parse task entry
    fscanf(fID, '%d', 2); % taskNum and startTime
    M = fscanf(fID, '%d', 1);          % number of mappers
    for m=1:M
        mapperID = fscanf(fID, '%d', 1);
        if isempty(find(slaveIDs==mapperID))
            slaveIDs = [slaveIDs, mapperID];
        end
    end
    R = fscanf(fID, '%d', 1);          % number of reducers
    for r=1:R
        reducerID  = fscanf(fID, '%d', 1);
        if isempty(find(slaveIDs==reducerID))
            slaveIDs = [slaveIDs, reducerID];
        end
        if ( fscanf(fID, '%c', 1) ~= ':' ); error('incorrect file format'); end
        fscanf(fID, '%f', 1); % data size
        if ( fscanf(fID, '%c', 1) ~= ':' ); error('incorrect file format'); end
        fscanf(fID, '%d', 1); % port number
    end
    totalNumFlows = totalNumFlows + M*R;
end

slaveIDs = sort(slaveIDs);
numSlaves = length(slaveIDs);

fclose(fID);

end
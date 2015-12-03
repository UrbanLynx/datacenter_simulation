function [sendData, receiveData] = parseSlaveLogs(slaveLogFilenames, totalNumFlows)

% constants
NUM_FIELDS = 3;     % coflowID, hostID (aka slaveID), timestamp

% SendData : each row is coflowID, hostID (aka slaveID), timestamp
sendData = zeros(totalNumFlows, NUM_FIELDS);
sendnum = 1;
% ReceiveData : each row is coflowID, hostID (aka slaveID), timestamp
receiveData = zeros(totalNumFlows, NUM_FIELDS);
recnum = 1;
for ii=1:length(slaveLogFilenames)
    fID = fopen(slaveLogFilenames{ii}, 'r');
    if ( fID == -1 )
        error('could not open file %s', slaveLogFilenames{ii});
    end
    while ~feof(fID)
        slaveID = fscanf(fID, '%d', 1);
        action = fscanf(fID, '%s', 1);
        coflowID = fscanf(fID, '%d', 1);
        timestamp = fscanf(fID, '%d', 1);
        if strcmp(action, 'SEND')
            sendData(sendnum,:) = [coflowID, slaveID, timestamp];
            sendnum = sendnum + 1;
        elseif strcmp(action, 'RECEIVE')
            receiveData(recnum,:) = [coflowID, slaveID, timestamp];
            recnum = recnum + 1;
        end
    end
    fclose(fID);
end

% sort by coflow number
[~, ind] = sort(receiveData(:,1));
for col = 1:NUM_FIELDS 
    receiveData(:,col) = receiveData(ind, col);
end
[~, ind] = sort(sendData(:,1));
for col = 1:NUM_FIELDS 
    sendData(:,col) = sendData(ind, col);
end

end
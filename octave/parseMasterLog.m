function taskNum_CoflowNum = parseMasterLog(masterLogFilename)

fID = fopen(masterLogFilename, 'r');
if ( fID == -1 )
    error('could not open file %s', masterLogFilename);
end

taskNum_CoflowNum = [];
while ~feof(fID)
    taskNum = fscanf(fID, '%d', 1);
    coflowID = fscanf(fID, '%d', 1);
    taskNum_CoflowNum = [taskNum_CoflowNum; taskNum, coflowID];
end

fclose(fID);

end
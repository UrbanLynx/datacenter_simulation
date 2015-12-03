function [taskNumber, startTime, M, mapperIDs, ...
            R, reducerIDs, reducerPortNums] = parseTask(filename, entry_num)

% open file
fID = fopen(filename, 'r');
if ( fID == -1 )
    error('could not open file %s', filename);
end

% check number of tasks in this file
header_str = fgetl(fID);
header = sscanf(header_str, '%d');
numTasks = header(1);
if entry_num > numTasks
    error('invalid task # specified');
end

% skip to the task number we want
for k=1:entry_num-1
    fgetl(fID);
end

% parse specified task 
taskNumber = fscanf(fID, '%d', 1);
startTime = fscanf(fID, '%d', 1);
M = fscanf(fID, '%d', 1);          % number of mappers
mapperIDs = NaN(1,M);
for m=1:M
    mapperIDs(m) = fscanf(fID, '%d', 1);
end
R = fscanf(fID, '%d', 1);          % number of reducers
reducerIDs = NaN(1,R);
dataSizes = NaN(1,R);
reducerPortNums = NaN(1,R);
for r=1:R
    reducerIDs(r) = fscanf(fID, '%d', 1);
    if ( fscanf(fID, '%c', 1) ~= ':' ); error('incorrect file format'); end
    dataSizes(r) = fscanf(fID, '%f', 1);
    if ( fscanf(fID, '%c', 1) ~= ':' ); error('incorrect file format'); end
    reducerPortNums(r) = fscanf(fID, '%d', 1);
end

fclose(fID);

end



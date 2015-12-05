function deltaT = timeBetweenTasks(taskStartTime, taskEndTime)   

numTasks = length(taskStartTime);
if length(taskEndTime) ~= numTasks
    error('input mismatch');
end

deltaT = NaN(1, numTasks-1);
for tasknum = 2:numTasks
    deltaT(tasknum-1) = taskStartTime(tasknum) - taskEndTime(tasknum-1);
end
    

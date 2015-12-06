send = sendDataShifted;
rec = recDataShifted;

figure; hold all
coflowID = 0;
ind = find(sendDataShifted(:,1)==coflowID);
scatter(send(ind,3), ones(length(ind),1), 'filled', 'r')
coflowID = 100;
ind = find(sendDataShifted(:,1)==coflowID);
scatter(send(ind,3), ones(length(ind),1), 'filled', 'b')

for pt = 1:10
    figure; hold all
    coflowID = 200*pt;
    ind = find(send(:,1)==coflowID);
    shiftthis = min(send(ind,3))
    scatter(send(ind,3)-shiftthis, ones(length(ind),1), 'filled', 'r')
        coflowID = 200*pt + 100;
    ind = find(send(:,1)==coflowID);
    scatter(send(ind,3)-shiftthis, ones(length(ind),1), 'filled', 'b')
    title(['data pt ', int2str(pt)])
end
    
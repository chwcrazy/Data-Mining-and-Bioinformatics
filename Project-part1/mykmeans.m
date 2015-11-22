function [finLabel, finC] = mykmeans(data, K, iter, eps)
%KMEAN Summary of this function goes here
%   Detailed explanation goes here
[n, m] = size(data);
maxrep = 10;


finLabel = NaN(n,1);
finC = NaN(K,m);
finSSE = inf;

%for ti = 1:maxrep
    
    Label = randsample(K,n,true);
    C = getCen(data, Label, K);

    change = Inf;
    SSE = getSSE(data, Label, C);
    it = 0;

    while(change > eps)

        newLabel = getNewLabel(data, C);
        newC = getCen(data, newLabel, K);
        newSSE = getSSE(data, newLabel, newC);
        change = abs(newSSE - SSE);
        Label = newLabel;
        C = newC;
        SSE = newSSE;

        it = it+1;
        if(it >= iter) 
            break
        end
    end
    
    if(SSE < finSSE)
        finLabel = Label;
        finC = C;
        finSSE = SSE;
    end

%end

end

function [newC] = getCen(data, Label, K)
[n, m] = size(data);
newC = zeros(K, m);
newCcnt = zeros(K,1);
for i = 1:n
    newC(Label(i),:) = newC(Label(i),:) + data(i,:);
    newCcnt(Label(i)) = newCcnt(Label(i)) + 1;
end
%{
newC
newCcnt
sum(newCcnt)
%}
for i = 1:K
    newC(i,:) = newC(i,:)./newCcnt(i);
end

end

function [newLabel] = getNewLabel(data, C)
[n, m] = size(data);
[k, p] = size(C);
newLabel = NaN(n,1);

for i = 1:n
    min_p = Inf;
    for j = 1:k
        nowDist = getDist(data(i,:), C(j,:));
        if(nowDist < min_p)
            min_p = nowDist;
            newLabel(i) = j;
        end
    end
end

end

function [val] = getSSE(data, Label, C)
val = 0;
[n, m] = size(data);

for i = 1:n
    val = val + (data(i,:) - C(Label(i),:)).^2;
end

end

function [val] = getDist(a, b)
val = sqrt(sum((a - b).^2));
end

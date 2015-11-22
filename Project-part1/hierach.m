function [val] = hierach(data)
%HIERACH Summary of this function goes here
%   Detailed explanation goes here
[n, m] = size(data);

d = NaN(n,n);
val = NaN(n,n);
label = NaN(1,n);
rest = NaN(1,n);

clustercenter = data;
clustercnt = ones(n,1);

for i = 1:n
    for j = i:n
        d(i,j) = getDist(data(i,:), data(j,:));
        d(j,i) = d(i,j);
    end
    label(i) = i;
    rest(i) = i;
end

val(1,:) = label;

len = n;

for it = 1 : n-1
    nowi = -1;
    nowj = -1;
    min_d = inf;
    for i = 1 : len
        for j = i+1:len
            if(d(i,j) < min_d)
                min_d = d(i,j);
                nowi = i;
                nowj = j;
            end
        end
    end


    %disp(d);

    
    %singel or complete link
    %
    for i = 1 : len
        for j = i+1:len
            if((i == nowi) || (i == nowj))
                %d(i,j) = min(d(nowi,j), d(nowj,j));
                 d(i,j) = max(d(nowi,j), d(nowj,j));
                d(j,i) = d(i,j);
            elseif((j == nowi) || (j == nowj))
                %d(i,j) = min(d(i,nowi), d(i,nowj));
                 d(i,j) = max(d(i,nowi), d(i,nowj));
                d(j,i) = d(i,j);
            end
        end
    end
    %}
    
    %by cluster center
    %{
    newcenter = (clustercenter(nowi,:).*clustercnt(nowi) + clustercenter(nowj,:).*clustercnt(nowj)) ./ (clustercnt(nowi) + clustercnt(nowj));
    clustercenter(nowi,:) = newcenter;
    clustercenter(nowj,:) = [];
    clustercnt(nowi) = clustercnt(nowi) + clustercnt(nowj);
    clustercnt(nowj) = [];
    
    for i = 1:len -1
        for j = i+1:len-1
            d(i,j) = getDist(clustercenter(i,:), clustercenter(j,:));
            d(j,i) = d(i,j);
        end
    end
    %}
    
    
    

    for i = 1:n
        if(label(i) == rest(nowj))
            label(i) = rest(nowi);
        end
    end
    
    val(it+1,:) = label;

    %{
    disp('nowi = ');
    disp(nowi);
    disp('nowj = ');
    disp(nowj);
    disp('label = ');
    disp(label);
    disp('rest = ');
    disp(rest);
    %}

    len = len -1;
    rest(nowj) = [];
    d(nowj,:) = [];
    d(:,nowj) = [];
     
end


end


function [val] = getDist(a, b)
val = sqrt(sum((a - b).^2));
end


function [val] = Correla(data, lab)
%CORRELATION Summary of this function goes here
%   Detailed explanation goes here
[n, m] = size(data);

d = NaN(n,n);
c = NaN(n,n);
d_bar = 0;
c_bar = 0;

for i = 1:n
    for j = 1:n
        if(lab(i) == lab(j))
            c(i,j) = 1;
            c_bar = c_bar + 1;
        else
            c(i,j) = 0;
        end
    end
end

for i = 1:n
    for j = 1:n
        di = getDist(data(i,:), data(j,:));
        d(i,j) = 1/(1+di);
        d_bar = d_bar + d(i,j);
    end
end

c_bar = c_bar/(n*n);
d_bar = d_bar/(n*n);

val = (sum(sum((d - d_bar).*(c - c_bar))))/(sqrt(sum(sum((d - d_bar).^2)))*sqrt(sum(sum((c - c_bar).^2))));

end

function [val] = getDist(a, b)
val = sqrt(sum((a - b).^2));
end


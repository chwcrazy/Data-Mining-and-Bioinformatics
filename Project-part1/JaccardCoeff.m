function [val] = JaccardCoeff(n, groundT, Label)
%JACCARDCOEFF Summary of this function goes here
%   Detailed explanation goes here
P = NaN(n, n);
C = NaN(n, n);

for i = 1:n
    for j = 1:n
        if(groundT(i) == groundT(j))
            P(i,j) = 1;
        else
            P(i,j) = 0;
        end
    end
end

for i = 1:n
    for j = 1:n
        if(Label(i) == Label(j))
            C(i,j) = 1;
        else
            C(i,j) = 0;
        end
    end
end

m = zeros(2,2);

%{
for i = 1:n
    for j = 1:n
        if((C(i,j) == 1))
            if(P(i,j) == 1)
                m(2,2) = m(2,2) + 1;
            else
                m(2,1) = m(2,1) + 1;
            end
        else
            if(P(i,j) == 1)
                m(1,2) = m(1,2) + 1;
            else
                m(1,1) = m(1,1) + 1;
            end
        end
    end
end
%}


for i = 1:n
    for j = 1:n
        m(C(i,j)+1,P(i,j)+1) = m(C(i,j)+1,P(i,j)+1) + 1;
    end
end

%sum(sum(m))
val = m(2,2)/(m(2,2) + m(2,1) + m(1,2));

end


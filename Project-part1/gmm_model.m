function [label, now_mu, now_sigma, now_pi] = gmm_model(data, k)
%GMM_MODE Summary of this function goes here
%   Detailed explanation goes here
[n, m] = size(data);
ini = (var(data) == 0);
x = [];
for i = 1:m
    if(ini(i) == 1)
        x = [x,i];
    end
end
data(:,x) = [];

[n, m] = size(data);

label = randsample(k,n,true);
now_mu = getCen(data, label, k);
now_pi = ones(1,k) ./ k;
now_sigma = zeros(m,m,k);
now_sigma(:,:,1) = diag(var(data));
for i = 2:k
    now_sigma(:,:,i) = now_sigma(:,:,i-1);
end


eps = 1e-15;

lastL = inf;

while(true)
    
    pdata = ones(n,k);
    for i = i:n
        for j = 1:k
            pdata(i,j) = gauss_prob(data(i,:), now_mu(j,:),now_sigma(:,:,j));
        end
    end
    
    %E step
    
    r = pdata;
    for i = 1:n
        r(i,:) = r(i,:) .* now_pi;
        r(i,:) = r(i,:) ./ sum(r(i,:));
    end
    
    %M step
    for i = 1:k
        now_pi(i) = sum(r(:,i))/n;
    end
    
    for i = 1:k
        now_mu(i,:) = (r(:,i)' * data) ./ sum(r(:,i));
    end
    
    for i = 1:k
        sigma_k = zeros(m,m);
        for j = 1:n
            tmp = data(j,:) - now_mu(i,:);
            sigma_k = sigma_k + r(j,i) .* (tmp'*tmp);
        end
        now_sigma(:,:,i) = diag(diag(sigma_k ./ sum(r(:,i))));
    end
    
    L = sum(log(pdata*now_pi'));
    if (abs(L-lastL) < eps)
        break;
    end
    lastL = L;
    
    
end

label = NaN(n,1);

for i = 1:n
    minp = -1;
    for j = 1:k
        if(minp < pdata(i,j))
            minp = pdata(i,j);
            label(i) = j;
        end
    end
end


end

function [val] = gauss_prob(data, mu, sigma)
[n, m] = size(data);

tmp = data - mu;

val = 1/sqrt((2*pi)^m * det(sigma)) * exp(-0.5*(tmp / sigma * tmp'));

end

function [newC] = getCen(data, Label, K)
[n, m] = size(data);
newC = zeros(K, m);
newCcnt = zeros(K,1);
for i = 1:n
    newC(Label(i),:) = newC(Label(i),:) + data(i,:);
    newCcnt(Label(i)) = newCcnt(Label(i)) + 1;
end

for i = 1:K
    newC(i,:) = newC(i,:)./newCcnt(i);
end

end


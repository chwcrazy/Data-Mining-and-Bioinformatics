clear;clc

data = dlmread('cho.txt');
% newdata = dlmread('iyer_result.txt');
% newdata(363,:) = [];
%data(363,:) = [];


[n,m] = size(data);
bb = data(:,3:m);
bb = zscore(bb);



[~,score,lat] = pca(bb);

%  figure;
%  gscatter(score(:,1), score(:,2), newdata(:,2));
%  jacc_hadoop = JaccardCoeff(n,data(:,2),newdata(:,2))
%  corre_hadoop = Correla(bb,newdata(:,2))
%  
% ground truth
 figure;
 gscatter(score(:,1), score(:,2), data(:,2));
 jacc_truth = JaccardCoeff(n,data(:,2),data(:,2))
 corre_truth = Correla(bb,data(:,2))
 
 %k-means
[index, c] = mykmeans(bb, 10, 1000, 0.00001);

 figure;
 gscatter(score(:,1), score(:,2), index);
 jacc_kmeans = JaccardCoeff(n,data(:,2),index)
 corre_kmeans = Correla(bb,index)
 
 %hierach
 val = hierach(bb);
 hielab = val(n-9,:);
 figure;
 gscatter(score(:,1), score(:,2), hielab);
 jacc_hierach = JaccardCoeff(n,data(:,2),hielab)
 corre_hierach = Correla(bb,hielab)


 %gmm
 [gmmlab,now_mu, now_sigma, now_pi] = gmm_model(bb,10);
 figure;
 gscatter(score(:,1), score(:,2), gmmlab);
 jacc_gmm = JaccardCoeff(n,data(:,2),gmmlab)
 corre_gmm = Correla(bb,gmmlab)






% 
% val = hierach(bb);
% [gmmlab,now_mu, now_sigma, now_pi, pdata] = gmm_mode(bb,10);
% 
% 
% 
% [lab] = kmeans(data(:,3:m),5);
% 
% comp1 = JaccardCoeff(n,data(:,2),lab)
% comp2 = JaccardCoeff(n,data(:,2),index)
% comp3 = JaccardCoeff(n,data(:,2),val(n-8,:))
% 
% 
% corre = Correla(bb,data(:,2))
% 
% 
% lat(1)/sum(lat)
% lat(2)/sum(lat)
% plot(score(:,1) , score(:,2),'.');
% gscatter(zscore(score(:,1)), zscore(score(:,2)), data(:,2));
%  figure;
%  gscatter(score(:,1), score(:,2), data(:,2));
% figure;
% gscatter(score(:,1), score(:,2), lab);
%  figure;
%  gscatter(score(:,1), score(:,2), index);
%  figure;
%  gscatter(score(:,1), score(:,2), newdata(:,2));
% figure;
% gscatter(score(:,1), score(:,2), gmmlab);
% figure;
% gscatter(score(:,1), score(:,2), val(n-8,:));
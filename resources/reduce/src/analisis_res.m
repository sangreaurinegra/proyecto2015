clear all
close all
fid=fopen('list_images.txt');
A=textscan(fid,'%s','delimiter', '\n');
fclose(fid);
imname=A{1};
nname=length(imname);

PARAM={
'LONGITUD'
'LATITUD'
'HEIGHT'
'NUMCR'
'MEALENCR'
'STDLENCR'
'SWELENCR'
'TOTFLUCR'
'MEAFLUCR'
'STDFLUCR'
'SKWFLUCR'
};
nt=size(PARAM,1);
for i=1:nt
    tok{i}=PARAM{i};
    if length(tok{i}) < 8
        for j=length(tok{i})+1:8
            tok{i}(j)=' ';
        end
    end
    tok{i}(9)='=';
end

for im=1:nname

fid=fopen([imname{im} '_res.txt']);
A=textscan(fid,'%s','delimiter', '\n');
fclose(fid);
C=A{1};

% the engine to search for the values keywords in the cell C
for i=1:nt
     ix=strfind(C,tok{i});
     j=find(cellfun('length',ix)>0);
     ib=strfind(C{j},'/'); % discard data after /
     ie=strfind(C{j}(1:ib-1),'=');
     ig=strfind(C{j}(1:ie-1),'-');
     param=C{j}(1:ib-1);
     if length(ig) > 0  % if there is a symbol - in the keyword
         for k=1:length(ig)
             param(ig(k))='_'; % change - by _
         end
     end
     param(find(param == ' '))=[];
     eval([param ';']) % assign value of keyword
end

res(im) = struct('LONGITUD',[LONGITUD],'LATITUD',[LATITUD],'HEIGHT',[HEIGHT], ...
    'NUMCR',[NUMCR],'MEALENCR',[MEALENCR],'STDLENCR',[STDLENCR], ...
    'SWELENCR',[SWELENCR],'TOTFLUCR',[TOTFLUCR],'MEAFLUCR',[MEAFLUCR], ...
    'STDFLUCR',[STDFLUCR],'SKWFLUCR',[SKWFLUCR]);

end

clear LONGITUD LATITUD HEIGHT NUMCR MEALENCR STDLENCR SWELENCR TOTFLUCR ...
    MEAFLUCR STDFLUCR SKWFLUCR

for i=1:nt
    eval([PARAM{i} '=extractfield(res,''' PARAM{i} ''');'])
end

figure
hist(NUMCR,50)
xlabel('Length of CR','FontSize',12)
ylabel('Histogram','FontSize',12)
print -r300 -dpng histo_length.png

figure
hist(TOTFLUCR,50)
xlabel('Flux of CR','FontSize',12)
ylabel('Histogram','FontSize',12)
print -r300 -dpng histo_flux.png

COORD= arrayfun(@(x,y)([x;y]),deg2rad(90-LATITUD),deg2rad(mod(LONGITUD,360)),'UniformOutput',false);
nSide=2;
hpix = ang2pix(nSide, COORD);
npix=1:nSide2nPix(nSide);
nimagpix=hist(hpix,npix);


col=colormap(jet);
ncol=length(col);
col=[0 0 0; col];

minI=min(nimagpix(find(nimagpix>0)));
maxI=max(nimagpix);
figure
hp3d(nimagpix)
caxis=[0 minI:(maxI-minI)/(ncol-1):maxI];
colormap(col)
title('Spatial distribution of the number of analysed images','FontSize',12)
print -r300 -dpng dist_spa_number_ima.png

for i=npix
    sel=find(hpix==i);
    meanNUMCR(i)=mean(NUMCR(sel));
    meanTOTFLUCR(i)=mean(TOTFLUCR(sel));
end

minN=min(meanNUMCR);
maxN=max(meanNUMCR);
meanNUMCR(isnan(meanNUMCR))=0;
figure
hp3d(meanNUMCR)
caxis=[0 minN:(maxN-minN)/(ncol-1):maxN];
colormap(col)
title('Spatial distribution of the number of CR','FontSize',12)
print -r300 -dpng dist_spa_number_CR.png

minF=min(meanTOTFLUCR);
maxF=max(meanTOTFLUCR);
meanTOTFLUCR(isnan(meanTOTFLUCR))=0;
figure
hp3d(meanTOTFLUCR)
caxis=[0 minF:(maxF-minF)/(ncol-1):maxF];
colormap(col)
title('Spatial distribution of the Total Flux of CR','FontSize',12)
print -r300 -dpng dist_spa_flux_CR.png


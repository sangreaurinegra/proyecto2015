% Script para el analisis de CR y calculo de la posicion del HST

% INPUT
% Requiere como entrada un archivo list_images.txt con los nombres de las
% imagenes (uno por linea), por ej.
% iaa901jxq
% iaa901k0q

% De cada imagen requiere los siguientes archivos:
% XXX_raw.cro.fits    - imagen de CR, salida de xzap
% XXX_raw.fits.head   - dump del header del raw
% XXX_spt.fits.head   - dump del header del spt

% OUTPUT
% Como salida se crea por cada imagen un archivo con los resultados con el
% nombre
% XXX_res.txt
% Tiene un formato de KEYWORD= .....
%
% Requiere las siguientes funciones
% intrinsecas de Matlab
%    skewness, fitsread, im2bw, bwconncomp , ecef2geodetic
% de la comunidad
%    date2jd.m, lst.m, nutation.m, obliquity.m, rad2deg.m
%    cal_pos.m - calculo de la posicion del HST
%

close all
clear all

fid=fopen('list_images.txt');
A=textscan(fid,'%s','delimiter', '\n');
fclose(fid);
imname=A{1};

for im=1:length(imname)
% leo el archivo fits
A=fitsread([imname{im} '_raw.fits.conv.fits.cro.fits']);
A(:,2103:2108)=0;
% convierto la imagen a BW
BW=im2bw(A,1);

% calculo de las posiciones
cal_pos

% normalizo por el tiempo de exposicion
A=A/EXPTIME;

% hallo los objetos conexos
CC = bwconncomp(BW);
% listo cuantos pixeles tiene cada objeto conexo
numPixels = cellfun(@numel,CC.PixelIdxList);
% estadistica de los largos de los CR
meanlen=mean(numPixels);
stdlen=std(numPixels);
sklen=skewness(numPixels);

% determino el flujo total sobre los rayos cosmicos
fluxCR=sum(sum(A));
% estadistica de los flujos de los CR
meanflux=fluxCR/CC.NumObjects;

for i=1:length(CC.PixelIdxList)
    fluxCRpix(i)=sum(A(CC.PixelIdxList{i}));
end
stdflux=std(fluxCRpix);
skflux=skewness(fluxCRpix);

fid=fopen([imname{im} '_res.txt'],'w');
fprintf(fid,'FILENAME= %s\n',['''' imname{im} '''']);
fprintf(fid,'%s\n',PARAM{1});
fprintf(fid,'%s\n',PARAM{2});
fprintf(fid,'JD      = %14.5f / Julian Date\n',JD);
fprintf(fid,'%s\n',PARAM{3});
fprintf(fid,'%s\n',PARAM{4});
fprintf(fid,'%s\n',PARAM{5});
fprintf(fid,'%s\n',PARAM{6});
fprintf(fid,'LONGITUD= %f      / degrees\n',LONGITUDE);
fprintf(fid,'LATITUD = %f      / degrees\n',LATITUDE);
fprintf(fid,'HEIGHT  = %f      / km from geoid surface\n',HEIGHT);

fprintf(fid,'\n     /  RESULTS OF THE ANALYSIS OF COSMIC RAYS (CR)  \n\n');

fprintf(fid,'NUMCR   = %d      / Number of CR/sec\n',CC.NumObjects/EXPTIME);
fprintf(fid,'MEALENCR= %f      / Mean length of CR in pixels\n',meanlen);
fprintf(fid,'STDLENCR= %f      / Std of the length of CR in pixels\n',stdlen);
fprintf(fid,'SWELENCR= %f      / Skewness of the length of CR in pixels\n',sklen);
fprintf(fid,'TOTFLUCR= %15.9g      / Total flux of CR in ADUs/sec\n',fluxCR);
fprintf(fid,'MEAFLUCR= %g      / Mean flux of CR in ADUs/sec\n',meanflux);
fprintf(fid,'STDFLUCR= %g      / Std of the flux of CR in ADUs/sec\n',stdflux);
fprintf(fid,'SKWFLUCR= %g      / Skewness of the flux of CR in ADUs/sec\n',skflux);
fclose(fid);

end

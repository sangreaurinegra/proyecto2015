fid=fopen([imname{im} '_spt.fits.head']);
B=textscan(fid,'%s','delimiter', '\n');
fclose(fid);
C1=B{1};

fid=fopen([imname{im} '_raw.fits.head']);
B=textscan(fid,'%s','delimiter', '\n');
fclose(fid);
C2=B{1};    

C=[C1 ; C2];
clear C1 C2

% ...the tokens = keywords
tok={
   'DATE-OBS='
   'TIME-OBS='
   'EXPTIME ='
   'POSTNSTX='
   'POSTNSTY='
   'POSTNSTZ='
};

% The X,Y,Z coordinates are in the geocentric J2000.0 inertial coordinate 
% system. This is a right-handed coordinate system centered in the Earth, 
% with the X axis pointing towards the vernal equinox for the year 2000, 
% the Z-axis pointing towards the north celestial pole for the year 2000, 
% and the Y axis orthogonal to both.

% the engine to search for the values keywords in the cell C
nt=size(tok,1);
d=cell(nt,1);
clear PARAM
for i=1:nt
     ix=strfind(C,tok{i});
     j=find(cellfun('length',ix)>0);
     PARAM{i}=C{j};
     ib=strfind(C{j},'/'); % discard data after /
     ie=strfind(C{j}(1:ib-1),'=');
     ig=strfind(C{j}(1:ie-1),'-');
     param=C{j}(1:ib-1);
     if length(ig) > 0  % if there is a symbol - in the keyword
         for k=1:length(ig)
             param(ig(k))='_'; % change - by _
         end
     end
     eval([param ';']) % assign value of keyword
end

POS=[POSTNSTX POSTNSTY POSTNSTZ]';  % Position vector in X,Y,Z HST system

% separate DATE_OBS into YEAR,MONTH,DAY
ig=strfind(DATE_OBS,'-');
YEAR=str2num(DATE_OBS(ig(1)-4:ig(1)-1));
MONTH=str2num(DATE_OBS(ig(1)+1:ig(1)+2));
DAY=str2num(DATE_OBS(ig(2)+1:ig(2)+2));
% separate TIME_OBS into HOUR,MINUTE,SECOND
ig=strfind(TIME_OBS,':');
HOUR=str2num(TIME_OBS(ig(1)-2:ig(1)-1));
MINUTE=str2num(TIME_OBS(ig(1)+1:ig(1)+2));
SECOND=str2num(TIME_OBS(ig(2)+1:ig(2)+2));

DISTANCE=sqrt(POSTNSTX^2+POSTNSTY^2+POSTNSTZ^2);
LATITUDE=rad2deg(atan(POSTNSTZ/DISTANCE));
RA=mod(rad2deg(atan2(POSTNSTY,POSTNSTX)),360);
JD=date2jd(YEAR,MONTH,DAY,HOUR,MINUTE,SECOND);
% GMST - Greenwich Mean Sidereal Time in radian
GMST=lst(JD,0)*2*pi;
% GMST = Hour angle of Vernal Equinox 

% Compute rotation matrix around axis Z with angle GMST
Rot=[cos(GMST) sin(GMST) 0; -sin(GMST) cos(GMST) 0; 0 0 1];
POSR=Rot*POS; % Rotated system with the X-axis in the Greenwich meridian

semia=6378.137; % Earth semimajor axis - ellipsoid WGS84
finv=298.257223563; % Inverse of flattening - ellipsoid WGS84
f=1/finv;
ecc=sqrt(2*f-f^2);  % eccentricity

% convert X,Y,Z into geodetic coordinates
[PHI, LAMBDA, HEIGHT] = ecef2geodetic(POSR(1), POSR(2), POSR(3), [semia ecc]);

LONGITUDE=rad2deg(LAMBDA);
LATITUDE=rad2deg(PHI);


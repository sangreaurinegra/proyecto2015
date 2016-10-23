function LST=lst(JD,EastLong,STType);
%--------------------------------------------------------------------
% lst function         Local Sidereal Time, (mean or apparent),
%                    for vector of JD's and a given East Longitude.
% input  : - Vector of JD, in UT1 time scale.
%          - East Longitude in radians.
%          - Sidereal Time Type,
%            'm' - Mean (default).
%            'a' - apparent.
% output : - vector of LST in fraction of day.
%    By  Eran O. Ofek           August 1999
%--------------------------------------------------------------------
RAD = 57.29577951308232;

if (nargin==2),
   STType = 'm';
elseif (nargin==3),
   % do nothing
else
   error('Illigal number of input arguments');
end


% convert JD to integer day + fraction of day
TJD = floor(JD - 0.5) + 0.5;
DayFrac = JD - TJD;

T = (TJD - 2451545.0)./36525.0;

GMST0UT = 24110.54841 + 8640184.812866.*T + 0.093104.*T.*T - 6.2e-6.*T.*T.*T;

% convert to fraction of day in range [0 1)
GMST0UT = GMST0UT./86400.0;

GMST0UT = GMST0UT - floor(GMST0UT);
LST = GMST0UT + 1.0027379093.*DayFrac + EastLong./(2.*pi);
LST = LST - floor(LST);


switch STType
 case {'m'}
    % do nothing
 case {'a'}
    % calculate nutation
    NutMat = nutation(JD);
    Obl    = obliquity(JD);
    EquationOfEquinox = (RAD.*3600).*NutMat(:,1).*cos(Obl)./15;
    LST = LST + EquationOfEquinox./86400;    
 otherwise
    error('Unknown sidereal time type');
end

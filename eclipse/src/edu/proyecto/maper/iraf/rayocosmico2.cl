procedure rayocosmico2
struct *inputFile
struct *inputFile2
struct *list
struct *lista
struct *ldump_raw
struct *ldump_spt
struct *data
struct *darks
struct *dl1
struct *dl3
struct *dl2
begin
	string s4, s5, s6, s7
	string imagen
	s7 = ""
	inputFile="input.txt"
	!ls *.fits > fitsFiles.txt
	if(fscan(inputFile,s1)!=EOF)
	{
		hselect(s1//"_raw.fits[1]","TARGNAME", expr="yes") | scan(s2)
		hselect(s1//"_spt.fits[1]","TARGNAME", expr="yes") | scan(s3)
		hselect(s1//"_raw.fits[1]","CHINJECT", expr="yes") | scan(s4)
		hselect(s1//"_spt.fits[1]","CHINJECT", expr="yes") | scan(s5)
		hselect(s1//"_raw.fits[1]","FLASHCUR", expr="yes") | scan(s6)
		hselect(s1//"_spt.fits[1]","FLASHCUR", expr="yes") | scan(s7)

		b1 = (s2 == "DARK" && s3 == "DARK")
		b1 = b1 && (s4 == "NONE" || s4 == "NONE") && (s5 == "NONE" || s5 == "NONE")
		b1 = b1 && (s6 != "LOW" && s7 != "LOW")

		if(b1)
		{
			imhead(s1//"_raw.fits[1]", l+ , > s1//"_raw.fits"//".head")
			imhead(s1//"_spt.fits[1]", l+ , > s1//"_spt.fits"//".head")
			
			imcopy( s1//"_raw.fits[1]" , s1//"_raw.fits.conv")
			imagen=s1//"_raw.fits.conv"
			chpixtype( imagen, imagen, newpixty = "real" )
			
			!touch darks
			!touch cdarks
			!rm darks
			!rm cdarks

			print(s1//"_raw.fits.conv.fits", >> "darks")
			print(s1//"_raw.fits.conv.fits.crr", >> "cdarks")
			
			
			xzap.verbose = yes            #  Verbose output?
			xzap.zmin = 2000.           	  #  Minimum data value for fmedian filter
			xzap.zmax = 4000.            #  Maximum data value for fmedian filter
			xzap.zboxsz = 10              #  Box size for fmedian filter
			xzap.skyfiltsize = 15         #  Median filter size for local sky evaluation
			xzap.subsample = 0            #  Block averaging factor before median filtering
			xzap.ngrowobj = 3             #  Number of pixels to flag as buffer around objects
			xzap.nrings = 0               #  Number of pixels to flag around CRs
			xzap.nsigneg = 0.             #  Number of sky sigma for negative zapping
			xzap.nsigobj = 1.5            #  Number of sky sigma for object identification
			xzap.nsigrej = 2.0            #  The n-sigma sky rejection parameter
			xzap.maxiter = 20             #  The maximum number of iterations
			xzap.statsec = ""             #  Image section to use for computing sky sigma
			xzap.nsigzap = 1.             #  Zapping threshold in number of sky sigma
			xzap.del_crmask = yes		  #  Delete cosmic ray mask after execution?
			xzap.checklimits = yes        #  Check min and max pix values before filtering?
			xzap.del_wimages = yes        #  Delete working images after execution?
			xzap.del_wmasks = yes         #  Delete working .pl masks after execution?
			xzap.imglist = ""             
			xzap.outimglist = ""             
			xzap.omskimglist = ""             
			xzap.crmskimglist = ""             
			flpr 0
			
			xzap( inlist = "@darks", omasks = "u5d2012li_bpx.fit", outlist = "@cdarks", crmasks= ".crm" )

			#!touch cronly
			#!rm cronly
			#print(s1//"_raw.fits.conv.fits.cro", >> "cronly")
			
			imarith( s1//"_raw.fits.conv.fits", '-', s1//"_raw.fits.conv.fits.crr", s1//"_raw.fits.conv.fits.cro", pixtype="real", calctype="real" )

			hselect(s1//"_raw.fits[1]", fields="$I,$DATE-OBS,$TIME-OBS,$EXPSTART,$EXPEND,$EXPTIME,$SUNANGLE,$MOONANGL,$SUN_ALT,$PA_V3", expr="yes", missing="n/a", >> "rawdataC.txt")

			hselect(s1//"_spt.fits[1]", fields="$I,DATE,FILENAME,INSTRUME,EQUINOX,ELON_REF,ELAT_REF,GLON_REF,GLAT_REF,UTC0,PSTRTIME,PSTPTIME,RA_V1,DEC_V1,POSTNSTX,POSTNSTY,POSTNSTZ,VELOCSTX,VELOCSTY,VELOCSTZ,RA_SUN,DEC_SUN,RA_MOON,DEC_MOON", expr="yes", missing="n/a", >> "sptdataC.txt")

			!tr '-' ' ' < rawdataC.txt > rawdata.txt
			!tr '-' ' ' < sptdataC.txt > sptdata.txt
			!rm rawdataC.txt
			!rm sptdataC.txt

			!sh ./sextractor.sh
		}
	}
end

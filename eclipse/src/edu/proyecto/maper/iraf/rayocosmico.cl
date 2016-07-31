# RAYOCOSMICO v1.3 - Santiago Roland (sroland@oalm.gub.uy) 21/02/2014
# Ejecutar el script en la misma carpeta donde estan las imagenes en formato *.fits raw y spt
procedure rayocosmico
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


	int k, n, m
	string s1, s2, nombre, imagen
	k=1
	# Limpio antes de empezar
	# Si existe alguno de los archivos los borra
	if(access("fin.lock")){del("fin.lock")}	
	if(access("inicio.lock")){del("inicio.lock")}	
	#if(access("CosmicRaysProc")){del("CosmicRaysProc")}	
	if(access("junk")){del("junk")}	
	if(access("data")){del("data")}	
	if(access("darks")){del("darks")}	
	if(access("pdarks")){del("pdarks")}	
	if(access("cdarks")){del("cdarks")}	
	if(access("crrdarks")){del("crrdarks")}	
	if(access("cronly")){del("cronly")}	
	if(access("lista.txt")){del("lista.txt")}	
	if(access("nomatch.txt")){del("nomatch.txt")}	
	if(access("files1.lis")){del("files1.lis")}	
	if(access("tmd")){del("tmd")}	
	if(access("rawdata.txt")){del("rawdata.txt")}
	if(access("sptdata.txt")){del("sptdata.txt")}
	if(access("rawdataC.txt")){del("rawdataC.txt")}
	if(access("sptdataC.txt")){del("sptdataC.txt")}

	if(access("default.sex")){del("default.sex")}
	if(access("rawimhead.txt")){del("rawimhead.txt")}
	if(access("sptimhead.txt")){del("sptimhead.txt")}
	if(access("flashdel.lis")){del("flashdel.lis")}
	if(access("lista2.txt")){del("lista2.txt")}
	if(access("junk2")){del("junk2")}	
	if(access("tmd2")){del("tmd2")}	
	if(access("junk3")){del("junk3")}	
	if(access("tmd3")){del("tmd3")}	
	if(access("tmd2_")){del("tmd2_")}	
	if(access("move2spt")){del("move2spt")}	
	if(access("move2spt2")){del("move2spt2")}	
	if(access("tmd2spt")){del("tmd2spt")}	
	if(access("list_images0.txt")){del("list_images0.txt")}
	if(access("list_images.txt")){del("list_images.txt")}
	if(access("list_images1.txt")){del("list_images1.txt")}
	if(access("list_images2.txt")){del("list_images2.txt")}
	if(access("list_images3.txt")){del("list_images3.txt")}

	#el comando touch se usa para modificar la fecha ultimo acceso a la actual. Como el archivo fue borrado previemente, se crea uno nuevo.
	!touch inicio.lock
	
	# se borran los archivos indicados
	!rm -rf CosmicRaysProc
	!rm -f _*.lis
	!rm -f *.head
	

	!rm -f *.pl
	!rm -f _*.fits
	!rm -f *.crr.fits
	!rm -f *.cro.fits
	!rm -f *conv.fits
	!rm -f *.cro.fits
	!rm -f *.head
	!rm -f *.cat
	!rm -f CosmicRaysProc/default.sex.0
	!rm -f CosmicRaysProc/sextractor.sh
	!rm -f CosmicRaysProc/default.param
	!rm -rf bad-flash-charge

	# si me quiero traer backups de alguna lado...
	!cp -f ../original/*.* .
	
	# se listan todos los archivos fits en el archivo files.lis
	!ls *.fits > files.lis
	# se crea un archivo files1.lis con los nombres de los archivos fits pero modificados a .fits[1], y se borra el archivo creado previamente
	!sed 's/.fits/.fits[1]/g' files.lis > files1.lis
	!rm files.lis
	
	# ###############################################
	# Genero una lista con los tipos de imagenes presentes en la carpeta BIAS, DARKS, etc
	# Esto necesita modificarse para que no descarte todas las imagenes... work in progress
	# Se crea el archivo lista con los nombres de archivo y los tipos de imagenes
	lista="files1.lis"
	while(fscan(lista,s1)!=EOF)
		{
		hselect(s1,"$I,TARGNAME", expr="yes", >> "lista.txt")
		}
	!rm files1.lis

	# Remuevo todas las lineas que contengan la palabra DARK
	!sed '/DARK/d' lista.txt > tmd
	# Remuevo las terminaciones [1] y lo llamo junk, ahora es una lista de imagenes a borrar
	!sed 's/\[.*$//g' tmd > junk
	# Borro todas las imagenes de junk
	!rm junk
	!rm tmd
	!rm lista.txt
	
	# ##############################################3
	!ls *.fits > chinjdel.lis
	!sed 's/.fits/.fits[1]/g' chinjdel.lis > chinjdel1.lis
	!rm chinjdel.lis
	lista="chinjdel1.lis"
	!mkdir bad-flash-charge

	while(fscan(lista,s1)!=EOF)
		{
		hselect(s1,"$I,CHINJECT", expr="yes", >> "lista3.txt")
		}
	!rm chinjdel.lis
	# Remuevo todas las lineas que contengan la palabra SUCSESSFUL y ABORTED
	!sed '/NONE/d' lista3.txt > tmd3
	# Remuevo las terminaciones [1] y lo llamo junk, ahora es una lista de imagenes borrbles
	!sed 's/\[.*$//g' tmd3 > junk3
	# Borro todas las imagenes de junk

	!cat junk3 | xargs -I % cp % bad-flash-charge
	imdel('@junk3')
	!rm junk3
	!rm tmd3
	!rm lista3.txt

	
	# ##############################################
	!echo '-------rayocosmico fits'
	!ls *.fits
	!ls *.fits > flashdel.lis
	!sed 's/.fits/.fits[1]/g' flashdel.lis > flashdel1.lis
	!rm -f flashdel.lis
	lista="flashdel1.lis"

	while(fscan(lista,s1)!=EOF)
		{
		hselect(s1,"$I,FLASHCUR", expr="yes", >> "lista2.txt")
		}
	!rm -f flashdel.lis
	# Remuevo todas las lineas que contengan la palabra LOW
	!sed '/LOW/d' lista2.txt > tmd2
	!grep -Fvxf tmd2 lista2.txt > tmd2spt #LosLOW
	# Remuevo las terminaciones [1] y lo llamo junk, ahora es una lista de imagenes borrables
	!sed 's/\t.*$//g' tmd2spt > move2spt #LosLOW_raw.fits[1]
	!sed 's/_raw/_spt/g' move2spt > move2spt2 #LosLOW_spt.fits[1]
	!grep -Fvxf move2spt2 tmd2 > tmd2_

	!sed 's/\[.*$//g' tmd2_ > junk2

	# Borro todas las imagenes de junk
	!cat junk2 | xargs -I % cp % bad-flash-charge
	#!cat move2spt2 | xargs -I % cp % bad-flash-charge

	#imdel('@junk2') #esto borra los archivos que tienen baja resolucion aparentemente
	!rm -f junk2
	!rm -f tmd2
	!rm -f tmd2_
	!rm -f tmd2spt
	!rm -f move2spt
	!rm -f move2spt2
	!rm -f lista2.txt

	# #########################################################
	# Lanzo la comparacion para separar las imagenes huérfanas
	!echo '------lets compare'
	!sh compare.sh
	
	print ("Descargando los keywords en los archivos rawdataC.txt y sptdataC.txt #####")
	!ls *_raw.fits > _rawfits0.lis
	!ls *_spt.fits > _sptfits0.lis
	!sed 's/.fits/.fits[1]/g' _rawfits0.lis > _rawfits.lis
	!sed 's/.fits/.fits[1]/g' _sptfits0.lis > _sptfits.lis

	ldump_raw="_rawfits.lis"
	while(fscan(ldump_raw,s1)!=EOF)
		{
		hselect(s1, fields="$I,$DATE-OBS,$TIME-OBS,$EXPSTART,$EXPEND,$EXPTIME,$SUNANGLE,$MOONANGL,$SUN_ALT,$PA_V3", expr="yes", missing="n/a", >> "rawdataC.txt")
		}

	ldump_spt="_sptfits.lis"
	while(fscan(ldump_spt,s1)!=EOF)
		{
		hselect(s1, fields="$I,DATE,FILENAME,INSTRUME,EQUINOX,ELON_REF,ELAT_REF,GLON_REF,GLAT_REF,UTC0,PSTRTIME,PSTPTIME,RA_V1,DEC_V1,POSTNSTX,POSTNSTY,POSTNSTZ,VELOCSTX,VELOCSTY,VELOCSTZ,RA_SUN,DEC_SUN,RA_MOON,DEC_MOON", expr="yes", missing="n/a", >> "sptdataC.txt")
		}

	!sed -i -e '1iCampos: IMAGE|DATE-OBS|TIME-OBS|EXPSTART|EXPEND|EXPTIME|SUNANGLE|MOONANGL|SUN_ALT|PA_V3' rawdataC.txt
	!sed -i -e '1iCampos: IMAGE|DATE|FILENAME|INSTRUME|EQUINOX|ELON_REF|ELAT_REF|GLON_REF|GLAT_REF|UTC0|PSTRTIME|PSTPTIME|RA_V1|DEC_V1|POSTNSTX|POSTNSTY|POSTNSTZ|VELOCSTX|VELOCSTY|VELOCSTZ|RA_SUN|DEC_SUN|RA_MOON|DEC_MOON' sptdataC.txt
	!mkdir CosmicRaysProc
	!sed 's/.fits/.head/g' _rawfits0.lis > rawimhead.txt
	!sed 's/.fits/.head/g' _sptfits0.lis > sptimhead.txt
	
	!ls *.fits > _fits0.lis
	!sed 's/.fits/.fits[1]/g' _fits0.lis > _fits.lis
	list="_fits.lis"
	
	while(fscan(list,s1)!=EOF)
		{
		n=strlen(s1)
		m=n-3
		s2=substr(s1, 1,m)		
		imhead(s1, l+ , > s2//".head")
		print("Header de imagen: ",s1," ok")
		k=k+1;
		}
	
	k=k-1
	print("Headers salvados: ", k)
	
	# Listamos los datos RAW
	!ls *_raw.fits > data

	# Copio la extensión de imagen de los darks en la lista @darks
	print ("-----Copiando extension/capa de imagen de los darks #########################")
	
	dl1="data"
	while(fscan(dl1,s1)!=EOF)
		{
		imcopy( s1//"[1]" , s1//".conv")
		imagen=s1//".conv"
		chpixtype( imagen, imagen, newpixty = "real" ) 
		}
	
	!ls *.conv.fits > darks
	!sed 's/.conv.fits/.conv.fits.crr/g' darks > cdarks
	!mv darks pdarks

	# Estos son los parametros modificables de la tarea XZAP
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


	
	# Remuevo rayos cosmicos con la tarea XZAP



# OPTION 2 - xzap on a single task on a list of images
flpr 0
	xzap( inlist = "@pdarks", omasks = "u5d2012li_bpx.fit", outlist = "@cdarks", crmasks= ".crm" )
	# Creo lista de los darks sin rayos cosmicos y creo futura lista de imagenes con SOLO rayos cosmicos
	
	
	
	!ls *.crr.fits > crrdarks
	!sed 's/.conv.fits/.conv.fits.cro/g' pdarks > cronly
	# Resto las imagenes para generar las imagenes con SOLO rayos cosmicos
	print ("Generando imagenes con valor de fondo cero, SOLO con rayos cosmicos ####")
	
	dl2="pdarks"
	while(fscan(dl2,s1)!=EOF)
		{
		imarith( s1, '-', s1//".crr", s1//".cro", pixtype="real", calctype="real" )
		}


	!rm *.crr.fits

	!tr '-' ' ' <rawdataC.txt >rawdata.txt
	!tr '-' ' ' <sptdataC.txt >sptdata.txt
	!rm pdarks crrdarks cronly darks data cdarks lista.txt files.lis files1.lis tmd junk
	!cp *.cro.fits CosmicRaysProc
	!cp rawdata.txt CosmicRaysProc
	!cp sptdata.txt CosmicRaysProc
	!rm rawdata.txt sptdata.txt rawdataC.txt sptdataC.txt
	!cp default.sex.0 CosmicRaysProc
	!cp default.param CosmicRaysProc
	!cp sextractor.sh CosmicRaysProc
	!sh CosmicRaysProc/sextractor.sh
	!rm *.cro.fits
	!cp *.cat CosmicRaysProc
	!cp *.head CosmicRaysProc
	!rm *.head
	!rm *.cat default.sex rawimhead.txt sptimhead.txt
	!rm CosmicRaysProc/default.sex.0
	!rm CosmicRaysProc/sextractor.sh
	!rm CosmicRaysProc/default.param
	!rm _*.lis
	!rm *.conv*
	!ls CosmicRaysProc/*.cro.fits > list_images0.txt
	!sed 's/\[.*$//g' list_images0.txt > list_images1.txt
	!cat list_images1.txt | sed 's/\_.*$//g' > list_images2.txt
	!cat list_images2.txt | sed -re 's/^.+\///' > list_images3.txt
	!sed -i -e "1d" list_images3.txt
	!mv list_images3.txt list_images.txt
	!rm list_images0.txt
	!rm list_images1.txt
	!rm list_images2.txt
	!rm list_images3.txt
	!cp list_images.txt CosmicRaysProc
	!rm list_images.txt
	!rm CosmicRaysProc/aaaaDUMMY*
	
	!mv analisis_res.m CosmicRaysProc
	!mv cal_pos.m CosmicRaysProc
	!mv date2jd.m CosmicRaysProc
	!mv deg2rad.m CosmicRaysProc
	!mv ecef2geodetic.m CosmicRaysProc
	!mv hp3d.m CosmicRaysProc
	!mv iden_cr.m CosmicRaysProc
	!mv lst.m CosmicRaysProc
	!mv nutation.m CosmicRaysProc
	!mv obliquity.m CosmicRaysProc
	!mv rad2deg.m CosmicRaysProc

	#!matlab -nojvm < CosmicRaysProc/iden_cr.m
	#!mkdir CosmicRaysProc/matlab-results
	#!mv CosmicRaysProc/*res.txt CosmicRaysProc/matlab-results
	
	!touch fin.lock
end

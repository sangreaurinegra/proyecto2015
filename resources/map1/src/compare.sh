#!/bin/sh
mkdir NoMatches
mkdir Matches

echo '-----fits files'
ls *.fits
ls *.fits > allfiles0.txt

echo '-----raw fits files'
ls *raw.fits
ls *raw.fits > allfilesraw.txt

for i in $(ls *raw.fits); do
	raw=$(echo "$i"|sed 's/\(.*\)......../\1/')
	
    for j in $(ls *spt.fits); do
		spt=$(echo "$j"|sed 's/\(.*\)......../\1/')
		if [ "$spt" = "$raw" ]; then
			echo "---------Los siguientes SI"
			echo "$i" >> rawmatch0.txt
			echo "$raw"
			echo "$spt"
			#echo "$j" >> sptmatch0.txt
			mv "$j" Matches/
			break
		else
			echo "---------Los siguientes NO"
			echo "$raw"
			#echo "$spt"
		fi
	done
done

# Lo que queda en el directorio original se mueven
# NoMatches y listo el pollo

#mv *.fits NoMatches/
sort rawmatch0.txt | uniq -u > rawmatch.txt
#sort sptmatch0.txt | uniq -u > sptmatch.txt

rm rawmatch0.txt
#rm sptmatch0.txt

grep -Fvxf rawmatch.txt allfilesraw.txt > allfilestmp.txt
#grep -Fvxf sptmatch.txt allfilestmp.txt > nomatch.txt


#rm sptmatch.txt
rm allfilesraw.txt
#rm allfilestmp.txt

while read junk; do
  mv "$junk" NoMatches
  #rm "$junk"
done < allfilestmp.txt

while read raws; do
  mv "$raws" Matches
  #rm "$raws"
done < rawmatch.txt

rm rawmatch.txt
mv *spt.fits NoMatches
mv Matches/* .
rm -r Matches
rm allfiles0.txt
rm allfilestmp.txt

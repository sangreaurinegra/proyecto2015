#!/bin/sh

for i in *cro.fits; do
    sed "s/test.cat/"$i".cat/g" default.sex.0 > default.sex
	sextractor -c default.sex "$i"  
  
done

#ls | grep "php" > files.txt
#for file in $(cat files.txt); do
#    sed 's/catch/send/g' $file > TMPfile.php && mv TMPfile.php $file
#done



#!/bin/bash
#scriptParams=("test1" "test2" "test3")  ## Better store it as arrays.
#while read -r line; do
#    for i in in "${!scriptParams[@]}"; do  ## Indices of array scriptParams would be populated to i starting at 0.
#        line=${line/"\$test.cat"/"${catalogo}"}  ## ${var/p/r} replaces patterns (p) with r in the contents of var. Here we also add 1 to the index to fit with the targets.
#    done
#    echo "<br>$line</br>"
#done < template.txt

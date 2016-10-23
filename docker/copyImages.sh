#!/bin/bash


for file in *.fits
do
	for i in {1..2}
	do 
		cp "$file" ./$i"$file"
	done
done


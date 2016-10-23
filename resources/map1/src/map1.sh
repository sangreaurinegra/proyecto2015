#!/bin/bash

if [ -z "$1" ]
then
        echo "No se recibio el nombre del archivo"
elif ! [ -f "$1" ]
then
	echo "No se encontro el archivo"
else
	eval `/home/gabriel/.ureka/ur_setup`

	touch input.txt
	rm input.txt
	echo "$1" > input.txt

	echo ECHO DESDE MAP.SH



	cl < clInput.txt > cl.out 2> cl.err
fi



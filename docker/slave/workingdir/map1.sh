#!/bin/bash

if [ -z "$1" ]
then
        echo "No se recibio el parametro"
else
	eval `/root/.ureka/ur_setup`

	touch input.txt
	rm input.txt
	echo "$1" > input.txt

	echo ECHO DESDE MAP.SH

	cl < clInput.txt > cl.out 2> cl.err
fi






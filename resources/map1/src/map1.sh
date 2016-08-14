#!/bin/bash

ur_setup
touch input.txt
rm input.txt
echo "$1" > input.txt

cl < clInput.txt



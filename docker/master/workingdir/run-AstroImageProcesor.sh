#!/bin/bash

# test the hadoop cluster by running wordcount

# create input files 
mkdir input
echo "Inicio AstroImageProcesor" 
date
dateini=$(date +%s)

# create input directory on HDFS
hadoop fs -mkdir -p input

# put input files to HDFS
hdfs dfs -put ./input/* input

# run wordcount 
hadoop jar /root/workingdir/AstroImageProcesor.jar edu.proyecto.Main input/list_imagesHadoop.txt output

# print the input files
#echo -e "\ninput file1.txt:"
#hdfs dfs -cat input/file1.txt

#echo -e "\ninput file2.txt:"
#hdfs dfs -cat input/file2.txt

#echo -e "\nHadoop Test output:"
# print the output of wordcount
#hdfs dfs -cat output/part-r-00000


echo "Fin AstroImageProcesor"
date
datefin=$(date +%s)
diferencia=datefin-dateini
echo "Demoro $diferencia segundos"

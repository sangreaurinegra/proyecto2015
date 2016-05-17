#!/bin/bash

image=$1
tag="0.0.1"

if [ $# = 0 ]
then
	echo "Falta imagen como argumento!"
	exit 1
fi


# founction for delete images
function docker_rmi()
{
	echo -e "\n\nsudo docker rmi $1:$tag"
	sudo docker rmi $1:$tag
}


# founction for build images
function docker_build()
{
	cd $1
	echo -e "\n\nsudo docker build -t $1:$tag ."
	/usr/bin/time -f "real  %e" sudo docker build -t $1:$tag .
	cd ..
}


echo -e "\n Remuevo TODOS los contenedores \ndocker rm -f $(docker ps -a -q)"

sudo docker rm -f $(sudo docker ps -a -q)

sudo docker images >images.txt

if [ $image == "master" ]
then
	docker_rmi master
	docker_build master
elif [ $image == "slave" ]
then
	docker_rmi slave
	docker_build slave
else
	echo "El nombre de la imagen esta mal"
fi

echo -e "\nImagenes antes del build."
cat images.txt
rm images.txt

echo -e "\nImagenes luego del build."
sudo docker images

# Docker file Master que extiene el master prepara copias locales y ajustes para la ejecucion en cluster
FROM sangreaurinegra/master:0.1.1

MAINTAINER SangreAurinegra <sangreaurinegra@gmail.com>

#RUN rm -r /resources

RUN rm /root/run-wordcount.sh

ADD resources/ /root/resources/
ADD input/ /root/input/
ADD output/ /root/output/
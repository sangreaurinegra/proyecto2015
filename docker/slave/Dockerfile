# Docker file Master que extiene el master prepara copias locales y ajustes para la ejecucion en cluster
FROM sangreaurinegra/slave:0.1.1

MAINTAINER SangreAurinegra <sangreaurinegra@gmail.com>

#RUN rm -r /resources

ADD workingdir/ /root/workingdir/

ENV WORKING_DIR /root/workingdir/

WORKDIR /root/workingdir/

RUN eval `/root/.ureka/ur_setup`

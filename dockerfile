FROM alpine:3.9.2
LABEL maintainer="jep0042@posteo.de"
EXPOSE 8270
RUN apk update && \
    apk upgrade && \
    apk add openjdk8 && \
    # nss needs to be installed manually due to https://github.com/docker-library/openjdk/issues/289
    apk add nss && \
    apk add maven && \
    apk add git
RUN cd ~ && \
    git clone https://github.com/JeP42/robotframework-easycsvmap.git && \
    cd ~/robotframework-easycsvmap && \
    mvn install
ENTRYPOINT java -jar ~/robotframework-easycsvmap/robotremoteserver/target/robotremoteserver-easycsvmap-jar-with-dependencies.jar --port 8270
CMD /bin/sh     

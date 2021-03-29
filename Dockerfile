FROM gradle:6.8.3-jdk11

WORKDIR /tmp
ADD . /tmp

RUN gradle build

CMD ["gradle", "clean", "bootRun"]
EXPOSE 3000
EXPOSE 8080
EXPOSE 80

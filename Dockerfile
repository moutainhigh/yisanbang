FROM java:8
VOLUME /tmp
EXPOSE 8089
ADD /target/yisanbang-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
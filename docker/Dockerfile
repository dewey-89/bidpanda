# jdk17 image start
FROM openjdk:17-jdk
# 인자 정리 -Jar
ARG JAR_FILE=*.jar
# jar file copy
COPY ${JAR_FILE} app.jar
# docker Timezone 설정
ENV TZ=Asia/Seoul
#
ENTRYPOINT ["java", "-jar", "/app.jar"]
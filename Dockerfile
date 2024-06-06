# Run the web service on container startup.
#CMD ["java", "-jar", "/*.jar"]


#
# Build stage
#
FROM eclipse-temurin:17-jdk-jammy AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
# Download dependencies and build a release artifact.
# copy just the pom.xml for cache efficiency
COPY ./.mvn $HOME/.mvn
COPY ./mvnw $HOME    
COPY ./mvnw.cmd $HOME    
COPY ./pom.xml $HOME
# go-offline using the pom.xml
#RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package
#The go-offline goal downloads all dependencies and plugins
RUN ./mvnw -B dependency:go-offline
# Copy local code to the container image.
ADD . $HOME
# At this point, all dependencies are available in the local Maven repository.
RUN ./mvnw -f $HOME/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:17-jre-jammy 
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/runner.jar
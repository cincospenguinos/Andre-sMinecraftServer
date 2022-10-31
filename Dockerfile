FROM eclipse-temurin:17

RUN apt update && apt install -y git

# This is where we're putting everything
RUN mkdir /minecraft-server

# Get Maven
WORKDIR /opt
RUN wget -O maven.tar.gz https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
RUN tar -xzvf maven.tar.gz
ENV PATH="/opt/apache-maven-3.8.6/bin:$PATH"

# Build spigot plugin
RUN mkdir /tmp/spigot_plugin
WORKDIR /tmp/spigot_plugin
COPY . .
RUN mvn package
RUN mv target/AndresServerPlugin-jar-with-dependencies.jar /tmp/spigot_plugin/AndreServerPlugin.jar

# Build spigot
RUN mkdir /tmp/build_spigot
WORKDIR /tmp/build_spigot
RUN wget -O BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
RUN java -jar BuildTools.jar
RUN mv spigot**.jar minecraft-server.jar
RUN mv minecraft-server.jar /minecraft-server/minecraft-server.jar
RUN mkdir /minecraft-server/plugins
RUN cp /tmp/spigot_plugin/AndreServerPlugin.jar /minecraft-server/plugins/
RUN echo "eula=true" > /minecraft-server/eula.txt

WORKDIR /minecraft-server
VOLUME /minecraft-server

EXPOSE 25565
CMD ["java", "-jar", "/minecraft-server/minecraft-server.jar", "nogui"]

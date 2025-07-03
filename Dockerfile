FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1

RUN apt-get update && \ 
    apt-get install -y libxrender1 libxtst6 libxi6

WORKDIR /se_dominion

COPY . /se_dominion

RUN sbt update && sbt compile

CMD ["sbt", "run"]^

# Run XServer, then run this command to start the GUI
# docker run -e DISPLAY=host.docker.internal:0.0 -ti dominion:v1
FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1

WORKDIR /app

COPY . .

RUN sbt update

RUN sbt compile

CMD ["sbt", "run"]
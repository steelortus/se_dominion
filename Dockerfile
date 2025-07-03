FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1

WORKDIR /se_dominion

# Copy only necessary files to reduce build context size
COPY . /se_dominion

# Update and compile the project
RUN sbt update && sbt compile

# Run the application
CMD ["sbt", "run"]
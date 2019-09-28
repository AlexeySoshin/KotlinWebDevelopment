FROM openjdk:11

COPY . .

RUN ./gradlew build -x test

CMD ["./gradlew", "run"]
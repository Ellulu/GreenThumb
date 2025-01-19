# Étape 1 : Build
FROM gradle:8.2-jdk17 AS build

WORKDIR /app

# Copier les fichiers nécessaires
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# Donner les permissions d'exécution à gradlew
RUN chmod +x gradlew

# Télécharger les dépendances
RUN ./gradlew dependencies --no-daemon

# Copier le code source
COPY src ./src

# Construire l'application
RUN ./gradlew bootJar --no-daemon && echo "BootJar completed successfully!"

# Débogage : vérifier si le JAR est généré
RUN ls /app/build/libs

# Étape 2 : Exécution
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copier le fichier JAR généré
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

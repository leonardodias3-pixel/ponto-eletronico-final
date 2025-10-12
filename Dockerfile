# Passo 1: Use uma imagem base oficial que já tem o Java 17 instalado.
FROM eclipse-temurin:17-jdk-jammy

# Passo 2: Crie uma pasta de trabalho dentro da nossa "caixa".
WORKDIR /app

# Passo 3: Copie os arquivos de build primeiro para aproveitar o cache do Docker.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Passo 4: Baixe todas as dependências do projeto.
RUN ./mvnw dependency:go-offline

# Passo 5: Copie o resto do código-fonte da nossa aplicação.
COPY src ./src

# Passo 6: Execute o comando de build para compilar tudo e gerar o arquivo .jar.
RUN ./mvnw clean install -DskipTests

# Passo 7: Diga ao Docker que nossa aplicação vai usar a porta 8080.
EXPOSE 8080

# Passo 8: O comando final para rodar a aplicação quando a "caixa" for ligada.
CMD ["java", "-jar", "target/ponto-eletronico-0.0.1-SNAPSHOT.jar"]
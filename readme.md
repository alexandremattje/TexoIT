# TexoIT

## Ferramentas e Versões
#### Java: 20
#### Springboot: 3.1.3
#### Gradle
#### Swagger

## Comandos
* ```./gradlew build``` Executa os testes e constroi a aplicação
* ```./gradlew test``` Executa apenas os tests
* ```./gradlew bootRun --args="--importFile=movielist.csv"``` Executa a aplicação importando os dados do arquivo [movielist.csv](movielist.csv) que está na pasta raiz do projeto 

Caso queira importar os dados de algum arquivo fora da pasta do projeto deve se usar o caminho completo desde o root do sistema

Ex.: ```./gradlew bootRun --args="--importFile=/Users/alexandremattje/IdeaProjects/TexoIT/movielist.csv"```

### Porta 8080
A aplicação está configurada para executar na porta 8080 (padrão do Springboot), para executar em outra porta tem que incluir um parâmetro no arquivo [application.properties](/src/main/resources/application.properties)
```server.port:8888``` onde 8888 é o número da porta que deseja executar

## Swagger
Caso deseje verificar a API da aplicação pode abrir o Swagger no link [Swagger localhost](http://localhost:8080/swagger-ui/index.html)
Por padrão o link está usando a porta 8080, esta porta deve ser alterada no link caso a porta de execução da aplicação tenha sido alterada
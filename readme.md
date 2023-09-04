# TexoIT

* Os testes referente a importação do arquivo está na classe ```StartupDataTest```

## Ferramentas e Versões
#### Java: 20
#### Springboot: 3.1.3
#### Gradle
#### Swagger

## Comandos
* ```./gradlew build``` Executa os testes e constroi a aplicação
* ```./gradlew test``` Executa apenas os tests
* ```./gradlew bootRun ``` Executa o projeto importando o arquivo ```movielist.csv``` que está na pasta raiz o projeto
* ```./gradlew bootRun --args="--importFile=movielist.csv"``` Executa a aplicação importando os dados do arquivo [movielist.csv](movielist.csv) que está na pasta raiz do projeto 

Caso queira importar os dados de algum arquivo fora da pasta do projeto deve se usar o caminho completo desde o root do sistema

Ex.: ```./gradlew bootRun --args="--importFile=/Users/alexandremattje/IdeaProjects/TexoIT/movielist.csv"```

### Porta 8080
A aplicação está configurada para executar na porta 8080 (padrão do Springboot), para executar em outra porta tem que incluir um parâmetro no arquivo [application.properties](/src/main/resources/application.properties)
```server.port:8888``` onde 8888 é o número da porta que deseja executar

## Swagger
Caso deseje verificar a API da aplicação pode abrir o Swagger no link [Swagger localhost](http://localhost:8080/swagger-ui/index.html)
Por padrão o link está usando a porta 8080, esta porta deve ser alterada no link caso a porta de execução da aplicação tenha sido alterada

Este ficou o resultado da consulta com o arquivo original
```
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

Este ficou o resultado após adicionar as três novas linhas.
O valor máximo não é 13 como está na mensagem do WhattsUp e sim 22.
2002 - 1980 = 22
2037 - 2015 = 22
```
1980;Test 1;Test 1;Matthew Vaughn;yes
2003;Test 2;Test 2;Matthew Vaughn;yes
2037;Test 3;Test 3;Matthew Vaughn;yes
```
```
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    },
    {
      "producer": "Matthew Vaughn",
      "interval": 1,
      "previousWin": 2002,
      "followingWin": 2003
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 22,
      "previousWin": 1980,
      "followingWin": 2002
    },
    {
      "producer": "Matthew Vaughn",
      "interval": 22,
      "previousWin": 2015,
      "followingWin": 2037
    }
  ]
}
```
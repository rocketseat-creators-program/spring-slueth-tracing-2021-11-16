<img src="https://storage.googleapis.com/golden-wind/experts-club/capa-github.svg" />

## Monitorando sua aplicação com Spring Slueth e Zipkin
Este projeto visa exemplificar a utilização do Spring Slueth juntamente com o Zipkin, o que viabiliza o acompanhamento das requisições realizadas a aplicação, permitindo verificar o tempo de cada requisição, mesmo que um sistema chame o outro serviço, sendo assim, o mesmo é muito utilizado em ambiente distribuído.

### Ambiente Utilizado
* Java JDK 16
* Spring 2.5.6
* Docker
* Eclipse
* Ubuntu 20.04

### Configuração do Zipkin
Para subir o zipkin  na porta `9411` utilizando o docker use o seguinte comando:

```shell
docker run -d -p 9411:9411 openzipkin/zipkin
```

### Script para rodar todos 
Considerando que tive de criar 4 aplicações utilizei o seguinte script para conseguir subir todas as aplicações utilizando o terminal, não necessitando ter todos na sua IDE. Para rodar basta passar o parâmetro `-s` indicando o número do serviço que deseja subir.

```shell
while getopts s: flag
do
    case "${flag}" in
        s) service=${OPTARG};;
    esac
done

echo "..:: Starting Service $service ::.."

if [ $service -eq 1 ]
then
  cd ./tracing-1
fi

if [ $service -eq 2 ]
then
  cd ./tracing-2
fi

if [ $service -eq 3 ]
then
  cd ./tracing-3
fi

if [ $service -eq 4 ]
then
  cd ./tracing-4
fi

if [ $service -gt 5 ]
then
  echo "opção inválida!";
else
  mvn clean install
  java -jar ./target/slueth-test-0.0.1-SNAPSHOT.jar
fi
```

### Diretório do Projeto

```sh
.
├── tracing-1/    # aplicação base utilizada para chamar o tracing2.
├── tracing-2/    # o tracing2 chama o tracing 3 e 4.
├── tracing-3/    # esse basicamente possui um controller e retorna uma string.
├── tracing-4/    # esse basicamente possui um controller e retorna uma string.
├── .gitignore    # arquivos que são desconsiderados pelo git
├── pom.xml       # dependências do projeto
└── README.md
```


## Expert

| [<img src="https://avatars.githubusercontent.com/u/1785791?s=400&u=cf86c9ae2216765f948ca2136eda7e632e0cd922&v=4" width="75px;"/>](https://github.com/gustavodsf) |
| :-: |
|[Gustavo Figueiredo](https://github.com/gustavodsf)|

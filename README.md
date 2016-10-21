Projeto utilizando o Vertx Web com Validaçao isomorfica
=======================================================


Desenvolvemos este projeto como uma prova de conceito durante o planejamento da
construçao da soluçao do IRPF MOBILE WEB.



## Tecnologias utilizadas:

### Backend

* Vertx Web

* Rest Assured para testes de integraçao

* AssertJ ou 

* Liquibase para versionamento do banco de dados

* Postgres para persistencia dos dados



### FRONTEND

* Angular 2 + Ionic

* Gauge e WebdriverIO para testes de especificaçao / Navegaçao

* FakeJS para geraçao de dados para testes
 
* DexieJS ou PouchDB para dados offline



### Validaçao Isomorfica  FRONTEND + BACKEND 

* AVJS (JSON Schema Validator)

* FETCH API
   - frontend -> fetch polyfill
   - backend  -> fetch alike api usando vertx-client

* Promise
   - frontend: es6
   - backend: http://jdeferred.org/

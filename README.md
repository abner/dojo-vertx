

# Executar em modo de desenvolvimento

```bash
mvn exec:exec@dev
```

> O vertx inicia em modo de desenvolvimento e recompila e faz redeploy em caso de mudanças no código fonte.


# Inicializar App vertx a partir do Jar em Background

Para inicializar:
---------------

```bash
java -jar target/vertxapp-0.0.1-SNAPSHOT-fat.jar start vertxapp
``` 

Para parar:
---------------

```bash
java -jar target/vertxapp-0.0.1-SNAPSHOT-fat.jar stop vertxapp
```




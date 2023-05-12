package mflix.lessons;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @see com.mongodb.client.MongoClients
 * @see com.mongodb.client.MongoIterable
 * @see com.mongodb.ReadPreference
 * @see org.bson.codecs.Codec
 */
@SpringBootTest
public class MongoClientLesson extends AbstractLesson {

  private MongoClient mongoClient;

  private MongoDatabase database;

  private MongoCollection<Document> collection;

  private String uri = "mongodb+srv://m220student:m220password@cluster0.zsej6.mongodb.net";

  private Document document;

  private Bson bson;

  @Test
  public void MongoClientInstance() {

    /*
    Let's start by instantiating a MongoClient object, since this is the
    pillar of all client (our code) and server (atlas cluster)
    communication.

    To do this we will be using the com.mongodb.client.MongoClients
    builder.

    For this example I'll be using a MongoDB uri that defines to which
    cluster and how we should connect.

    Once I have my connection string, I can go ahead and call
    MongoClients and create() a MongoClient instance, by providing a
    mongodb uri.
--------------------
    Vamos começar instanciando um objeto MongoClient, já que este é o
    pilar de todos os clientes (nosso código) e servidor (cluster atlas)
    comunicação.

    Para fazer isso, usaremos com.mongodb.client.MongoClients
    construtor.

    Para este exemplo, estarei usando um uri MongoDB que define para qual
    cluster e como devemos nos conectar.

    Assim que tiver minha string de conexão, posso prosseguir e ligar
    MongoClients e crie () uma instância MongoClient, fornecendo um
    mongodb uri.
     */

    mongoClient = MongoClients.create(uri);

    Assert.assertNotNull(mongoClient);

    MongoClientSettings settings;

    /*
    The MongoClients object will create a MongoClient instance by
    extracting the client connection settings from the connection string.
    However, can also do extended configuration, by setting
    configuration options, that may not be present in the connection
    string uri by setting a MongoClientSettings object.

    This class contains a builder method, a static class method, that
    allows you to compose the different types of client settings upon
    each other.
--------------------
    O objeto MongoClients criará uma instância MongoClient por
    extrair as configurações de conexão do cliente da string de conexão.
    No entanto, também pode fazer configuração estendida, definindo
    opções de configuração, que podem não estar presentes na conexão
    string uri definindo um objeto MongoClientSettings.

    Esta classe contém um método construtor, um método de classe estática, que
    permite que você componha os diferentes tipos de configurações do cliente em
    uns aos outros.

     */

    ConnectionString connectionString = new ConnectionString(uri);
    MongoClientSettings clientSettings =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applicationName("sample_mflix")
            .applyToConnectionPoolSettings(
                builder -> builder.maxWaitTime(1000, TimeUnit.MILLISECONDS))
            .build();

    mongoClient = MongoClients.create(clientSettings);

    Assert.assertNotNull(mongoClient);
  }

  @Test
  public void MongoDatabaseInstance() {

    mongoClient = MongoClients.create(uri);

    /*
    Now that we have a MongoClient instance, we can go ahead and connect
    to our cluster and list all available databases in our cluster by
    running the listDatabases command, similar to this mongo shell command:

        db.adminCommand({listDatabases: 1})
--------------------
    Agora que temos uma instância do MongoClient, podemos prosseguir e conectar
    ao nosso cluster e liste todos os bancos de dados disponíveis em nosso cluster por
    executando o comando listDatabases, semelhante a este comando shell mongo:

        db.adminCommand ({listDatabases: 1})
     */

    MongoIterable<String> databaseIterable = mongoClient.listDatabaseNames();

    /*
    This command returns a MongoIterable instance, an iterable object
    that we can use to iterate over the results of the a given command.
    We will be using MongoIterable instances quite often.
--------------------
    Este comando retorna uma instância MongoIterable, um objeto iterável
    que podemos usar para iterar os resultados de um determinado comando.
    Estaremos usando instâncias MongoIterable com bastante frequência.
     */

    List<String> dbnames = new ArrayList<>();
    for (String name : databaseIterable) {
      System.out.println(name);
      dbnames.add(name);
    }

    /*
    Important to note that Iterable instance get exhausted, like a cursor,
    so consider using the iterable instance to fill arrays and lists if
    you need to go over the contents more than once.
--------------------
    Importante observar que a instância Iterable se esgota, como um cursor,
    então considere usar a instância iterável para preencher matrizes e listas se
    você precisa revisar o conteúdo mais de uma vez.
     */

    Assert.assertTrue(dbnames.contains("sample_mflix"));

    /*
    Then we have our MongoDatabase object. We will use this object to
    access and create/drop our collections, run commands and define
    database level read preferences, read concerns and write concerns.
--------------------
    Então temos nosso objeto MongoDatabase. Usaremos este objeto para
    acessar e criar / descartar nossas coleções, executar comandos e definir
    preferências de leitura de nível de banco de dados, preocupações de leitura e preocupações de gravação.
     */

    database = mongoClient.getDatabase("sample_mflix");

    ReadPreference readPreference = database.getReadPreference();

    /*
    Here, because we did not specify a Read Preference, we driver will
    use the default configuration which is primary.
--------------------
    Aqui, como não especificamos uma preferência de leitura, o motorista irá
    use a configuração padrão que é primária
     */

    Assert.assertEquals("primary", readPreference.getName());
  }

  @Test
  public void MongoCollectionInstance() {

    /*
    A MongoCollection instance is what is used to read and write to the
    documents, which is usually the entity the application processes to
    manipulate the data it needs.

    To instantiate a collection, we need to provide the
    collection name from a MongoDatabase instance.
--------------------
    Uma instância MongoCollection é o que é usado para ler e escrever no
    documentos, que geralmente é a entidade para a qual o aplicativo processa
    manipular os dados de que necessita.

    Para instanciar uma coleção, precisamos fornecer o
    nome da coleção de uma instância MongoDatabase.
     */

    mongoClient = MongoClients.create(uri);
    database = mongoClient.getDatabase("sample_mflix");
    collection = database.getCollection("movies");

    /*
    In this example we are using the basic form of interacting with
    data defined in a MongoCollection, where we return Document instances
    from any given query.
    However, the MongoCollection allows us to specify Codec so
    that we can return business objects, application defined classes,
    as return of queries. More about this on the POJO support lessons.
--------------------
    Neste exemplo, estamos usando a forma básica de interação com
    dados definidos em um MongoCollection, onde retornamos instâncias de Document
    de qualquer consulta.
    No entanto, o MongoCollection nos permite especificar o Codec para
    que podemos retornar objetos de negócios, classes definidas pelo aplicativo,
    como retorno de consultas. Mais sobre isso nas lições de suporte do POJO.

     */

    MongoIterable<Document> cursor = collection.find().skip(10).limit(20);

    /*
    All of our Data Manipulation Language (DML) will be expressed via a
    MongoCollection instance;
--------------------
    Toda a nossa linguagem de manipulação de dados (DML) será expressa por meio de um
    Instância MongoCollection;
     */
    List<Document> documents = new ArrayList<>();
    Assert.assertEquals(20, cursor.into(documents).size());
  }

  @Test
  public void DocumentInstance() {
    mongoClient = MongoClients.create(uri);
    database = mongoClient.getDatabase("test");
    collection = database.getCollection("users");

    /*
    The basic data structures in MongoDB are documents. The document
    model is what we consider to be the best way to represent data.
    Using documents, makes your data definition as close as possible to
    your OOP object models.

    Since we are dealing with an Object-Oriented Programing language (OOP)
    like Java, having a class that expresses the documents structure,
    becomes imperative.
--------------------
    As estruturas de dados básicas no MongoDB são documentos. O documento
    modelo é o que consideramos ser a melhor maneira de representar os dados.
    O uso de documentos torna a definição de seus dados o mais próximo possível de
    seus modelos de objetos OOP.

    Já que estamos lidando com uma linguagem de programação orientada a objetos (OOP)
    como Java, tendo uma classe que expressa a estrutura dos documentos,
    torna-se imperativo.
    */

    document = new Document("name", new Document("first", "Norberto").append("last", "Leite"));

    /*
    This document defines a MongoDB document that looks like this in its
    json format:

     {
        "name": {
                "first": "Norberto",
                "last": "Leite"
        }
     }

    */

    collection.insertOne(document);

    /*
    We use documents for everything in MongoDB.
    - define data objects
    - define queries
    - define update operations
    - define configuration settings
    ...

    At the Java layer we have the Document class but also the Bson class.
    The Document class implements the Bson interface, because Documents
    are BSON data structures.
--------------------
    Usamos documentos para tudo no MongoDB.
    - definir objetos de dados
    - definir consultas
    - definir operações de atualização
    - definir as configurações
    ...

    Na camada Java, temos a classe Document, mas também a classe Bson.
    A classe Document implementa a interface Bson, porque Documents
    são estruturas de dados BSON.
    */

    Assert.assertTrue(document instanceof Bson);

    /*
    We will also use instances of Bson, throughout the course, to define
    fine tune aspects of our queries like query operators and aggregation
    stages. More on that in the next lectures.
--------------------
    Também usaremos instâncias de Bson, ao longo do curso, para definir
    aperfeiçoar aspectos de nossas consultas, como operadores de consulta e agregação
    estágios. Mais sobre isso nas próximas palestras.
    */

  }
}

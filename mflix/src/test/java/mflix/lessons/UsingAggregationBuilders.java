package mflix.lessons;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @see com.mongodb.client.model.Facet
 * @see com.mongodb.client.model.Accumulators
 * @see com.mongodb.client.model.Aggregates
 */
@SpringBootTest
public class UsingAggregationBuilders extends AbstractLesson {

  /*
  In this lesson we are going to walk you through how to build complex
  aggregation framework pipelines using the Java Driver aggregation builders
   */

  @Test
  public void singleStageAggregation() {
    /*
    First we are going to see the how to create a single stage aggregation.
    In this case, I would like to know how many movies have been
    produced/shot in Portugal.

    Primeiro, veremos como criar uma agregação de estágio único.
    Neste caso, gostaria de saber quantos filmes foram
    produzido / filmado em Portugal.
     */
    String country = "Portugal";

    /*
    And I'm going to use the MongoDB aggregation framework.
    For that, I'll need to have a pipeline defined that will hold all
    of our aggregation stages. In this case, there will be
    just one stage in our pipeline, the $match stage.
    db.movies.aggregate([{$match: {countries: "Portugal"}}])

        E vou usar a estrutura de agregação MongoDB.
    Para isso, vou precisar ter um pipeline definido que abrigará todos
    de nossos estágios de agregação. Neste caso, haverá
    apenas um estágio em nosso pipeline, o estágio $ match.
    db.movies.aggregate ([{$ match: {countries: "Portugal"}}])
     */

    // express the match criteria expressa os critérios de correspondência
    Bson countryPT = Filters.eq("countries", country);

    /*
    The aggregation() collection method takes a list of Bson objects that
    define the different stages of our pipeline, that we are going to
    set using an ArrayList.

    O método de coleção aggregation () leva uma lista de objetos Bson que
    definir as diferentes etapas do nosso pipeline, que iremos
    definido usando um ArrayList.
     */

    List<Bson> pipeline = new ArrayList<>();

    /*
    Instead of manually constructing the Bson document
    that expresses the aggregation stage, you should use Aggregates builder
    class.

    com.mongodb.client.model.Aggregates provides a set of syntactic sugar
    class builders and methods for each of the support aggregation stages.
    Although we can build any aggregation stage by appending Document or
    Bson objects with the respective expressions, Aggregates allows for a
     more concise stage build up, with less typing.

    The match() method takes as argument a filter expression, similar to
    the ones we would use in the case of find() command.

    Em vez de construir manualmente o documento Bson
    que expressa o estágio de agregação, você deve usar o construtor Aggregates
    classe.

    com.mongodb.client.model.Aggregates fornece um conjunto de açúcares sintáticos
    construtores de classes e métodos para cada um dos estágios de agregação de suporte.
    Embora possamos construir qualquer estágio de agregação, anexando Documento ou
    Objetos Bson com as respectivas expressões, Aggregates permite um
     construção de estágio mais concisa, com menos digitação.

    O método match () leva como argumento uma expressão de filtro, semelhante a
    aqueles que usaríamos no caso do comando find ().
    */

    Bson matchStage = Aggregates.match(countryPT);

    // add the matchStage to the pipeline adicione o matchStage ao pipeline
    pipeline.add(matchStage);

    /*
    Once we've appended the match stage to the pipeline array, we can
    then execute the aggregate() collection command.
    Depois de anexar o estágio de correspondência à matriz do pipeline, podemos
    em seguida, execute o comando de coleção aggregate ().
    */

    AggregateIterable<Document> iterable = moviesCollection.aggregate(pipeline);

    /*
    As a result of the aggregate() method, we get back an
    AggregateIterable. Similar to other iterables, this object allows us
     to iterate over the result set.

         Como resultado do método aggregate (), recebemos um
    AggregateIterable. Semelhante a outros iteráveis, este objeto nos permite
     para iterar no conjunto de resultados.
    */

    // collect all movies into an array list coletar todos os filmes em uma lista de array
    List<Document> builderMatchStageResults = new ArrayList<>();
    iterable.into(builderMatchStageResults);

    /*
    Which should produce a list of 115 movies produced in Portugal.
    Que deverá produzir uma lista de 115 filmes produzidos em Portugal
     */
    Assert.assertEquals(115, builderMatchStageResults.size());
  }

  @Test
  public void aggregateSeveralStages() {
    /*
    A single aggregation pipeline, and in particular a $match stage,
    could be achieve by using the find() command. So let's use something
    a bit more interesting, which is exactly what we should be using the
    aggregation framework for.
     */

    List<Bson> pipeline = new ArrayList<>();

    /*
    For all movies produced in Portugal, sum the number of times
    a particular cast member gets to visit such a lovely place. How
    many times has an individual cast member, participated in a movie
    produced in this country. And ofcourse, let's not forget to return the
    results sorted Ascending regarding the number of gigs.
    In the mongo shell this question would be answered by the following
    aggregation:
    db.movies.aggregate([
        {$match: {countries: "Portugal"}},
        {$unwind: "$cast"},
        {$group: {_id: "$cast", gigs: {$sum: 1}}}
    ])
    For that we are going to need:
    */

    /*
    - $match to find all movies produced in portugal
     */
    String country = "Portugal";
    Bson countryPT = Filters.eq("countries", country);
    Bson matchStage = Aggregates.match(countryPT);

    /*
    - $unwind the elements of the $cast array
     */
    Bson unwindCastStage = Aggregates.unwind("$cast");

    /*
    - $group based on cast name and count the number of times a cast
    member appears in the result set
     */
    // group by cast members
    String groupIdCast = "$cast";

    /*
    Group operations are in place to do some sort of accumulation
    operation.
    Operations like $sum, $avg, $min, $max ... are good candidates to be
    used along side group operations, and there is a java builder for that.
    @see com.mongodb.client.model.Accumulators handles all accumulation
    operations.
     */

    // use $sum accumulator to sum 1 for each cast member appearance.
    BsonField sum1 = Accumulators.sum("count", 1);

    // adding both group _id and accumulators
    Bson groupStage = Aggregates.group(groupIdCast, sum1);

    /*
    - $sort based on the new computed field `gigs`
     */

    // create the sort order using Sorts builder
    Bson sortOrder = Sorts.descending("count");
    // pass the sort order to the sort stage builder
    Bson sortStage = Aggregates.sort(sortOrder);

    /*
    With all these stages, we are now ready to call our aggregate method
    with a bit more complex of a pipeline than a single $match stage.
     */

    pipeline.add(matchStage);
    pipeline.add(unwindCastStage);
    pipeline.add(groupStage);
    pipeline.add(sortStage);

    AggregateIterable<Document> iterable = moviesCollection.aggregate(pipeline);

    List<Document> groupByResults = new ArrayList<>();
    for (Document doc : iterable) {
      System.out.println(doc);
      groupByResults.add(doc);
    }

    /*
    The aggregation framework also provides stages that combine
    operations that are typically expressed by several stages.
    For example, $sortByCount, combines both the $group with a $sum
    accumulator with a $sort stage.
    Don't believe me? Well, let's check it out!
     */

    List<Bson> shorterPipeline = new ArrayList<>();

    // we already have built booth $match and $unwind stages
    shorterPipeline.add(matchStage);
    shorterPipeline.add(unwindCastStage);

    // create the $sortByCountStage
    Bson sortByCount = Aggregates.sortByCount("$cast");

    // append $sortByCount stage to shortPipeline
    shorterPipeline.add(sortByCount);

    // list to collect shorterPipeline results
    List<Document> sortByCountResults = new ArrayList<>();

    for (Document doc : moviesCollection.aggregate(shorterPipeline)) {
      System.out.println(doc);
      sortByCountResults.add(doc);
    }

    /*
    Running both pipelines, the same set of results.
     */

    Assert.assertEquals(groupByResults, sortByCountResults);
  }

  @Test
  public void complexStages() {

    /*
    Not all aggregation stages are made equal. There are ones that are
    more complex than others, in terms of type of operation and
    parameters they may take to operate.
    Ex: a $lookup stage is takes a fair more amount of parameters/options
     to execute than a $addFields stage
     {
        $lookup: {
            from: "collection_name",
            pipeline: [{}] - sub-pipeline
            let: {...} - expression
            as: "field_name" - output array field name
        }
     }

     vs

     {
        $addFields: {
            "new_field": {expression} - expression that computes field value
           }
     }

     */

    List<Bson> pipeline = new ArrayList<>();

    /*
    To exemplify this scenario, let's go ahead and do the following:
    - create facets of the movie documents that where produced in Portugal
    - facet on cast_members: list of cast members that are found in the
    movies produced in Portugal
    - facet on genres_count: list of genres and it's count
    - facet on year_bucket: matching movies year bucket

    For each facet we are going to create a com.mongodb.client.Facet object.
     */

    // $unwind the cast array
    Bson unwindCast = Aggregates.unwind("$cast");

    // create a set of cast members with $group
    Bson groupCastSet = Aggregates.group("", Accumulators.addToSet("cast_list", "$cast"));

    /*
    Facet constructor takes a facet name and variable arguments,
    variable-length argument, of sub-pipeline stages that build up the
    expected facet values.
    For the cast_filter we need to unwind the cast arrays and use group
    to create a set of cast members.
     */

    Facet castMembersFacet = new Facet("cast_members", unwindCast, groupCastSet);

    // unwind genres
    Bson unwindGenres = Aggregates.unwind("$genres");

    // genres facet bucket
    Bson genresSortByCount = Aggregates.sortByCount("$genres");

    // create a genres count facet
    Facet genresCountFacet = new Facet("genres_count", unwindGenres, genresSortByCount);

    // year bucketAuto
    Bson yearBucketStage = Aggregates.bucketAuto("$year", 10);

    // year bucket facet
    Facet yearBucketFacet = new Facet("year_bucket", yearBucketStage);

    /*
    The Aggregates.facet() method also takes variable set of Facet
    objects that composes the sub-pipelines of each facet element.

    db.movies.aggregate([
        { "$match" : { "countries" : "Portugal" } },
        { "$facet" : {
            "cast_members" : [{ "$unwind" : "$cast" }, { "$group" : { "_id" : "", "cast_list" : { "$addToSet" : "$cast" } } }],
            "genres_count" : [{ "$unwind" : "$genres" }, { "$sortByCount" : "$genres" }],
            "year_bucket" : [{ "$bucketAuto" : { "groupBy" : "$year", "buckets" : 10 } }]
            }
        }
      ])
     */

    // $facets stage
    Bson facetsStage = Aggregates.facet(castMembersFacet, genresCountFacet, yearBucketFacet);

    // match stage
    Bson matchStage = Aggregates.match(Filters.eq("countries", "Portugal"));

    // putting it all together
    pipeline.add(matchStage);
    pipeline.add(facetsStage);

    int countDocs = 0;
    for (Document doc : moviesCollection.aggregate(pipeline)) {
      System.out.println(doc);
      countDocs++;
    }

    Assert.assertEquals(1, countDocs);
  }

  /*
  Let's recap:
  - Aggregation framework pipelines are composed of lists of Bson stage
  document objects
  - Use the driver Aggregates builder class to compose the different stages
  - Use Accumulators, Sorts and Filters builders to compose the different
  stages expressions
  - Complex aggregation stages can imply several different sub-pipelines
  and stage arguments.
   */

}

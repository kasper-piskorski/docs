package ai.grakn;

import ai.grakn.concept.ResourceType;
import ai.grakn.example.MovieGraphFactory;
import ai.grakn.example.PokemonGraphFactory;
import ai.grakn.graql.QueryBuilder;

import java.util.UUID;

import static ai.grakn.graql.Graql.name;
import static ai.grakn.graql.Graql.var;

public class DocTestUtil {

    public static GraknGraph getTestGraph() {
        String keyspace = UUID.randomUUID().toString().replaceAll("-", "");
        GraknGraph graknGraph = Grakn.factory(Grakn.IN_MEMORY, keyspace).getGraph();
        graknGraph.showImplicitConcepts(true);
        PokemonGraphFactory.loadGraph(graknGraph);
        MovieGraphFactory.loadGraph(graknGraph);
        QueryBuilder qb = graknGraph.graql();
        qb.insert(
                var().sub("pokemon").has("name", "Pikachu"),
                var().sub("pokemon-type").has("name", "dragon"),
                name("marriage").sub("relation"),
                name("trainer").sub("role"),
                name("pokemon-trained").sub("role"),
                name("type-id").sub("resource-type").datatype(ResourceType.DataType.STRING),
                name("pokemon-type").hasResource("type-id")
        ).execute();

        return graknGraph;
    }
}

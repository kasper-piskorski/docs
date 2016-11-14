package io.grakn;

import io.grakn.example.MovieGraphFactory;
import io.grakn.example.PokemonGraphFactory;
import io.grakn.graql.QueryBuilder;

import java.util.UUID;

import static io.grakn.graql.Graql.id;
import static io.grakn.graql.Graql.var;

public class DocTestUtil {

    public static GraknGraph getTestGraph() {
        String keyspace = UUID.randomUUID().toString().replaceAll("-", "");
        GraknGraph graknGraph = Grakn.factory(Grakn.IN_MEMORY, keyspace).getGraph();
        PokemonGraphFactory.loadGraph(graknGraph);
        MovieGraphFactory.loadGraph(graknGraph);
        QueryBuilder qb = graknGraph.graql();
        qb.insert(
                var().isa("pokemon").has("name", "Pikachu"),
                var().isa("pokemon-type").has("name", "dragon"),
                id("marriage").isa("relation-type"),
                id("trainer").isa("role-type"),
                id("pokemon-trained").isa("role-type")
        ).execute();

        return graknGraph;
    }
}

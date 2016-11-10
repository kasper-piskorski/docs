package io.mindmaps;

import io.mindmaps.example.MovieGraphFactory;
import io.mindmaps.example.PokemonGraphFactory;
import io.mindmaps.graql.QueryBuilder;

import java.util.UUID;

import static io.mindmaps.graql.Graql.id;
import static io.mindmaps.graql.Graql.var;

public class DocTestUtil {

    public static MindmapsGraph getTestGraph() {
        String keyspace = UUID.randomUUID().toString().replaceAll("-", "");
        MindmapsGraph mindmapsGraph = Mindmaps.factory(Mindmaps.IN_MEMORY, keyspace).getGraph();
        PokemonGraphFactory.loadGraph(mindmapsGraph);
        MovieGraphFactory.loadGraph(mindmapsGraph);
        QueryBuilder qb = mindmapsGraph.graql();
        qb.insert(
                var().isa("pokemon").has("name", "Pikachu"),
                var().isa("pokemon-type").has("name", "dragon"),
                id("marriage").isa("relation-type"),
                id("trainer").isa("role-type"),
                id("pokemon-trained").isa("role-type")
        ).execute();

        return mindmapsGraph;
    }
}

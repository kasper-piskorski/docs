import io.mindmaps.*;
import io.mindmaps.concept.*;
import io.mindmaps.example.PokemonGraphFactory;
import io.mindmaps.graql.*;
import static io.mindmaps.graql.Graql.*;

// This is some dumb stuff so IntelliJ doesn't get rid of the imports
Concept concept = null;

String keyspace = UUID.randomUUID().toString().replaceAll("-", "");
MindmapsGraph mindmapsGraph = Mindmaps.factory(Mindmaps.IN_MEMORY, keyspace).getGraph();
PokemonGraphFactory.loadGraph(mindmapsGraph);
QueryBuilder qb = withGraph(mindmapsGraph);
qb.insert(
    id("Pikachu").isa("pokemon"),
    id("dragon").isa("pokemon-type")
).execute();

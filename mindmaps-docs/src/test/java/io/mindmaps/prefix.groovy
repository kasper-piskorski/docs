import io.mindmaps.*;
import io.mindmaps.concept.*;
import io.mindmaps.example.PokemonGraphFactory;
import io.mindmaps.graql.*;
import static io.mindmaps.graql.Graql.*;

// This is some dumb stuff so IntelliJ doesn't get rid of the imports
//noinspection GroovyConstantIfStatement
if (false) {
    Concept concept = null;
    Var var = null;
    PokemonGraphFactory.loadGraph(null);
}

MindmapsGraph mindmapsGraph = DocTestUtil.getTestGraph();
QueryBuilder qb = mindmapsGraph.graql();
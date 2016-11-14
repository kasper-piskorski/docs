import io.grakn.*;
import io.grakn.concept.*;
import io.grakn.example.PokemonGraphFactory;
import io.grakn.graql.*;
import static io.grakn.graql.Graql.*;

// This is some dumb stuff so IntelliJ doesn't get rid of the imports
//noinspection GroovyConstantIfStatement
if (false) {
    Concept concept = null;
    Var var = null;
    PokemonGraphFactory.loadGraph(null);
}

GraknGraph graknGraph = DocTestUtil.getTestGraph();
QueryBuilder qb = graknGraph.graql();
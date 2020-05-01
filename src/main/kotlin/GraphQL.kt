import cats.Cat
import cats.CatsService
import com.apurebase.kgraphql.graphql
import io.ktor.routing.Route

fun Route.graphql(catsService: CatsService) = graphql {
    query("cats") {
        resolver{ -> catsService.all() }
    }

    type<Cat>()
}
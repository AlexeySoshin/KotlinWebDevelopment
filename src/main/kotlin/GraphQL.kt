import cats.Cat
import cats.CatsService
import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.graphql
import io.ktor.routing.Route

fun Route.graphql(catsService: CatsService) = graphql {
    query("cats") {
        resolver { ctx: Context ->
            ctx[String::class]
            catsService.all()
        }
    }
    query("cat") {
        resolver { id: Int ->
            catsService.findById(id)
        }
    }

    type<Cat>()
}
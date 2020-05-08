import cats.Cat
import cats.CatsService
import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.graphql
import io.ktor.routing.Route
import nidomiro.kdataloader.dsl.dataLoader

fun Route.graphql(catsService: CatsService) = graphql {
    query("cat") {
        resolver { id: Int ->
            catsService.findById(id)
        }
    }
    query("cats") {
        resolver { ctx: Context ->
            ctx[String::class]
            catsService.all()
        }
    }

    type<Cat>()
}
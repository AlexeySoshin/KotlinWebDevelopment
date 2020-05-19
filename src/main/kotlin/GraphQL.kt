import cats.Cat
import cats.CatsService
import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.graphql
import com.apurebase.kgraphql.ktor
import io.ktor.routing.Route

fun Route.graphql(catsService: CatsService) = graphql {
    configure {
        ktor {
            playground = true
        }
    }
    mutation("createCat") {
        resolver { name: String, age: Int? ->
            val id = catsService.create(name, age)
            catsService.findById(id)
        }
    }
    query("cats") {
        resolver { ->
            catsService.all()
        }
    }
    query("cat") {
        resolver { id: Int?,
                   name: String? ->
            when {
                id != null -> catsService.findById(id)
                name != null -> //catsService.findByName(id)
                    Cat(id = 0, name = name, age = 0)
                else -> null
            }
        }
    }

    type<Cat>()
}
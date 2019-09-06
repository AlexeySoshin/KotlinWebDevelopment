import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val port = 8080

    val server = embeddedServer(Netty, port, module = Application::mainModule)

    server.start()
}

fun Application.mainModule() {
    val host = "localhost"
    val port = 5555
    val dbName = "cats_db"
    val dbUser = "cats_user"
    val dbPassword = "catspass123"
    val db = Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver",
        user = dbUser, password = dbPassword)

    transaction {
        SchemaUtils.create(Cats)
    }

    install(ContentNegotiation) {
        jackson{
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    routing {
        trace {
            application.log.debug(it.buildText())
        }
        get("/{name}") {
            val name = call.parameters["name"]

            context.respond(mapOf("Cat name:" to name))
        }
        get {
            context.respond(mapOf("Welcome" to "our Cat Hotel"))
        }
    }
}
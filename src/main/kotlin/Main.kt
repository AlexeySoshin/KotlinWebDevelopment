import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val port = 8080

    val server = embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            jackson{
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        routing {
            get {
                context.respond(mapOf("Welcome" to "our Cat Hotel"))
            }
            get("/{name}") {
                val name = call.parameters["name"]

                context.respond(mapOf("Cat name:" to name))
            }
        }
    }

    server.start()
}

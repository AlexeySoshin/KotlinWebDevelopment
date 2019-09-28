import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun emptyPath() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "")

            assertEquals(HttpStatusCode.OK, call.response.status())
        }
    }

    @Test
    fun validValue() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "/Snowflake")

            assertEquals("""
                {
                        "Cat name:" : "Snowflake"
                }
            """.asJson(), call.response.content?.asJson())
        }
    }
}

fun String.asJson() = ObjectMapper().readTree(this)
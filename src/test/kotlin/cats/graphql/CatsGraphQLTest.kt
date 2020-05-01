package cats.graphql

import asJson
import cats.Cats
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import mainModule
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CatsGraphQLTest {

    @Test
    fun `GraphQL returns cats`() {
        withTestApplication(Application::mainModule) {
            val result = handleRequest(HttpMethod.Post, "/graphql") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""
                    { 
                        cats { 
                            name 
                            age 
                        } 
                    }""".asGraphQLQuery())
            }

            assertEquals(
                """{"data":{"cats":[{"age":3,"name":"Shmuzy"},{"age":2,"name":"Fluffy"}]}}""".asJson(),
                result.response.content?.asJson()
            )
        }
    }

    @Before
    fun setup() {
        DB.connect()
        transaction {
            Cats.deleteAll()

            Cats.insert {
                it[name] = "Shmuzy"
                it[age] = 3
            }

            Cats.insert {
                it[name] = "Fluffy"
                it[age] = 2
            }
        }
    }
}

private fun String.asGraphQLQuery() = """{"query": "${this}"}""".replace("\n", "")

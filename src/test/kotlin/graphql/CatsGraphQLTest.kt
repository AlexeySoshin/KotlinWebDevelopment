package graphql

import DB
import asJson
import cats.Cats
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import mainModule
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CatsGraphQLTest {
    @Before
    fun setup() {
        DB.connect()
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Cats)
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

    @Test
    fun `GraphQL returns a single cat by name`() {
        withTestApplication(Application::mainModule) {
            val graphqlResponse = handleRequest(HttpMethod.Post, "/graphql") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""
                        {
                            cat(name: \"Shmuzy\") {
                                name
                            }
                        }
                        """.asGraphQLQuery())
            }

            assertEquals(
                """{"data":{"cat":{"name": "Shmuzy"}}}""".asJson(),
                graphqlResponse.response.content?.asJson()
            )
        }
    }

    @Test
    fun `GraphQL creates a cat`() {
        withTestApplication(Application::mainModule) {
            val graphqlResponse = handleRequest(HttpMethod.Post, "/graphql") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""
                     mutation { 
                        createCat(name: \"Apollo\", age: 4) { 
                            name 
                            age 
                        } 
                     }""".asGraphQLQuery())
            }

            assertEquals(
                """{"data":{"createCat":{"name":"Apollo","age":4}}}""".asJson(),
                graphqlResponse.response.content?.asJson()
            )
        }
    }

    @Test
    fun `GraphQL returns a single cat`() {
        val dbCat = transaction {
            Cats.selectAll().first()
        }
        withTestApplication(Application::mainModule) {
            val graphqlResponse = handleRequest(HttpMethod.Post, "/graphql") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""
                        {
                            cat(id: ${dbCat[Cats.id]}) {
                                name
                            }
                        }
                        """.asGraphQLQuery())
            }

            assertEquals(
                """{"data":{"cat":{"name":"Shmuzy"}}}""".asJson(),
                graphqlResponse.response.content?.asJson()
            )
        }
    }

    @Test
    fun `GraphQL returns cats`() {
        withTestApplication(Application::mainModule) {
            val graphqlResponse = handleRequest(HttpMethod.Post, "/graphql") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""
                        {
                            cats {
                                name 
                                age
                            }
                        }
                        """.asGraphQLQuery())
            }

            assertEquals(
                """{"data":{"cats":[{"age":3,"name":"Shmuzy"},{"age":2,"name":"Fluffy"}]}}""".asJson(),
                graphqlResponse.response.content?.asJson()
            )
        }
    }
}

private fun String.asGraphQLQuery() = """{"query": "${this}"}""".replace("\n", "")

package cats

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface CatsService {
    suspend fun create(name: String, age: Int?): Int

    suspend fun all(): List<Cat>

    suspend fun findById(id: Int): Cat?
}

class CatsServiceDB : CatsService {
    override suspend fun findById(id: Int): Cat? {
        val row = transaction {
            addLogger(StdOutSqlLogger)
            Cats.select {
                Cats.id eq id // select cats.id, cats.name, cats.age from catsw where cats.id = 1
            }.firstOrNull()
        }
        return row?.asCat()
    }

    override suspend fun create(name: String, age: Int?): Int {
        val id = transaction {
            Cats.insertAndGetId { cat ->
                cat[Cats.name] = name
                if (age != null) {
                    cat[Cats.age] = age
                }
            }
        }
        return id.value
    }

    override suspend fun all(): List<Cat> {
        return transaction {
            Cats.selectAll().map { row ->
                row.asCat()
            }
        }
    }

    private fun ResultRow.asCat() = Cat(
        this[Cats.id].value,
        this[Cats.name],
        this[Cats.age]
    )

}


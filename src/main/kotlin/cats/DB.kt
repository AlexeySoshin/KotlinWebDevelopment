package cats

import org.jetbrains.exposed.dao.IntIdTable


object Cats: IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
    val age = integer("age").default(0)
}

data class Cat(val id: Int,
               val name: String,
               val age: Int)

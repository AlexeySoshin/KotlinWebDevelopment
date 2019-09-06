import org.jetbrains.exposed.dao.IntIdTable

object Cats: IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
    val age = integer("age").default(0)
}
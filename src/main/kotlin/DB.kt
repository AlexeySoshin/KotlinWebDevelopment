import org.jetbrains.exposed.sql.Database

object DB {
    private val host = "localhost"
    private val port = 5555
    private val dbName = "cats_db"
    private val dbUser = "cats_user"
    private val dbPassword = "catspass123"
    fun connect() = Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver",
        user = dbUser, password = dbPassword)
}

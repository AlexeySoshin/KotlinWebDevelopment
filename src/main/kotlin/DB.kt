import org.jetbrains.exposed.sql.Database

object DB {
    private val host = System.getenv("DB_HOST")
    private val port = System.getenv("DB_PORT")
    private val dbName = System.getenv("DB_NAME")
    private val dbUser = System.getenv("DB_USER")
    private val dbPassword = System.getenv("DB_PASSWORD")
    fun connect() = Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver",
        user = dbUser, password = dbPassword)
}

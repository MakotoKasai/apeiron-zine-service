package art.kasai.apeiron.zine.infrastructure.db

import org.jetbrains.exposed.dao.id.LongIdTable
object ProjectTable : LongIdTable("projects") {
    var title = varchar("title", length = 255)
    val description = text("description")
    val createdAt = long("created_at")
}
package art.kasai.apeiron.zine.infrastructure.db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object ImageTable : LongIdTable("images") {
    val projectId = reference(
        name = "project_id",
        foreign = ProjectTable,
        onDelete = ReferenceOption.CASCADE
    )
    val filePath = varchar("file_path", length = 1024)
    val caption = text("caption").default("")
    val sortOrder = integer("sort_order").default(0)
    val createdAt = long("created_at")
}
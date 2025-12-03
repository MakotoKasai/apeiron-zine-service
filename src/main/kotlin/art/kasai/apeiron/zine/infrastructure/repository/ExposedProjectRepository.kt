package art.kasai.apeiron.zine.infrastructure.repository

import art.kasai.apeiron.zine.domain.model.Project
import art.kasai.apeiron.zine.domain.repository.ProjectRepository
import art.kasai.apeiron.zine.infrastructure.db.DatabaseFactory
import art.kasai.apeiron.zine.infrastructure.db.ProjectTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.update

class ExposedProjectRepository: ProjectRepository {

    private fun toProject(row: ResultRow): Project =
        Project(
            id = row[ProjectTable.id].value,
            title = row[ProjectTable.title],
            description = row[ProjectTable.description],
            createdAt = row[ProjectTable.createdAt],
        )

    override suspend fun create(title: String, description: String): Project =
        DatabaseFactory.dbQuery {
            val now = System.currentTimeMillis()
            val insertedId = ProjectTable.insertAndGetId { row ->
                row[ProjectTable.title] = title
                row[ProjectTable.description] = description
                row[ProjectTable.createdAt] = now
            }
            Project(
                id = insertedId.value,
                title = title,
                description = description,
                createdAt = now,
            )
        }

    override suspend fun findById(id: Long): Project? =
        DatabaseFactory.dbQuery {
            ProjectTable
                .selectAll().where { ProjectTable.id eq id }
                .limit(1)
                .firstOrNull()
                ?.let { toProject(it) }
        }

    override suspend fun findAll(): List<Project> =
        DatabaseFactory.dbQuery {
            ProjectTable
                .selectAll()
                .orderBy(ProjectTable.createdAt, SortOrder.DESC)
                .map { toProject(it) }
        }

    override suspend fun update(id: Long, title: String, description: String): Project? =
        DatabaseFactory.dbQuery {
            val exists = ProjectTable.selectAll().where { ProjectTable.id eq id }.firstOrNull()
            if( exists == null ) return@dbQuery null

            ProjectTable.update({ ProjectTable.id eq id }) { row ->
                row[ProjectTable.title] = title
                row[ProjectTable.description] = description
            }

            ProjectTable
                .selectAll().where { ProjectTable.id eq id }
                .first()
                .let { toProject(it) }
        }

    override suspend fun delete(id: Long): Boolean =
        DatabaseFactory.dbQuery {
            ProjectTable.deleteWhere { ProjectTable.id eq id } > 0
        }

}
package art.kasai.apeiron.zine.infrastructure.repository

import art.kasai.apeiron.zine.domain.model.Image
import art.kasai.apeiron.zine.domain.repository.ImageRepository
import art.kasai.apeiron.zine.infrastructure.db.DatabaseFactory
import art.kasai.apeiron.zine.infrastructure.db.ImageTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ExposedImageRepository: ImageRepository {

    private fun toImage(row: ResultRow): Image =
        Image(
            id = row[ImageTable.id].value,
            projectId = row[ImageTable.projectId].value,
            filePath = row[ImageTable.filePath],
            caption = row[ImageTable.caption],
            sortOrder = row[ImageTable.sortOrder],
            createdAt = row[ImageTable.createdAt],
        )

    override suspend fun add(
        projectId: Long,
        filePath: String,
        caption: String?,
        sortOrder: Int?,
    ): Image = DatabaseFactory.dbQuery {
        val nowTime = System.currentTimeMillis()

        val nextOrder = sortOrder ?: ((
                ImageTable
                    .select(ImageTable.sortOrder.max())
                    .where { ImageTable.projectId eq projectId }
                    .firstOrNull()
                    ?.getOrNull(ImageTable.sortOrder.max())
                    ?: 0
                )
                + 1)

        val id = ImageTable.insertAndGetId { row ->
            row[ImageTable.projectId] = projectId
            row[ImageTable.filePath] = filePath
            row[ImageTable.caption] = caption ?: ""
            row[ImageTable.sortOrder] = nextOrder
            row[ImageTable.createdAt] = nowTime
        }

        Image(
            id = id.value,
            projectId = projectId,
            filePath = filePath,
            caption = caption ?: "",
            sortOrder = nextOrder,
            createdAt = nowTime
        )
    }

    override suspend fun findByProject(projectId: Long): List<Image> =
        DatabaseFactory.dbQuery {
            ImageTable
                .selectAll().where { ImageTable.projectId eq projectId }
                .orderBy( ImageTable.sortOrder to SortOrder.ASC)
                .map { toImage(it) }
        }

    override suspend fun delete(imageId: Long): Boolean =
        DatabaseFactory.dbQuery {
            ImageTable.deleteWhere { ImageTable.id eq imageId } >0
        }

}
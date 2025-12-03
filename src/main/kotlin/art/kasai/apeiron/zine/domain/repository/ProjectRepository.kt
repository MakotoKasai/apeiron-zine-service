package art.kasai.apeiron.zine.domain.repository

import art.kasai.apeiron.zine.domain.model.Project

interface ProjectRepository {
    suspend fun create(title: String, description: String): Project
    suspend fun findById(id: Long): Project?
    suspend fun findAll(): List<Project>
    suspend fun update(id: Long, title: String, description: String): Project?
    suspend fun delete(id: Long): Boolean
}
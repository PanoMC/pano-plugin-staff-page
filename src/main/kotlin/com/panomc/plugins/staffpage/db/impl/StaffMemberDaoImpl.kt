package com.panomc.plugins.staffpage.db.impl

import com.panomc.platform.annotation.Dao
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import com.panomc.plugins.staffpage.db.model.StaffMember
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.mysqlclient.MySQLClient
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope

@Dao
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class StaffMemberDaoImpl : StaffMemberDao() {

    override suspend fun init(sqlClient: SqlClient) {
        sqlClient
            .query(
                """
                    CREATE TABLE IF NOT EXISTS `${getTablePrefix() + tableName}` (
                      `id` bigint NOT NULL AUTO_INCREMENT,
                      `name` varchar(255) NOT NULL,
                      `role` varchar(255) NOT NULL,
                      `avatarUrl` varchar(512),
                      `socialLinks` text NOT NULL,
                      `priority` int NOT NULL DEFAULT 0,
                      `description` text,
                      `createdAt` bigint NOT NULL,
                      `updatedAt` bigint NOT NULL,
                      PRIMARY KEY (`id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Staff members table.';
                """.trimIndent()
            )
            .execute()
            .coAwait()
    }

    override suspend fun add(staffMember: StaffMember, sqlClient: SqlClient): Long {
        val query = "INSERT INTO `${getTablePrefix() + tableName}` (`name`, `role`, `avatarUrl`, `socialLinks`, `priority`, `description`, `createdAt`, `updatedAt`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        val rows: RowSet<Row> = sqlClient
            .preparedQuery(query)
            .execute(
                Tuple.of(
                    staffMember.name,
                    staffMember.role,
                    staffMember.avatarUrl,
                    staffMember.socialLinks,
                    staffMember.priority,
                    staffMember.description,
                    staffMember.createdAt,
                    staffMember.updatedAt
                )
            )
            .coAwait()
        return rows.property(MySQLClient.LAST_INSERTED_ID)
    }

    override suspend fun update(staffMember: StaffMember, sqlClient: SqlClient) {
        val query = "UPDATE `${getTablePrefix() + tableName}` SET `name` = ?, `role` = ?, `avatarUrl` = ?, `socialLinks` = ?, `priority` = ?, `description` = ?, `updatedAt` = ? WHERE `id` = ?"
        sqlClient.preparedQuery(query).execute(
            Tuple.of(
                staffMember.name,
                staffMember.role,
                staffMember.avatarUrl,
                staffMember.socialLinks,
                staffMember.priority,
                staffMember.description,
                System.currentTimeMillis(),
                staffMember.id
            )
        ).coAwait()
    }

    override suspend fun deleteById(id: Long, sqlClient: SqlClient) {
        val query = "DELETE FROM `${getTablePrefix() + tableName}` WHERE `id` = ?"
        sqlClient.preparedQuery(query).execute(Tuple.of(id)).coAwait()
    }

    override suspend fun getById(id: Long, sqlClient: SqlClient): StaffMember? {
        val query = "SELECT ${fields.toTableQuery()} FROM `${getTablePrefix() + tableName}` WHERE `id` = ?"
        val rows = sqlClient.preparedQuery(query).execute(Tuple.of(id)).coAwait()
        return rows.toEntities().getOrNull(0)
    }

    override suspend fun getAll(sqlClient: SqlClient): List<StaffMember> {
        val query = "SELECT ${fields.toTableQuery()} FROM `${getTablePrefix() + tableName}`"
        val rows = sqlClient.query(query).execute().coAwait()
        return rows.toEntities()
    }

    override suspend fun getAllOrdered(sqlClient: SqlClient): List<StaffMember> {
        val query = "SELECT ${fields.toTableQuery()} FROM `${getTablePrefix() + tableName}` ORDER BY `priority` ASC, `name` ASC"
        val rows = sqlClient.query(query).execute().coAwait()
        return rows.toEntities()
    }

    override suspend fun uninstall(sqlClient: SqlClient) {
        sqlClient.query("DROP TABLE IF EXISTS `${getTablePrefix() + tableName}`").execute().coAwait()
    }
}

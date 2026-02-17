package com.panomc.plugins.staffpage.db.dao

import com.panomc.platform.db.Dao
import com.panomc.plugins.staffpage.db.model.StaffMember
import io.vertx.sqlclient.SqlClient

abstract class StaffMemberDao : Dao<StaffMember>(StaffMember::class.java) {
    abstract suspend fun add(staffMember: StaffMember, sqlClient: SqlClient): Long
    abstract suspend fun update(staffMember: StaffMember, sqlClient: SqlClient)
    abstract suspend fun deleteById(id: Long, sqlClient: SqlClient)
    abstract suspend fun getById(id: Long, sqlClient: SqlClient): StaffMember?
    abstract suspend fun getAll(sqlClient: SqlClient): List<StaffMember>
    abstract suspend fun getAllOrdered(sqlClient: SqlClient): List<StaffMember>
}

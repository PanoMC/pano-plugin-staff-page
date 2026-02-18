package com.panomc.plugins.staffpage.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import com.panomc.plugins.staffpage.permission.ManageStaffPermission
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository

@Endpoint
class PanelGetStaffAPI(
    private val plugin: StaffPagePlugin
) : PanelApi() {
    override val paths = listOf(Path("/api/panel/staffs", RouteType.GET))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val staffMemberDao by lazy {
        plugin.pluginBeanContext.getBean(StaffMemberDao::class.java)
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageStaffPermission(), context)
        val staff = staffMemberDao.getAllOrdered(getSqlClient())
        return Successful(mapOf("staff" to staff.map { JsonObject.mapFrom(it).map }))
    }
}

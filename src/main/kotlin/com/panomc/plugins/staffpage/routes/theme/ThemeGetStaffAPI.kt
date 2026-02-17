package com.panomc.plugins.staffpage.routes.theme

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository

@Endpoint
class ThemeGetStaffAPI(
    private val plugin: StaffPagePlugin
) : Api() {
    override val paths = listOf(Path("/api/staff", RouteType.GET))

    private val staffMemberDao by lazy {
        plugin.pluginBeanContext.getBean(StaffMemberDao::class.java)
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        val staff = staffMemberDao.getAllOrdered(getSqlClient())
        return Successful(mapOf("staff" to staff.map { JsonObject.mapFrom(it).map }))
    }
}

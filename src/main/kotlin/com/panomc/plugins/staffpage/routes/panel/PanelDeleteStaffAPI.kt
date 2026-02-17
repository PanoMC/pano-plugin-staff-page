package com.panomc.plugins.staffpage.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import com.panomc.plugins.staffpage.permission.ManageStaffPermission
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.Parameters
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import io.vertx.json.schema.common.dsl.Schemas.numberSchema

@Endpoint
class PanelDeleteStaffAPI(
    private val plugin: StaffPagePlugin
) : PanelApi() {
    override val paths = listOf(Path("/api/panel/staff/:id", RouteType.DELETE))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val staffMemberDao by lazy {
        plugin.pluginBeanContext.getBean(StaffMemberDao::class.java)
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .pathParameter(Parameters.param("id", numberSchema()))
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageStaffPermission(), context)

        val id = context.pathParam("id").toLong()
        staffMemberDao.deleteById(id, getSqlClient())
        return Successful()
    }
}

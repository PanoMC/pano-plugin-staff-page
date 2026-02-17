package com.panomc.plugins.staffpage.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.error.NotFound
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import com.panomc.plugins.staffpage.db.model.StaffMember
import com.panomc.plugins.staffpage.permission.ManageStaffPermission
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.Bodies
import io.vertx.ext.web.validation.builder.Parameters
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class PanelUpdateStaffAPI(
    private val plugin: StaffPagePlugin
) : PanelApi() {
    override val paths = listOf(Path("/api/panel/staff/:id", RouteType.PUT))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val staffMemberDao by lazy {
        plugin.pluginBeanContext.getBean(StaffMemberDao::class.java)
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .pathParameter(Parameters.param("id", numberSchema()))
            .body(
                Bodies.json(
                    objectSchema()
                        .optionalProperty("name", stringSchema())
                        .optionalProperty("role", stringSchema())
                        .optionalProperty("avatarUrl", stringSchema())
                        .optionalProperty("socialLinks", stringSchema())
                        .optionalProperty("priority", intSchema())
                        .optionalProperty("description", stringSchema())
                )
            )
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageStaffPermission(), context)

        val id = context.pathParam("id").toLong()
        val existing = staffMemberDao.getById(id, getSqlClient()) ?: return NotFound()

        val body = context.body().asJsonObject()
        val updated = StaffMember(
            id = id,
            name = body.getString("name") ?: existing.name,
            role = body.getString("role") ?: existing.role,
            avatarUrl = body.getString("avatarUrl") ?: existing.avatarUrl,
            socialLinks = body.getString("socialLinks") ?: existing.socialLinks,
            priority = body.getInteger("priority") ?: existing.priority,
            description = body.getString("description") ?: existing.description,
            createdAt = existing.createdAt
        )

        staffMemberDao.update(updated, getSqlClient())
        return Successful()
    }
}

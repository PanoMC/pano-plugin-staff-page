package com.panomc.plugins.staffpage.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import com.panomc.plugins.staffpage.db.model.StaffMember
import com.panomc.plugins.staffpage.permission.ManageStaffPermission
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.Bodies
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class PanelCreateStaffAPI(
    private val plugin: StaffPagePlugin
) : PanelApi() {
    override val paths = listOf(Path("/api/panel/staffs", RouteType.POST))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val staffMemberDao by lazy {
        plugin.pluginBeanContext.getBean(StaffMemberDao::class.java)
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .body(
                Bodies.json(
                    objectSchema()
                        .requiredProperty("name", stringSchema())
                        .requiredProperty("role", stringSchema())
                        .optionalProperty("avatarUrl", stringSchema())
                        .optionalProperty("socialLinks", stringSchema())
                        .optionalProperty("priority", intSchema())
                        .optionalProperty("description", stringSchema())
                )
            )
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageStaffPermission(), context)

        val body = context.body().asJsonObject()
        val staffMember = StaffMember(
            name = body.getString("name"),
            role = body.getString("role"),
            avatarUrl = body.getString("avatarUrl"),
            socialLinks = body.getString("socialLinks") ?: "{}",
            priority = body.getInteger("priority") ?: 0,
            description = body.getString("description")
        )

        val id = staffMemberDao.add(staffMember, getSqlClient())
        return Successful(JsonObject().put("id", id).map)
    }
}

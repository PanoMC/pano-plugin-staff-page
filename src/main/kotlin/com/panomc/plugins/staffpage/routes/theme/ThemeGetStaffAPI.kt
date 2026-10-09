package com.panomc.plugins.staffpage.routes.theme

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import com.panomc.platform.schema.dsl.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import com.panomc.platform.schema.EndpointDoc
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class ThemeGetStaffAPI(
    private val plugin: StaffPagePlugin
) : Api() {
    override val paths = listOf(Path("/staffs", RouteType.GET))

    override val doc = EndpointDoc(
        summary = "The staff members in display order.",
        tag = "staff",
        response = objectSchema()
            .requiredProperty(
                "items",
                arraySchema().items(
                    objectSchema()
                        .requiredProperty("id", intSchema())
                        .requiredProperty("name", stringSchema())
                        .requiredProperty("role", stringSchema())
                        .optionalProperty("avatarUrl", stringSchema().nullable())
                        .optionalProperty("socialLinks", stringSchema())
                        .optionalProperty("priority", intSchema())
                        .optionalProperty("description", stringSchema().nullable())
                        .optionalProperty("createdAt", intSchema())
                        .optionalProperty("updatedAt", intSchema())
                )
            )
    )

    private val staffMemberDao by lazy {
        plugin.pluginBeanContext.getBean(StaffMemberDao::class.java)
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        val staff = staffMemberDao.getAllOrdered(getSqlClient())
        return Successful(mapOf("items" to staff.map { JsonObject.mapFrom(it).map }))
    }
}

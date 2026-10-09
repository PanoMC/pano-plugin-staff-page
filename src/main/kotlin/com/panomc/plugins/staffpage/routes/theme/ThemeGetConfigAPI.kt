package com.panomc.plugins.staffpage.routes.theme

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.config.StaffPageConfig
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import com.panomc.platform.schema.dsl.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import com.panomc.platform.schema.EndpointDoc
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class ThemeGetConfigAPI(
    private val plugin: StaffPagePlugin
) : Api() {
    override val paths = listOf(Path("/staff/config", RouteType.GET))

    override val doc = EndpointDoc(
        summary = "How the staff page is shown: its address, layout and place.",
        tag = "staff",
        response = objectSchema()
            .requiredProperty("pageUrl", stringSchema())
            .requiredProperty("viewMode", stringSchema())
            .requiredProperty("displayLocation", stringSchema())
    )

    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<StaffPageConfig>
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        return Successful(JsonObject.mapFrom(configManager.config).map)
    }
}

package com.panomc.plugins.staffpage.routes.theme

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.config.StaffPageConfig
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository

@Endpoint
class ThemeGetConfigAPI(
    private val plugin: StaffPagePlugin
) : Api() {
    override val paths = listOf(Path("/api/staff/config", RouteType.GET))

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

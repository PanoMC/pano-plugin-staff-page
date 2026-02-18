package com.panomc.plugins.staffpage.routes.panel

import com.panomc.platform.annotation.Endpoint
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.auth.AuthProvider
import com.panomc.platform.model.*
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.config.StaffPageConfig
import com.panomc.plugins.staffpage.permission.ManageStaffSettingsPermission
import com.panomc.platform.db.DatabaseManager
import com.panomc.plugins.staffpage.log.UpdatedStaffSettingsLog
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.validation.ValidationHandler
import io.vertx.ext.web.validation.builder.Bodies
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder
import io.vertx.json.schema.SchemaRepository
import io.vertx.json.schema.common.dsl.Schemas.*

@Endpoint
class PanelUpdateConfigAPI(
    private val plugin: StaffPagePlugin,
) : PanelApi() {
    override val paths = listOf(Path("/api/panel/staff/config", RouteType.PUT))

    private val authProvider by lazy {
        plugin.applicationContext.getBean(AuthProvider::class.java)
    }

    private val databaseManager by lazy {
        plugin.applicationContext.getBean(DatabaseManager::class.java)
    }

    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<StaffPageConfig>
    }

    override fun getValidationHandler(schemaRepository: SchemaRepository): ValidationHandler =
        ValidationHandlerBuilder.create(schemaRepository)
            .body(
                Bodies.json(
                    objectSchema()
                        .optionalProperty("pageUrl", stringSchema())
                        .optionalProperty("viewMode", stringSchema())
                        .optionalProperty("customHtml", stringSchema())
                        .optionalProperty("showInSupport", booleanSchema())
                )
            )
            .build()

    override suspend fun handle(context: RoutingContext): Result {
        authProvider.requirePermission(ManageStaffSettingsPermission(), context)

        val body = context.body().asJsonObject()
        configManager.saveConfig(body)

        val sqlClient = getSqlClient()
        val userId = authProvider.getUserIdFromRoutingContext(context)
        val username = databaseManager.userDao.getUsernameFromUserId(userId, sqlClient)!!

        databaseManager.panelActivityLogDao.add(
            UpdatedStaffSettingsLog(userId, username, plugin.pluginId),
            sqlClient
        )

        return Successful()
    }
}

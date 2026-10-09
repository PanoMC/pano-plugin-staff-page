package com.panomc.plugins.staffpage.sitemap

import com.panomc.platform.api.SitemapEntry
import com.panomc.platform.api.SitemapProvider
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.plugins.staffpage.StaffPagePlugin
import com.panomc.plugins.staffpage.config.DisplayLocation
import com.panomc.plugins.staffpage.config.StaffPageConfig
import com.panomc.plugins.staffpage.db.dao.StaffMemberDao
import io.vertx.sqlclient.SqlClient
import org.springframework.stereotype.Component

/** The staff page, when it is a page of its own and has at least one member, for `GET /api/v1/sitemap`. */
@Component
class StaffPageSitemapProvider(
    private val plugin: StaffPagePlugin,
    private val staffMemberDao: StaffMemberDao
) : SitemapProvider {
    private val configManager by lazy {
        plugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<StaffPageConfig>
    }

    override suspend fun entries(sqlClient: SqlClient): List<SitemapEntry> {
        val config = configManager.config

        if (config.displayLocation != DisplayLocation.THEME_PAGE) {
            return emptyList()
        }

        val staff = staffMemberDao.getAll(sqlClient)

        if (staff.isEmpty()) {
            return emptyList()
        }

        return listOf(
            SitemapEntry("pano-plugin-staff-page:staff", mapOf("url" to config.pageUrl), staff.maxOf { it.updatedAt })
        )
    }
}

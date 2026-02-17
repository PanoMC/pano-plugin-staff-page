package com.panomc.plugins.staffpage

import com.panomc.platform.api.PanoPlugin
import com.panomc.platform.api.config.PluginConfigManager
import com.panomc.platform.setup.SetupManager
import com.panomc.plugins.staffpage.config.StaffPageConfig

class StaffPagePlugin : PanoPlugin() {
    private val pluginDatabaseManager by lazy {
        applicationContext.getBean(com.panomc.platform.api.PluginDatabaseManager::class.java)
    }

    private val setupManager by lazy {
        applicationContext.getBean(SetupManager::class.java)
    }

    private var isInitialized = false

    override suspend fun onStart() {
        logger.info("Starting Staff Page Plugin...")
        startPlugin()
    }

    internal suspend fun startPlugin() {
        if (isInitialized) return
        isInitialized = true

        if (!setupManager.isSetupDone()) {
            logger.info("Setup is not finished, waiting for setup completion...")
            return
        }

        val configManager = PluginConfigManager(this, StaffPageConfig::class.java)
        pluginBeanContext.beanFactory.registerSingleton(PluginConfigManager::class.java.name, configManager)

        pluginDatabaseManager.initialize(this)

        logger.info("Staff Page Plugin Started!")
    }

    override suspend fun onEnable() {
        logger.info("Staff Page Plugin Enabled!")
    }

    override suspend fun onDisable() {
        isInitialized = false
    }

    override suspend fun onUninstall() {
        logger.info("Uninstalling Staff Page Plugin...")
        pluginDatabaseManager.uninstall(this)
    }
}

package com.panomc.plugins.staffpage

import com.panomc.platform.api.PanoPlugin

class StaffPagePlugin : PanoPlugin() {
    override suspend fun onStart() {
        logger.info("Starting Staff Page Plugin...")
    }

    override suspend fun onEnable() {
        logger.info("Staff Page Plugin Enabled!")
    }

    override suspend fun onUninstall() {
        logger.info("Uninstalling Staff Page Plugin...")
    }
}

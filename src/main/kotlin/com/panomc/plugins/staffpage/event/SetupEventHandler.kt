package com.panomc.plugins.staffpage.event

import com.panomc.platform.api.annotation.EventListener
import com.panomc.platform.api.event.SetupEventListener
import com.panomc.plugins.staffpage.StaffPagePlugin

@EventListener
class SetupEventHandler(private val plugin: StaffPagePlugin) : SetupEventListener {
    private val logger by lazy {
        plugin.logger
    }

    override suspend fun onSetupFinished() {
        // isRunning, not plugin.pluginState: older Pano versions keep pluginState at its creation value.
        if (plugin.isRunning) {
            logger.info("Setup finished! Initializing plugin...")

            plugin.startPlugin()
        }
    }
}

package com.panomc.plugins.staffpage.config

import com.panomc.platform.api.config.PluginConfig

class StaffPageConfig(
    val pageUrl: String = "staff",
    val viewMode: ViewMode = ViewMode.CARD,
    val displayLocation: DisplayLocation = DisplayLocation.THEME_PAGE,
    version: Int = 1
) : PluginConfig(version)

enum class DisplayLocation {
    THEME_PAGE,
    SUPPORT_PAGE
}

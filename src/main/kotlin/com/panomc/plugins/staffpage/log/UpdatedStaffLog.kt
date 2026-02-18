package com.panomc.plugins.staffpage.log

import com.panomc.platform.db.model.PluginActivityLog
import io.vertx.core.json.JsonObject

class UpdatedStaffLog(
    userId: Long,
    username: String,
    pluginId: String,
    staffName: String
) : PluginActivityLog(
    userId = userId,
    pluginId = pluginId,
    details = JsonObject().put("target", staffName).put("username", username)
)

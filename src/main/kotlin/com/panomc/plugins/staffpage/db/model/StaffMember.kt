package com.panomc.plugins.staffpage.db.model

import com.panomc.platform.db.DBEntity

open class StaffMember(
    var id: Long = -1,
    val name: String = "",
    val role: String = "",
    val avatarUrl: String? = null,
    val socialLinks: String = "{}", // JSON string
    val priority: Int = 0,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : DBEntity()

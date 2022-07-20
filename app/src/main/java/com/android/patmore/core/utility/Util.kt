package com.android.patmore.core.utility

import com.github.marlonlom.utilities.timeago.TimeAgo
import timber.log.Timber
import java.text.SimpleDateFormat

fun String.toMillis(): Long {
    val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"

    val date = SimpleDateFormat(dateFormat)
    return try {
        val res = date.parse(this).time
        res
    } catch (e: Exception) {
        Timber.e(e.stackTraceToString())
        0
    }
}

fun Long.toRelativeTime(): String {
    var res = TimeAgo.using(this)
    res = res.replace("within", "")
    res = res.replace("about", "")
    res = res.replace("an hour", "1 hour")
    res += " ago"
    return res.trim()
}

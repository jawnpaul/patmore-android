package com.android.patmore.core.utility

import com.github.marlonlom.utilities.timeago.TimeAgo
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

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
    return if (res.contains("ago")) {
        res.trim()
    } else {
        res += " ago".trim()
        res
    }
}

fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") {
    it.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase(
            Locale.getDefault()
        ) else char.toString()
    }
}.trim()

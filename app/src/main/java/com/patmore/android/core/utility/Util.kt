package com.patmore.android.core.utility

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    res = res.replace("yesterday", "1 day ")
    res = res.replace("an hour", "1 hour ")
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

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun RecyclerView.initRecyclerViewWithoutLineDecoration(context: Context) {
    val linearLayoutManager = LinearLayoutManager(context)
    layoutManager = linearLayoutManager
}

fun RecyclerView.initRecyclerViewWithLineDecoration(context: Context) {
    val linearLayoutManager = LinearLayoutManager(context)
    layoutManager = linearLayoutManager
    addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
}

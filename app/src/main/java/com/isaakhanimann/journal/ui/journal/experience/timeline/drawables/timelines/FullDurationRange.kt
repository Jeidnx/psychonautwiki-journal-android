package com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.timelines

import com.isaakhanimann.journal.data.substances.classes.roa.DurationRange

data class FullDurationRange(
    val minInSeconds: Float,
    val maxInSeconds: Float
) {
    fun interpolateAtValueInSeconds(value: Float): Float {
        val diff = maxInSeconds - minInSeconds
        return minInSeconds + diff.times(value)
    }
}

fun DurationRange.toFullDurationRange(): FullDurationRange? {
    return if (minInSec != null && maxInSec != null) {
        FullDurationRange(minInSec, maxInSec)
    } else {
        null
    }
}
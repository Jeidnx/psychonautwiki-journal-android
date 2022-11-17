/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.dottedStroke
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.normalStroke
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.shapeAlpha

data class OnsetTotalTimeline(
    val onset: FullDurationRange,
    val total: FullDurationRange,
    val totalWeight: Float
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float>? {
        return null
    }

    override val widthInSeconds: Float =
        total.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        val onsetWeight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(onsetWeight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
            },
            color = color,
            style = density.normalStroke
        )
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = onsetEndX, y = height)
                val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = onsetEndX,
                    endX = totalX / 2f,
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalX / 2f,
                    startY = 0f,
                    endX = totalX,
                    endY = height
                )
            },
            color = color,
            style = density.dottedStroke
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetEndMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val onsetEndMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                val totalMinX =
                    total.minInSeconds * pixelsPerSec
                val totalMaxX =
                    total.maxInSeconds * pixelsPerSec
                moveTo(onsetEndMinX, height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = onsetEndMinX,
                    endX = totalX / 2,
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalX / 2,
                    startY = 0f,
                    endX = totalMaxX,
                    endY = height
                )
                lineTo(x = totalMinX, y = height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalMinX,
                    endX = totalX / 2,
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalX / 2,
                    startY = 0f,
                    endX = onsetEndMaxX,
                    endY = height
                )
                close()
            },
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetTotalTimeline(totalWeight: Float): OnsetTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullTotal != null) {
        OnsetTotalTimeline(
            onset = fullOnset,
            total = fullTotal,
            totalWeight = totalWeight
        )
    } else {
        null
    }
}
package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.endSmoothLineTo
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.startSmoothLineTo

data class TotalTimeline(
    val total: FullDurationRange,
    val weight: Float = 0.5f,
    val percentSmoothness: Float = 0.5f,
) : TimelineDrawable {
    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        strokeWidth: Float
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val totalMinX = total.minInSeconds * pixelsPerSec
                val totalX = total.interpolateAtValueInSeconds(weight) * pixelsPerSec
                moveTo(startX, height)
                endSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                startSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalX,
                    endY = height
                )
            },
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 30f))
            )
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color
    ) {
        drawScope.drawPath(
            path = Path().apply {
                // path over top
                val totalMinX = total.minInSeconds * pixelsPerSec
                val totalMaxX = total.maxInSeconds * pixelsPerSec
                moveTo(x = startX + (totalMinX / 2), y = 0f)
                startSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalMaxX,
                    endY = height
                )
                lineTo(x = startX + totalMaxX, y = height)
                // path bottom back
                lineTo(x = startX + totalMinX, y = height)
                endSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX + totalMinX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                close()
            },
            color = color.copy(alpha = 0.1f)
        )
    }

    override val widthInSeconds: Float = total.maxInSeconds
}

fun RoaDuration.toTotalTimeline(): TotalTimeline? {
    val fullTotal = total?.toFullDurationRange()
    return if (fullTotal != null) {
        TotalTimeline(total = fullTotal)
    } else {
        null
    }
}
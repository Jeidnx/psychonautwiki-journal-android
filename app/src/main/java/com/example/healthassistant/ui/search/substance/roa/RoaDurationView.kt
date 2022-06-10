package com.example.healthassistant.ui.search.substance.roa

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.DurationRange
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaDurationPreviewProviderForRoaDurationView
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProviderForRoaDurationView::class) roaDuration: RoaDuration
) {
    RoaDurationView(roaDuration = roaDuration, maxDuration = 13.toDuration(DurationUnit.HOURS))
}

@Composable
fun RoaDurationView(
    roaDuration: RoaDuration,
    maxDuration: Duration?
) {
    Column {
        val total = roaDuration.total
        val colorTimeLine = MaterialTheme.colors.secondary
        val colorTransparent = colorTimeLine.copy(alpha = 0.1f)
        val strokeWidth = 8f
        if ((total?.min != null) && (total.max != null)) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "total: ${total.text}",
                    textAlign = TextAlign.Center
                )
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    val canvasWidth = size.width
                    val max = maxDuration ?: total.max
                    val minX = (total.min.div(max) * canvasWidth).toFloat()
                    val maxX = (total.max.div(max) * canvasWidth).toFloat()
                    val midX = (minX + maxX) / 2
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = midX, y = 0f),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        start = Offset(x = minX, y = 0f),
                        end = Offset(x = maxX, y = 0f),
                        color = colorTransparent,
                        strokeWidth = 60f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        if (roaDuration.afterglow != null) {
            Text("after effects: ${roaDuration.afterglow.text}")
        } else {
            Spacer(modifier = Modifier.height(15.dp))
        }
        val undefinedCount = 4 - roaDuration.numberOfTimelineDurationsDefined
        if (maxDuration != null && undefinedCount < 4) {
            TimelineWithRange(
                roaDuration = roaDuration,
                maxDuration = maxDuration,
                undefinedCount = undefinedCount,
                strokeWidth = strokeWidth,
                colorTimeLine = colorTimeLine,
                colorTransparent = colorTransparent
            )
        }
    }
}

@Composable
fun TimelineWithRange(
    roaDuration: RoaDuration,
    maxDuration: Duration,
    undefinedCount: Int,
    strokeWidth: Float,
    colorTimeLine: Color,
    colorTransparent: Color
) {
    val isDarkTheme = isSystemInDarkTheme()
    val density = LocalDensity.current
    val textColor = if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
    val textSizeDen = density.run { 30f }
    val textPaintAlignCenter = remember(density) {
        Paint().apply {
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = textSizeDen
        }
    }
    val textPaintAlignLeft = remember(density) {
        Paint().apply {
            color = textColor
            textAlign = Paint.Align.LEFT
            textSize = textSizeDen
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        val canvasWidth = size.width
        val pixelsPerSec = canvasWidth.div(maxDuration.inWholeSeconds)
        val canvasHeightOuter = size.height
        val wholeDuration = roaDuration.total?.interpolateAt(0.5) ?: maxDuration
        val restDuration = wholeDuration - roaDuration.sumOfInterpolatedDurations
        val divider = if (undefinedCount == 0) 1 else undefinedCount
        val dottedLineWidths = restDuration.div(divider).inWholeSeconds * pixelsPerSec
        val onset = roaDuration.onset
        val comeup = roaDuration.comeup
        val peak = roaDuration.peak
        val offset = roaDuration.offset
        inset(vertical = strokeWidth / 2) {
            val canvasHeight = size.height
            drawLineThatMightBeDotted(
                onset,
                comeup,
                peak,
                offset,
                pixelsPerSec,
                dottedLineWidths,
                canvasHeight,
                strokeWidth,
                colorTimeLine,
                textPaintAlignCenter,
                textPaintAlignLeft
            )
        }
        drawFillPathForDurationRangesInTransparentColor(
            onset,
            comeup,
            peak,
            offset,
            pixelsPerSec,
            dottedLineWidths,
            canvasHeightOuter,
            colorTransparent
        )
    }
}

fun DrawScope.drawLineThatMightBeDotted(
    onset: DurationRange?,
    comeup: DurationRange?,
    peak: DurationRange?,
    offset: DurationRange?,
    pixelsPerSec: Float,
    dottedLineWidths: Float,
    canvasHeight: Float,
    strokeWidth: Float,
    colorTimeLine: Color,
    textPaintAlignCenter: Paint,
    textPaintAlignLeft: Paint
) {
    val onsetInterpol = onset?.interpolateAt(0.5)
    val comeupInterpol = comeup?.interpolateAt(0.5)
    val peakInterpol = peak?.interpolateAt(0.5)
    val offsetInterpol = offset?.interpolateAt(0.5)
    val start1 =
        onsetInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
    val pathEffect = PathEffect.dashPathEffect(
        floatArrayOf(20f, 30f)
    )
    drawLine(
        start = Offset(x = 0f, y = canvasHeight),
        end = Offset(x = start1, y = canvasHeight),
        color = colorTimeLine,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = if (onsetInterpol == null) pathEffect else null
    )
    val diff1 =
        comeupInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
    val start2 = start1 + diff1
    drawLine(
        start = Offset(x = start1, y = canvasHeight),
        end = Offset(x = start2, y = 0f),
        color = colorTimeLine,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = if (comeupInterpol == null) pathEffect else null
    )
    if (onset != null) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                onset.text,
                0f,
                canvasHeight - 11f,
                textPaintAlignLeft
            )
        }
    }
    if (comeup != null) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                comeup.text,
                (start1 + start2) / 2 + 15f,
                canvasHeight / 2,
                textPaintAlignLeft
            )
        }
    }
    val diff2 =
        peakInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
    val start3 = start2 + diff2
    drawLine(
        start = Offset(x = start2, y = 0f),
        end = Offset(x = start3, y = 0f),
        color = colorTimeLine,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = if (peakInterpol == null) pathEffect else null
    )
    if (peak != null) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                peak.text,
                (start2 + start3) / 2,
                35f,
                textPaintAlignCenter
            )
        }
    }
    val diff3 =
        offsetInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
    val start4 = start3 + diff3
    drawLine(
        start = Offset(x = start3, y = 0f),
        end = Offset(x = start4, y = canvasHeight),
        color = colorTimeLine,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = if (offsetInterpol == null) pathEffect else null
    )
    if (offset != null) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                offset.text,
                (start3 + start4) / 2 + 15f,
                canvasHeight / 2,
                textPaintAlignLeft
            )
        }
    }
}

fun DrawScope.drawFillPathForDurationRangesInTransparentColor(
    onset: DurationRange?,
    comeup: DurationRange?,
    peak: DurationRange?,
    offset: DurationRange?,
    pixelsPerSec: Float,
    dottedLineWidths: Float,
    canvasHeightOuter: Float,
    colorTransparent: Color
) {
    val path = Path().apply {
        // path over top
        val onsetStartMinX = onset?.min?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val comeupEndMinX =
            onsetStartMinX + (comeup?.min?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths)
        val maxOnset = onset?.max?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val maxComeup = comeup?.max?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val maxPeak = peak?.max?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val peakEndMaxX = maxOnset.plus(maxComeup).plus(maxPeak)
        val maxOffset = offset?.max?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val offsetEndMaxX = peakEndMaxX + maxOffset
        moveTo(onsetStartMinX, canvasHeightOuter)
        lineTo(x = comeupEndMinX, y = 0f)
        lineTo(x = peakEndMaxX, y = 0f)
        lineTo(x = offsetEndMaxX, y = canvasHeightOuter)
        // path bottom back
        val onsetStartMaxX = onset?.max?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val comeupEndMaxX = onsetStartMaxX + maxComeup
        val minOnset = onset?.min?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val minComeup = comeup?.min?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val minPeak = peak?.min?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val peakEndMinX = minOnset.plus(minComeup).plus(minPeak)
        val minOffset = offset?.min?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
        val offsetEndMinX = peakEndMinX + minOffset
        lineTo(x = offsetEndMinX, y = canvasHeightOuter)
        lineTo(x = peakEndMinX, y = 0f)
        lineTo(x = comeupEndMaxX, y = 0f)
        lineTo(x = onsetStartMaxX, y = canvasHeightOuter)
        close()
    }
    drawPath(
        path = path,
        color = colorTransparent
    )
}
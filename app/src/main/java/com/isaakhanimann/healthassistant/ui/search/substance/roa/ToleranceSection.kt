package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.Tolerance

@Preview(showBackground = true)
@Composable
fun ToleranceSectionPreview() {
    ToleranceSection(
        tolerance = Tolerance(
            full = "with prolonged use",
            half = "two weeks",
            zero = "1 month"
        ),
        crossTolerances = listOf(
            "dopamine",
            "stimulant"
        ),
        titleStyle = MaterialTheme.typography.subtitle2
    )
}

@Composable
fun ToleranceSection(
    tolerance: Tolerance?,
    crossTolerances: List<String>,
    titleStyle: TextStyle
) {
    if (tolerance != null || crossTolerances.isNotEmpty()) {
        Column {
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Tolerance", style = titleStyle)
            if (tolerance != null) {
                val labelWidth = 40.dp
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (tolerance.full != null) {
                        Text(
                            text = "full",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = tolerance.full)
                    }
                }
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (tolerance.half != null) {
                        Text(
                            text = "half",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = tolerance.half)
                    }
                }
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (tolerance.zero != null) {
                        Text(
                            text = "zero",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = tolerance.zero)
                    }
                }
                Text(text = "zero is the time until tolerance is like the first time", style = MaterialTheme.typography.caption)
            }
            if (crossTolerances.isNotEmpty()) {
                val names = crossTolerances.map { it }.distinct()
                    .joinToString(separator = ", ")
                Text(text = "Cross tolerance with $names")
            }
        }

    }

}
package com.example.healthassistant.ui.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.experiences.entities.Experience
import java.util.*

class SampleExperienceProvider : PreviewParameterProvider<Experience> {
    override val values: Sequence<Experience> = sequenceOf(
        Experience(title = "Day at Lake Geneva", creationDate = Date(), text = "Some notes")
    )
}
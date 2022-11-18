/*
 * Copyright (c) 2022. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.substance

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.substances.classes.Interactions

class InteractionsPreviewProvider : PreviewParameterProvider<Interactions> {
    override val values: Sequence<Interactions> = sequenceOf(
        Interactions(
            dangerous = listOf(
                "Alcohol",
                "AMT",
                "Cocaine"
            ),
            unsafe = listOf(
                "Tramadol",
                "MAOI",
                "Dissociatives"
            ),
            uncertain = listOf(
                "MDMA",
                "Stimulants",
                "Dextromethorphan"
            ),
        )
    )
}
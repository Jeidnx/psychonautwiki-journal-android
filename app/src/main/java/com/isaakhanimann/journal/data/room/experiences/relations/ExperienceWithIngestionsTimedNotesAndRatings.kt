/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import java.time.Instant

data class ExperienceWithIngestionsTimedNotesAndRatings(
    @Embedded val experience: Experience,
    @Relation(
        entity = Ingestion::class,
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ingestions: List<Ingestion>,
    @Relation(
        entity = TimedNote::class,
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val timedNotes: List<TimedNote>,
    @Relation(
        entity = ShulginRating::class,
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ratings: List<ShulginRating>
) {
    val sortInstant: Instant get() = ingestions.firstOrNull()?.time ?: experience.creationDate
}
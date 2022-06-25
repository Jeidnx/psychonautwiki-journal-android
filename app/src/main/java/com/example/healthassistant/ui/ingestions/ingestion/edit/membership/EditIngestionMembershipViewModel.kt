package com.example.healthassistant.ui.ingestions.ingestion.edit.membership

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.ui.main.routers.INGESTION_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditIngestionMembershipViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var ingestion: Ingestion? = null
    var selectedExperienceId: Int? by mutableStateOf(null)

    private var _experiences = MutableStateFlow<List<Experience>>(emptyList())
    val experiences = _experiences.asStateFlow()

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            ingestion = experienceRepo.getIngestionFlow(id = id).first()!!
            selectedExperienceId = ingestion?.experienceId

            experienceRepo.getExperiencesFlow()
                .collect {
                    _experiences.value = it
                }
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            ingestion!!.experienceId = selectedExperienceId
            experienceRepo.updateIngestion(ingestion!!)
        }
    }

}
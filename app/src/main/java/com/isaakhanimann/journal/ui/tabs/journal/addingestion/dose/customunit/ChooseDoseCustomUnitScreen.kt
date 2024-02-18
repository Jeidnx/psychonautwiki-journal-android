/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customunit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun ChooseDoseCustomUnitScreen(
    navigateToChooseTimeAndMaybeColor: (
        substanceName: String,
        administrationRoute: AdministrationRoute,
        units: String?,
        isEstimate: Boolean,
        dose: Double?,
        estimatedDoseVariance: Double?,
        customUnitId: Int?
    ) -> Unit,
    viewModel: ChooseDoseCustomUnitViewModel = hiltViewModel()
) {
    viewModel.customUnit?.let { customUnitUnwrapped ->
        ChooseDoseCustomUnitScreen(
            customUnit = customUnitUnwrapped,
            roaDose = viewModel.roaDose,
            doseText = viewModel.doseText,
            doseRemark = viewModel.doseRemark,
            onChangeDoseText = viewModel::onDoseTextChange,
            estimatedDoseVarianceText = viewModel.estimatedDoseVarianceText,
            onChangeEstimatedDoseVarianceText = viewModel::onEstimatedDoseVarianceChange,
            isValidDose = viewModel.isValidDose,
            isEstimate = viewModel.isEstimate,
            onChangeIsEstimate = {
                viewModel.isEstimate = it
            },
            navigateToNext = {
                navigateToChooseTimeAndMaybeColor(
                    customUnitUnwrapped.substanceName,
                    customUnitUnwrapped.administrationRoute,
                    customUnitUnwrapped.unit,
                    viewModel.isEstimate,
                    viewModel.dose,
                    viewModel.estimatedDoseVariance,
                    customUnitUnwrapped.id
                )
            },
            useUnknownDoseAndNavigate = {
                navigateToChooseTimeAndMaybeColor(
                    customUnitUnwrapped.substanceName,
                    customUnitUnwrapped.administrationRoute,
                    customUnitUnwrapped.unit,
                    false,
                    null,
                    null,
                    customUnitUnwrapped.id
                )
            },
            currentDoseClass = viewModel.currentDoseClass,
            customUnitCalculationText = viewModel.customUnitCalculationText,
        )
    }
}

@Preview
@Composable
fun ChooseDoseCustomUnitScreenPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    ChooseDoseCustomUnitScreen(
        customUnit = CustomUnit(
            substanceName = "Example Substance",
            administrationRoute = AdministrationRoute.ORAL,
            dose = 10.0,
            estimatedDoseVariance = null,
            isEstimate = false,
            isArchived = false,
            originalUnit = "mg",
            name = "Big Spoon",
            unit = "spoon",
            note = "Note about custom unit dose"
        ),
        roaDose = roaDose,
        doseText = "5",
        onChangeDoseText = {},
        estimatedDoseVarianceText = "",
        onChangeEstimatedDoseVarianceText = {},
        doseRemark = "This is a dose remark",
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
        useUnknownDoseAndNavigate = {},
        currentDoseClass = DoseClass.THRESHOLD,
        customUnitCalculationText = "2 pills x 20 mg = 40 mg",
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDoseCustomUnitScreen(
    customUnit: CustomUnit,
    roaDose: RoaDose?,
    doseRemark: String?,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    estimatedDoseVarianceText: String,
    onChangeEstimatedDoseVarianceText: (String) -> Unit,
    isValidDose: Boolean,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    navigateToNext: () -> Unit,
    useUnknownDoseAndNavigate: () -> Unit,
    currentDoseClass: DoseClass?,
    customUnitCalculationText: String?,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${customUnit.substanceName} (${customUnit.name})") },
                actions = {
                    IconButton(onClick = useUnknownDoseAndNavigate) {
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = "Log Unknown Dose"
                        )
                    }
                })
        },
        floatingActionButton = {
            if (isValidDose) {
                ExtendedFloatingActionButton(
                    onClick = navigateToNext,
                    icon = {
                        Icon(
                            Icons.Filled.NavigateNext,
                            contentDescription = "Next"
                        )
                    },
                    text = { Text("Next") },
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            LinearProgressIndicator(progress = 0.67f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            ElevatedCard(
                modifier = Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    if (!doseRemark.isNullOrBlank()) {
                        Text(text = doseRemark)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (roaDose != null) {
                        RoaDoseView(roaDose = roaDose)
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = 4.dp
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    if (roaDose != null) {
                        CustomUnitRoaDoseView(roaDose, customUnit)
                    }
                    AnimatedVisibility(visible = currentDoseClass != null && customUnitCalculationText != null) {
                        if (currentDoseClass != null && customUnitCalculationText != null) {
                            val doseColor = currentDoseClass.getComposeColor(isSystemInDarkTheme())
                            Text(
                                text = customUnitCalculationText,
                                color = doseColor,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    val focusManager = LocalFocusManager.current
                    val focusRequester = remember { FocusRequester() }
                    val textStyle = MaterialTheme.typography.titleMedium
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                    OutlinedTextField(
                        value = doseText,
                        onValueChange = onChangeDoseText,
                        textStyle = textStyle,
                        label = { Text("Dose", style = textStyle) },
                        isError = !isValidDose,
                        trailingIcon = {
                            Text(
                                text = customUnit.unit,
                                style = textStyle,
                                modifier = Modifier.padding(horizontal = horizontalPadding)
                            )
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onChangeIsEstimate(isEstimate.not()) }
                    ) {
                        Text("Is Estimate", style = MaterialTheme.typography.titleMedium)
                        Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                    }
                    AnimatedVisibility(visible = isEstimate) {
                        OutlinedTextField(
                            value = estimatedDoseVarianceText,
                            onValueChange = onChangeEstimatedDoseVarianceText,
                            textStyle = textStyle,
                            label = { Text("Estimated variance", style = textStyle) },
                            trailingIcon = {
                                Text(
                                    text = customUnit.unit,
                                    style = textStyle,
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            },
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
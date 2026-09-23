package mb28.crysongs.ui.popups

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import mb28.crysongs.core.Settings
import mb28.crysongs.ui.other.EdgeLightingEffect
import mb28.crysongs.ui.other.SettingSwitch

@Composable
fun FlagsPopup(onDismissRequired: () -> Unit) {
    AlertDialog(
        { onDismissRequired() },
        { },
        title = {
            Text("Flags")
        },
        text = {
            LazyColumn {
                val count = 4
                item {
                    Text("This features are experimental and might not work as expected.")
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    SettingSwitch(
                        Settings.gradientColoring,
                        "Gradient coloring", 0, count
                    ) { Settings.gradientColoring = it; Settings.save() }
                }
                item {
                    SettingSwitch(
                        Settings.edgeLighting,
                        "Edge lighting effect", 1, count,
                        Settings.waveformDataCapture
                    ) { Settings.edgeLighting = it; Settings.save() }
                }
                item {
                    SettingSwitch(
                        Settings.coverParallax,
                        "Cover parallax", 2, count,
                        Settings.waveformDataCapture
                    ) { Settings.coverParallax = it; Settings.save() }
                }
                item {
                    SettingSwitch(
                        Settings.windEffect,
                        "Wind effect", 3, count,
                        Settings.waveformDataCapture
                    ) { Settings.windEffect = it; Settings.save() }
                }
                if (Settings.waveformDataCapture && Settings.edgeLighting) {
                    item {
                        Box(Modifier.height(100.dp).padding(vertical = 10.dp)) {
                            EdgeLightingEffect(LocalActivity.current!!, true)
                        }
                    }
                    item {
                        val state = rememberSliderState(Settings.edgeLightingSaturation)
                        Text("Edge lighting saturation: ${(Settings.edgeLightingSaturation * 100f).fastRoundToInt() / 100f}")
                        Slider(
                            state,
                            onValueChange = {
                                state.value = it
                                Settings.edgeLightingSaturation = it
                            },
                            onValueChangeFinished = {
                                Settings.save()
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    item {
                        val state = rememberSliderState(Settings.edgeLightingLightness)
                        Text("Edge lighting lightness: ${(Settings.edgeLightingLightness * 100f).fastRoundToInt() / 100f}")
                        Slider(
                            state,
                            onValueChange = {
                                state.value = it
                                Settings.edgeLightingLightness = it
                            },
                            onValueChangeFinished = {
                                Settings.save()
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    item {
                        val state = rememberSliderState(Settings.edgeLightingHue1, trackRange = 0f..360f)
                        Text("Edge lighting hue #1: ${Settings.edgeLightingHue1.fastRoundToInt()}")
                        Slider(
                            state,
                            onValueChange = {
                                state.value = it
                                Settings.edgeLightingHue1 = it
                            },
                            onValueChangeFinished = {
                                Settings.save()
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    item {
                        val state = rememberSliderState(Settings.edgeLightingHue2, trackRange = 0f..360f)
                        Text("Edge lighting hue #2: ${Settings.edgeLightingHue2.fastRoundToInt()}")
                        Slider(
                            state,
                            onValueChange = {
                                state.value = it
                                Settings.edgeLightingHue2 = it
                            },
                            onValueChangeFinished = {
                                Settings.save()
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    item {
                        val state = rememberSliderState(Settings.edgeLightingHue3, trackRange = 0f..360f)
                        Text("Edge lighting hue #3: ${Settings.edgeLightingHue3.fastRoundToInt()}")
                        Slider(
                            state,
                            onValueChange = {
                                state.value = it
                                Settings.edgeLightingHue3 = it
                            },
                            onValueChangeFinished = {
                                Settings.save()
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    item {
                        val state = rememberSliderState(Settings.edgeLightingHue4, trackRange = 0f..360f)
                        Text("Edge lighting hue #4: ${Settings.edgeLightingHue4.fastRoundToInt()}")
                        Slider(
                            state,
                            onValueChange = {
                                state.value = it
                                Settings.edgeLightingHue4 = it
                            },
                            onValueChangeFinished = {
                                Settings.save()
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    )
}

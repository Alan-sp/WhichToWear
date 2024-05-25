package com.android.whichtowear.ui.survey.ColorPicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Preview
@Composable
@ExperimentalComposeUiApi
fun HsvColorPickerColoredSelectorScreen() {
    val controller = rememberColorPickerController()
    var hexCode by remember { mutableStateOf("") }
    var textColor by remember { mutableStateOf(Color.Transparent) }

    Column(modifier = Modifier.semantics { testTagsAsResourceId = true }) {
        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.weight(3f)) {
            listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Gray)
                .forEach { color ->
                    Surface(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        color = color,
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            controller.selectByColor(color, true)
                        },
                    ) {}
                }
        }

        Box(modifier = Modifier.weight(8f)) {
            HsvColorPicker(
                modifier = Modifier
                    .padding(10.dp),
                controller = controller,
                drawOnPosSelected = {
                    drawColorIndicator(
                        controller.selectedPoint.value,
                        controller.selectedColor.value,
                    )
                },
                onColorChanged = { colorEnvelope ->
                    hexCode = colorEnvelope.hexCode
                    textColor = colorEnvelope.color
                },
                initialColor = Color.Red,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AlphaSlider(
            modifier = Modifier
                .testTag("HSV_AlphaSlider")
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp)
                .align(Alignment.CenterHorizontally),
            controller = controller,
        )

        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp)
                .align(Alignment.CenterHorizontally),
            controller = controller,
        )

        Text(
            text = "#$hexCode",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        AlphaTile(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .align(Alignment.CenterHorizontally),
            controller = controller,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}
package com.example.pruebasubicacion.presentation.ui.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
class plantillasTexto(){
    @Composable
    fun TextoMalAire(
        text: String = "Mala calidad del aire",
        modifier: Modifier = Modifier,
        style: TextStyle = MaterialTheme.typography.titleLarge,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontSize: TextUnit = TextUnit.Unspecified,
        maxLines: Int = 1,
        overflow: TextOverflow = TextOverflow.Ellipsis
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = style.copy(fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize),
            color = color,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow
        )

    }
    @Composable
    fun TextoBuenAire(
        text: String = "Buena calidad del aire",
        modifier: Modifier = Modifier,
        style: TextStyle = MaterialTheme.typography.titleLarge,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
        fontSize: TextUnit = TextUnit.Unspecified,
        maxLines: Int = 1,
        overflow: TextOverflow = TextOverflow.Ellipsis
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = style.copy(fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize),
            color = color,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow
        )

    }
}



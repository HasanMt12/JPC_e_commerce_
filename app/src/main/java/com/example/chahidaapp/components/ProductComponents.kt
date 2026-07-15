package com.example.chahidaapp.components

import android.widget.TextView
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage

/**
 * Reusable network image loader component using Coil.
 */
@Composable
fun ProductImage(
    imageUrl: String,
    modifier: Modifier = Modifier.size(120.dp) // Defaulter sathe override custom size options
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Organic Product Image",
        modifier = modifier
    )
}

/**
 * Reusable custom HTML Parser for dynamic descriptions.
 */
@Composable
fun HtmlText(
    htmlDescription: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context)
        },
        update = { textView ->

            textView.text = HtmlCompat.fromHtml(htmlDescription, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}
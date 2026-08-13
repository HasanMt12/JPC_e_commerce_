package com.example.chahidaapp.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun FlyingCartOverlay(
    state: FlyingCartState,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        // Render animated flying items over top of screen
        state.activeParticles.forEach { particle ->
            val progress = particle.progress.value

            // Parabolic curve equation (arc upwards before descending)
            val currentX = particle.startOffset.x + (particle.endOffset.x - particle.startOffset.x) * progress
            val arcHeight = 150f // Peak height of flying arc in pixels
            val parabolaY = -4 * arcHeight * progress * (1 - progress)
            val currentY = particle.startOffset.y + (particle.endOffset.y - particle.startOffset.y) * progress + parabolaY

            // Scale down from 1.0 to 0.4 and fade near end
            val scale = 1f - (progress * 0.6f)
            val alpha = if (progress > 0.8f) (1f - progress) / 0.2f else 1f

            Box(
                modifier = Modifier
                    .offset { IntOffset(currentX.roundToInt() - 20, currentY.roundToInt() - 20) }
                    .size(36.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .background(Color(0xFF2E7D32), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
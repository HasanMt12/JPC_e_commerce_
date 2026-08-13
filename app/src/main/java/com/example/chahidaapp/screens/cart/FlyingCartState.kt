package com.example.chahidaapp.screens.cart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class FlyingParticle(
    val id: Long = System.currentTimeMillis(),
    val startOffset: Offset,
    val endOffset: Offset,
    val progress: Animatable<Float, *> = Animatable(0f)
)

class FlyingCartState(private val scope: CoroutineScope) {
    var cartTargetCoordinates: LayoutCoordinates? = null
    val activeParticles = mutableStateListOf<FlyingParticle>()

    fun triggerFly(buttonCoordinates: LayoutCoordinates) {
        val target = cartTargetCoordinates ?: return

        // Calculate center of clicked button
        val buttonSize = buttonCoordinates.size
        val buttonPosition = buttonCoordinates.positionInRoot()
        val startX = buttonPosition.x + (buttonSize.width / 2f)
        val startY = buttonPosition.y + (buttonSize.height / 2f)

        // Calculate center of bottom bar cart icon
        val targetSize = target.size
        val targetPosition = target.positionInRoot()
        val endX = targetPosition.x + (targetSize.width / 2f)
        val endY = targetPosition.y + (targetSize.height / 2f)

        val particle = FlyingParticle(
            startOffset = Offset(startX, startY),
            endOffset = Offset(endX, endY)
        )

        activeParticles.add(particle)

        scope.launch {
            particle.progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
            activeParticles.remove(particle)
        }
    }
}

@Composable
fun rememberFlyingCartState(): FlyingCartState {
    val scope = rememberCoroutineScope()
    return remember(scope) { FlyingCartState(scope) }
}
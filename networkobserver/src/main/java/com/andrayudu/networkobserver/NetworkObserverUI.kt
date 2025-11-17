package com.andrayudu.networkobserver


import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope

object NetworkObserverUI {

    fun attach(
        parent: ViewGroup,
        scope: CoroutineScope,
        mode: OfflineViewMode,
        onRetry: (() -> Unit)? = null
    ): OfflineController {

        val layout = when (mode) {
            OfflineViewMode.BANNER -> R.layout.offline_banner
            OfflineViewMode.FULLSCREEN -> R.layout.offline_fullscreen
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        parent.addView(view)

        return OfflineController(scope, view, mode, onRetry).apply {
            start(parent.context)
        }
    }
}

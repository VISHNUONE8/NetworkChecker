package com.andrayudu.networkobserver


import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch




class OfflineController(
    private val scope: CoroutineScope,
    private val offlineView: View,
    private val mode: OfflineViewMode,
    private val onRetry: (() -> Unit)? = null
) {

    private var job: Job? = null
    private var retryJob: Job? = null

    fun start(context: Context) {
        val retryBtn = offlineView.findViewById<Button?>(R.id.offlineRetryBtn)
        val retryLoader = offlineView.findViewById<ProgressBar?>(R.id.offlineRetryLoader)

        // ---------------------------------------------------------
        // FIX: Immediately show offline screen if internet is off
        // ---------------------------------------------------------
        if (!NetworkUtils.isInternetAvailable(context)) {
            offlineView.visibility = View.VISIBLE
        } else {
            offlineView.visibility = View.GONE
        }
        // ---------------------------------------------------------

        retryBtn?.setOnClickListener {
            retryLoader?.visibility = View.VISIBLE
            retryBtn.visibility = View.INVISIBLE

            // trigger callback to reload API
            onRetry?.invoke()

            retryJob?.cancel()
            retryJob = scope.launch {
                repeat(5) { // retry for ~5 sec
                    delay(1000)

                    if (NetworkUtils.isInternetAvailable(context)) {
                        retryLoader?.visibility = View.GONE
                        retryBtn.visibility = View.VISIBLE
                        offlineView.visibility = View.GONE
                        return@launch
                    }
                }

                // retry failed
                retryLoader?.visibility = View.GONE
                retryBtn.visibility = View.VISIBLE
            }
        }

        job = scope.launch {
            NetworkObserver.observe(context).collectLatest { status ->
                when (status) {
                    NetworkStatus.AVAILABLE -> offlineView.visibility = View.GONE
                    NetworkStatus.LOST -> offlineView.visibility = View.VISIBLE
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
        retryJob?.cancel()
    }
}


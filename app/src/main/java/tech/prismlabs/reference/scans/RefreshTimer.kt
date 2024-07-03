package tech.prismlabs.reference.scans

import android.os.CountDownTimer
import android.util.Log

class RefreshTimer(refreshTime: Long) {
    companion object {
        @Volatile private var shared: RefreshTimer? = null

        fun getSharedTimer(): RefreshTimer {
            return shared ?: synchronized(this) { // synchronized to avoid concurrency problem
                shared ?: RefreshTimer(
                    refreshTime = 15
                ).also { shared = it }
            }
        }
    }

    private var counter: CountDownTimer? = null
    private var isRunning: Boolean = false

    var onStart: (() -> Unit)? = null
    var onFired: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    init {
        if (counter != null) {
            counter!!.cancel()
        }
        counter = object : CountDownTimer(refreshTime * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remaining = millisUntilFinished / 1000
                Log.i("RefreshTimer", "Timer Refresh: $remaining")
                if (remaining == 1L) {
                    onFired?.let { it() }
                }
            }

            override fun onFinish() {
                start()
            }
        }
    }

    fun start() {
        if (isRunning) return
        if (counter == null) return
        counter!!.start()
        isRunning = true
        onStart?.let { it() }
        Log.i("RefreshTimer", "Timer Refresh Started")
    }

    fun stop() {
        if (counter == null) return
        counter!!.cancel()
        isRunning = false
        onCancel?.let { it() }
        Log.i("RefreshTimer", "Timer Refresh Stopped")
    }
}

package io.github.lee0701.morsekeyboard

import android.os.Handler
import android.view.MotionEvent
import android.view.View

class KeyTouchListener(val config: KeyTouchListener.Config, val listener: Listener): View.OnTouchListener {

    private val handler = Handler()
    private var state = State.IDLE

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.isPressed = true
                v.background.state = intArrayOf(android.R.attr.state_pressed)
                handler.removeCallbacksAndMessages(null)
                state = State.DOT
                handler.postDelayed({
                    state = State.DASH
                }, config.dashLength)
            }
            MotionEvent.ACTION_UP -> {
                v.isPressed = false
                v.background.state = intArrayOf(android.R.attr.state_enabled)
                handler.removeCallbacksAndMessages(null)
                when(state) {
                    State.DOT -> listener.onDot()
                    State.DASH -> listener.onDash()
                }
                v.performClick()

                handler.postDelayed({
                    state = State.SHORT_BREAK
                    listener.onShortBreak()
                }, config.shortBreakLength)

                handler.postDelayed({
                    state = State.LONG_BREAK
                    listener.onLongBreak()
                }, config.longBreakLength)
            }
        }
        return true
    }

    private enum class State {
        IDLE, DOT, DASH, SHORT_BREAK, LONG_BREAK
    }

    data class Config(
        val dashLength: Long,
        val shortBreakLength: Long,
        val longBreakLength: Long
    )

    interface Listener {
        fun onDot()
        fun onDash()
        fun onShortBreak()
        fun onLongBreak()
    }

}

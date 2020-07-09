package io.github.lee0701.morsekeyboard.decoder

import java.util.*

class TableMorseDecoder(listener: Listener, private val table: Map<List<Boolean>, String>, val capital: Boolean = true): MorseDecoder(listener) {

    private var state = State.NO_INPUT
    private val code = mutableListOf<Boolean>()
    private val label get() = code.map { c -> if(c) "-" else "·" }.joinToString(" ")

    override fun onDot() {
        code += false
        listener.onLabel(label)
    }

    override fun onDash() {
        code += true
        listener.onLabel(label)
    }

    override fun onShortBreak() {
        val text = table[code]
        if(text == null) listener.onMiss()
        else listener.onText(if(capital) text.toUpperCase(Locale.ROOT) else text.toLowerCase(Locale.ROOT))
        state = if(text == null) State.NO_INPUT else State.INPUT
        code.clear()
        listener.onLabel(label)
    }

    override fun onLongBreak() {
        code.clear()
        if(state == State.INPUT) listener.onText(" ")
        listener.onLabel(label)
    }

    private enum class State {
        NO_INPUT, INPUT
    }

}

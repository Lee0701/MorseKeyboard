package io.github.lee0701.morsekeyboard.decoder

import io.github.lee0701.morsekeyboard.KeyTouchListener

abstract class MorseDecoder(val listener: Listener): KeyTouchListener.Listener {

    interface Listener {
        fun onLabel(label: String)
        fun onText(text: String)
        fun onMiss()
    }

}

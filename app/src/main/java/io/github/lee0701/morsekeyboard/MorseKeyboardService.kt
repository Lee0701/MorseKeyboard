package io.github.lee0701.morsekeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Toast
import io.github.lee0701.morsekeyboard.decoder.MorseDecoder
import io.github.lee0701.morsekeyboard.decoder.TableMorseDecoder
import io.github.lee0701.morsekeyboard.decoder.table.MorseTable
import kotlinx.android.synthetic.main.morse_keyboard_view.view.*

class MorseKeyboardService: InputMethodService() {

    val config = KeyTouchListener.Config(250L, 500L, 1000L)

    override fun onCreateInputView(): View {
        val view = View.inflate(this, R.layout.morse_keyboard_view, null)

        val listener = object: MorseDecoder.Listener {
            override fun onLabel(label: String) {
                view.key.text = label
            }

            override fun onText(text: String) {
                val inputConnection = currentInputConnection ?: return
                if(text == "\b") {
                    if(inputConnection.getTextBeforeCursor(1, 0) == " ") inputConnection.deleteSurroundingText(1, 0)
                    val text = inputConnection.getTextBeforeCursor(30, 0)
                    val length = text.length - text.lastIndexOf(' ')
                    inputConnection.deleteSurroundingText(length, 0)
                } else if(text == " ") {
                    if(inputConnection.getTextBeforeCursor(1, 0) != "") inputConnection.commitText(text, 1)
                } else {
                    inputConnection.commitText(text, 1)
                }
            }

            override fun onMiss() {
                Toast.makeText(this@MorseKeyboardService, "Miss", Toast.LENGTH_SHORT).show()
            }
        }
        val table = MorseTable.ERROR + MorseTable.ALPHABET + MorseTable.NUMBER
        val decoder = TableMorseDecoder(listener, table)

        view.key.setOnTouchListener(KeyTouchListener(config, decoder))
        return view
    }

    override fun onEvaluateFullscreenMode(): Boolean {
        return false
    }
}

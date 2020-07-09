package io.github.lee0701.morsekeyboard

import android.content.Context
import android.content.SharedPreferences
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import io.github.lee0701.morsekeyboard.decoder.MorseDecoder
import io.github.lee0701.morsekeyboard.decoder.TableMorseDecoder
import io.github.lee0701.morsekeyboard.decoder.table.MorseTable
import kotlinx.android.synthetic.main.morse_keyboard_view.view.*

class MorseKeyboardService: InputMethodService() {

    lateinit var preference: SharedPreferences
    lateinit var config: KeyTouchListener.Config

    lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, true)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreateInputView(): View {
        preference = PreferenceManager.getDefaultSharedPreferences(this)
        config = KeyTouchListener.Config(
            preference.getInt("dash_length", 200).toLong(),
            preference.getInt("short_break_length", 500).toLong(),
            preference.getInt("long_break_length", 2000).toLong()
        )
        val vibrateOnMiss = preference.getBoolean("vibrate_on_miss", true)

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
                if(vibrateOnMiss) vibrate(200)
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

    private fun vibrate(length: Long) {
        if(Build.VERSION.SDK_INT >= 26) vibrator.vibrate(VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE))
        else vibrator.vibrate(length)
    }

}

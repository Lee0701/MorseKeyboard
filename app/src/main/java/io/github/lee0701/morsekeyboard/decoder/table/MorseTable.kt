package io.github.lee0701.morsekeyboard.decoder.table

object MorseTable {

    val ALPHABET = mapOf<List<Boolean>, String>(
        makeCode(".-") to "A",
        makeCode("-...") to "B",
        makeCode("-.-.") to "C",
        makeCode("-..") to "D",
        makeCode(".") to "E",
        makeCode("..-.") to "F",
        makeCode("--.") to "G",
        makeCode("....") to "H",
        makeCode("..") to "I",
        makeCode(".---") to "J",
        makeCode("-.-") to "K",
        makeCode(".-..") to "L",
        makeCode("--") to "M",
        makeCode("-.") to "N",
        makeCode("---") to "O",
        makeCode(".--.") to "P",
        makeCode("--.-") to "Q",
        makeCode(".-.") to "R",
        makeCode("...") to "S",
        makeCode("-") to "T",
        makeCode("..-") to "U",
        makeCode("...-") to "V",
        makeCode(".--") to "W",
        makeCode("-..-") to "X",
        makeCode("-.--") to "Y",
        makeCode("--..") to "Z"
    )

    val NUMBER = mapOf<List<Boolean>, String>(
        makeCode(".----") to "1",
        makeCode("..---") to "2",
        makeCode("...--") to "3",
        makeCode("....-") to "4",
        makeCode(".....") to "5",
        makeCode("-....") to "6",
        makeCode("--...") to "7",
        makeCode("---..") to "8",
        makeCode("----.") to "9",
        makeCode("-----") to "0"
    )

    val PROSIGN = mapOf<List<Boolean>, String>(
        makeCode("..-..-") to "\n",
        makeCode("........") to "\b",
        makeCode("..-..-.") to "\u0000"
    )

    fun makeCode(code: String): List<Boolean> {
        val dot = '.'
        val dash = '-'
        return code.map { c -> if(c == dot) false else if(c == dash) true else null }
            .filterNotNull()
    }

}

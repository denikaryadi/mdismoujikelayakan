package com.komida.co.id.mdisujikelayakan
import java.security.SecureRandom
import java.util.*

class RandomStringGenerator {
    private val CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz"
    private val CHAR_UPPER = CHAR_LOWER.uppercase(Locale.getDefault())
    private val NUMBER = "0123456789"
    private val DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER
    private val random = SecureRandom()

    fun generateRandomString(length: Int): String {
        if (length < 1) {
            throw IllegalArgumentException("Length must be a positive integer.")
        }

        val randomStringBuilder = StringBuilder(length)
        for (i in 0 until length) {
            val randomIndex = random.nextInt(DATA_FOR_RANDOM_STRING.length)
            val randomChar = DATA_FOR_RANDOM_STRING[randomIndex]
            randomStringBuilder.append(randomChar)
        }
        return randomStringBuilder.toString()
    }
}

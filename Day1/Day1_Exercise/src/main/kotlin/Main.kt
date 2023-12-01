import java.io.File

/**
 * A class that parses a file and extracts numbers from each line.
 *
 * The numbers are formed by concatenating the first and last digit found in each line.
 * Word representations of numbers (e.g., "one", "two", "three", etc.) are also recognized and treated as their digit counterparts.
 *
 * @property numberWords A map that associates each word representation of a number with its digit counterpart.
 */
class FileParser {

    /**
     * Parses a file and extracts numbers from each line.
     *
     * The numbers are formed by concatenating the first and last digit found in each line.
     * Word representations of numbers (e.g., "one", "two", "three", etc.) are also recognized and treated as their digit counterparts.
     *
     * @param filePath The path to the file to be parsed.
     * @return A list of numbers extracted from the file.
     */

    fun parseFile() {

        val validNumbers = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )

        val numbers = File("input.txt").readLines()
            .map { line ->
                val firstDigit = line.firstOrNull { it.isDigit() }?.let { it.toString() to line.indexOf(it) }
                val firstWord = validNumbers.keys.mapNotNull { key ->
                    if (line.contains(key)) {
                        return@mapNotNull validNumbers[key]!!.toString() to line.indexOf(key)
                    }

                    null
                }.minByOrNull { it.second }

                val lastDigit = line.lastOrNull { it.isDigit() }?.let { it.toString() to line.lastIndexOf(it) }
                val lastWord = validNumbers.keys.mapNotNull { key ->
                    if (line.contains(key)) {
                        return@mapNotNull validNumbers[key]!!.toString() to line.lastIndexOf(key)
                    }

                    null
                }.maxByOrNull { it.second }

                val first = listOf(firstDigit, firstWord).minByOrNull { it?.second ?: Int.MAX_VALUE }!!.first
                val last = listOf(lastDigit, lastWord).maxByOrNull { it?.second ?: Int.MIN_VALUE }!!.first

                "$first$last".toInt()
            }

        val sum = numbers.sum()
        println(sum)
    }
}

fun main() {
    val parser = FileParser()
    parser.parseFile()
}
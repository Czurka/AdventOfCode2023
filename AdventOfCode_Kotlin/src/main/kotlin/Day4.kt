import java.io.File
import kotlin.math.pow


/**
 * Parses a line from the input data and extracts two lists of integers.
 *
 * The line is split into two parts at the ":" character. Each part is further split at the "|" character.
 * The resulting strings are trimmed, split into individual numbers, and converted to integers.
 *
 * @param line The line to be parsed.
 * @return A pair of lists of integers extracted from the line.
 */
fun parseLine(line: String): Pair<List<Int>, List<Int>> {
    val parts = line.split(":")
    val numbers = parts[1].split("|") // use a raw string
    val numbersBefore = numbers[0].trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    val numbersAfter = numbers[1].trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    return Pair(numbersBefore, numbersAfter)
}

/**
 * Calculates the sum of multipliers for each line in the input data.
 *
 * The multiplier for a line is 2^(n-1), where n is the number of matching numbers in the line.
 *
 * @param filePath The path to the file containing the input data.
 * @return The sum of multipliers for each line in the input data.
 */
fun part1(filePath: String): Int {
    val resultList = mutableListOf<Int>()
    File(filePath).forEachLine { line ->
        val (numbersBefore, numbersAfter) = parseLine(line)
        val matchingNumbers = numbersBefore.intersect(numbersAfter).size
        val multiplier = 2.0.pow(matchingNumbers - 1).toInt()
        resultList.add(multiplier)
    }
    return resultList.sum()
}

/**
 * Calculates the total number of scratchcards obtained from the input data.
 *
 * Each line in the input data represents a scratchcard. For each scratchcard, the function calculates the number of matching numbers and adds copies of the next scratchcards equal to the number of matches to a queue. It then processes each scratchcard in the queue in the same way, until the queue is empty. The total number of scratchcards is the number of scratchcards processed.
 *
 * @param filePath The path to the file containing the input data.
 * @return The total number of scratchcards obtained from the input data.
 */
fun part2(filePath: String): Int {
    val lines = File(filePath).readLines()
    val queue = ArrayDeque(lines.mapIndexed { index, line -> Pair(index, line) })
    var totalScratchcards = 0

    while (queue.isNotEmpty()) {
        val (index, line) = queue.removeFirst()
        totalScratchcards++

        var matchingNumbers: Int
        do {
            val (numbersBefore, numbersAfter) = parseLine(line)
            matchingNumbers = numbersBefore.intersect(numbersAfter.toSet()).size

            for (i in 1..matchingNumbers) {
                val nextIndex = index + i
                if (nextIndex < lines.size) {
                    queue.addFirst(Pair(nextIndex, lines[nextIndex]))
                }
            }
        } while (matchingNumbers > 0 && queue.first().first == index)
    }

    return totalScratchcards
}

fun main() {
    val input = "src/main/resources/Day4_input.txt"
    println(part1(input))
    println(part2(input))
}
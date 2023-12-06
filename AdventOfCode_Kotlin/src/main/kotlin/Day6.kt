import java.io.File

/**
 * Parses the given file and returns a pair of lists of integers.
 * The first list represents the time values and the second list represents the distance values.
 * The file should have two lines: the first line starts with "Time:" and the second line starts with "Distance:".
 * Each line should have a series of integers separated by spaces after the label.
 *
 * @param filePath the path of the file to parse
 * @return a pair of lists of integers
 */
fun parseFileForPart1(filePath: String): Pair<List<Int>, List<Int>> {
    val lines = File(filePath).readLines()

    val timeList = lines[0].split(Regex("\\s+")).drop(1).map { it.toInt() }
    val distanceList = lines[1].split(Regex("\\s+")).drop(1).map { it.toInt() }

    return timeList to distanceList
}

/**
 * Parses the given file and returns a pair of longs.
 * The first long represents the time value and the second long represents the distance value.
 * The file should have two lines: the first line starts with "Time:" and the second line starts with "Distance:".
 * Each line should have a series of integers without spaces after the label.
 *
 * @param filePath the path of the file to parse
 * @return a pair of longs
 */
fun parseFileForPart2(filePath: String): Pair<Long, Long> {
    val lines = File(filePath).readLines()

    val timeList = lines[0].split(":")[1].replace(" ", "").toLong()
    val distanceList = lines[1].split(":")[1].replace(" ", "").toLong()

    return timeList to distanceList
}

/**
 * Calculates the number of winning variants for a race.
 * A winning variant is a duration for which the button is held at the start of the race that results in a travel distance greater than the provided distance.
 *
 * @param time the duration of the race in milliseconds
 * @param distance the record distance for the race in millimeters
 * @return the number of winning variants
 */
fun calculateWinningVariants(time: Long, distance: Long): Long {
    var winningVariants: Long = 0
    for (buttonHoldDuration in 0 until time) {
        val travelTime = time - buttonHoldDuration
        val travelDistance = buttonHoldDuration * travelTime
        if (travelDistance > distance) {
            winningVariants++
        }
    }
    return winningVariants
}

fun main() {
    val (timeList1, distanceList1) = parseFileForPart1("src/main/resources/Day6_input.txt")

    val winningVariantsList1 = timeList1.zip(distanceList1).map { (time1, distance1) ->
        calculateWinningVariants(time1.toLong(), distance1.toLong())
    }

    val (timeList2, distanceList2) = parseFileForPart2("src/main/resources/Day6_input.txt")

    val winningVariantsList2 = calculateWinningVariants(timeList2, distanceList2)

    println("Part1: ${winningVariantsList1.reduce { acc, i ->  acc * i }}")
    println("Part2: ${winningVariantsList2}")
}
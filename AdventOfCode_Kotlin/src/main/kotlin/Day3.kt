import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.absoluteValue

/**
 * Represents a position in the input data.
 *
 * @property row The row of the position.
 * @property range The range of the position.
 */
sealed class Position(val row: Int, val range: IntRange)

/**
 * Represents a number in the input data.
 *
 * @property value The value of the number.
 * @property row The row of the number.
 * @property range The range of the number.
 */
class Number(val value: Int, row: Int, range: IntRange) : Position(row, range)

/**
 * Represents a character in the input data.
 *
 * @property value The value of the character.
 * @property row The row of the character.
 * @property range The range of the character.
 */
class Character(val value: String, row: Int, range: IntRange) : Position(row, range)

/**
 * Parses a line of the input data.
 *
 * @param line The line to be parsed.
 * @param rowId The row of the line.
 * @return A sequence of positions extracted from the line.
 */
fun parse(line: String, rowId: Int): Sequence<Position> = sequence {
    var buffer = ""
    fun emitNumber(idx: Int): Number? {
        if(buffer.isNotEmpty()) {
            val result = Number(buffer.toInt(),  rowId, idx - buffer.length + 1 .. idx)
            buffer = ""
            return result
        }
        return null
    }
    line.forEachIndexed { idx, c ->
        if('.' == c) {
            emitNumber(idx - 1)?.let { yield(it) }
        } else if(! c.isDigit()) {
            emitNumber(idx - 1)?.let { yield(it) }
            yield(Character(c.toString(), rowId, idx - 1 .. idx + 1))
        } else {
            buffer += c
        }
    }
    emitNumber(line.length - 1)?.let { yield(it) }
}

/**
 * Parses the input data.
 *
 * @param lines The lines to be parsed.
 * @return A sequence of positions extracted from the lines.
 */
fun parse(lines: List<String>): Sequence<Position> = lines.mapIndexed{idx, line -> parse(line, idx) }
    .asSequence().flatMap { it }

/**
 * Checks if two ranges overlap.
 *
 * @param r The other range.
 * @return True if the ranges overlap, false otherwise.
 */
fun IntRange.overlap(r: IntRange): Boolean {
    var a = this
    var b = r

    if(a.first > b.first) {
        a = r; b = this
    }
    return b.first <= a.last
}

/**
 * Checks if a number overlaps with a character.
 *
 * @param char The character.
 * @return True if the number overlaps with the character, false otherwise.
 */
fun Number.overlap(char: Character): Boolean {
    if((this.row - char.row).absoluteValue <= 1) {
        return this.range.overlap(char.range)
    }
    return false
}

/**
 * Gets the context of a row.
 *
 * @param rowId The row ID.
 * @return The context of the row.
 */
fun <T> Map<Int, List<T>>.getContext(rowId: Int) = (rowId - 1 .. rowId + 1)
    .map { this.getOrDefault(it, listOf()) }
    .flatMap { it.asSequence() }

/**
 * Solves part 1 of the problem.
 *
 * @param lines The input data.
 * @return The solution to part 1.
 */
fun part1(lines: List<String>): Int {
    val elements = parse(lines).toList()
    val numbers = elements.filterIsInstance<Number>()
    val chars = elements.filterIsInstance<Character>().groupBy { it.row }
    val toSum = numbers.filter { n -> chars.getContext(n.row).any { c -> n.overlap(c) } }
    return toSum.sumOf { it.value }
}

/**
 * Solves part 2 of the problem.
 *
 * @param lines The input data.
 * @return The solution to part 2.
 */
fun part2(lines: List<String>): Int {
    val elements = parse(lines).toList()
    val numbers = elements.filterIsInstance<Number>().groupBy { it.row }
    val gears = elements
        .filterIsInstance<Character>()
        .filter { it.value == "*" }

    val found = gears.map { g ->
        numbers.getContext(g.row).filter { it.overlap(g) }.toList()
    }.filter { it.size == 2 }
    return found.sumOf { it[0].value * it[1].value }
}

/**
 * Reads the input data from a file.
 *
 * @param name The name of the file.
 * @return The input data.
 */
fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()


/**
 * The main function.
 */
fun main() {
    val input = readInput("Day3_input")
    val part1Out = part1(input)
    val part2Out = part2(input)
    print("$part1Out \n")
    print("$part2Out \n")
}
import java.io.File

/**
 * Calculates the sum of game IDs for which a game is possible.
 *
 * A game is possible if the number of red, green, and blue balls required for each set in the game
 * does not exceed the available number of red, green, and blue balls.
 *
 * @param filePath The path to the file containing the game data.
 * @param redBalls The number of available red balls.
 * @param greenBalls The number of available green balls.
 * @param blueBalls The number of available blue balls.
 * @return The sum of game IDs for which a game is possible.
 */
fun sumOfPossibleGameIds(filePath: String, redBalls: Int, greenBalls: Int, blueBalls: Int): Int {
    var sumOfGameIds = 0
    File(filePath).forEachLine { line ->
        val parts = line.split(":")
        val gameId = parts[0].split(" ")[1].toInt()
        val gameSets = parts[1].split(";")
        var isGamePossible = true
        gameSets.forEach { set ->
            val balls = set.split(",")
            balls.forEach { ball ->
                val ballParts = ball.trim().split(" ")
                val count = ballParts[0].toInt()
                when (ballParts[1]) {
                    "red" -> if (count > redBalls) isGamePossible = false
                    "green" -> if (count > greenBalls) isGamePossible = false
                    "blue" -> if (count > blueBalls) isGamePossible = false
                }
            }
            if (!isGamePossible) return@forEach
        }
        if (isGamePossible) {
            sumOfGameIds += gameId
        }
    }
    return sumOfGameIds
}

/**
 * Calculates the sum of the powers of all games.
 *
 * The power of a game is the product of the maximum number of red, green, and blue balls required for any set in the game.
 *
 * @param filePath The path to the file containing the game data.
 * @return The sum of the powers of all games.
 */
fun sumOfGamePowers(filePath: String): Int {
    var sumOfPowers = 0
    File(filePath).forEachLine { line ->
        val gameSets = line.split(":")[1].split(";")
        var maxRed = Int.MIN_VALUE
        var maxGreen = Int.MIN_VALUE
        var maxBlue = Int.MIN_VALUE
        gameSets.forEach { set ->
            var red = 0
            var green = 0
            var blue = 0
            val balls = set.split(",")
            balls.forEach { ball ->
                val ballParts = ball.trim().split(" ")
                val count = ballParts[0].toInt()
                when (ballParts[1]) {
                    "red" -> red = count
                    "green" -> green = count
                    "blue" -> blue = count
                }
            }
            if (red > maxRed) maxRed = red
            if (green > maxGreen) maxGreen = green
            if (blue > maxBlue) maxBlue = blue
        }
        val power = maxRed * maxGreen * maxBlue
        sumOfPowers += power
    }
    return sumOfPowers
}

/**
 * The main function.
 *
 * Reads the game data from a file and prints the sum of possible game IDs and the sum of game powers.
 */
fun main() {
    val filePath = "src/main/resources/Day2_input.txt"
    val redBalls = 12
    val greenBalls = 13
    val blueBalls = 14
    val sumOfGameIds = sumOfPossibleGameIds(filePath, redBalls, greenBalls, blueBalls)
    println("Sum of possible game IDs: $sumOfGameIds")
    val sumOfPowers = sumOfGamePowers(filePath)
    println("Sum of game powers: $sumOfPowers")
}
import java.io.File
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

fun main() {
    val filePath = "input.txt"
    val redBalls = 12
    val greenBalls = 13
    val blueBalls = 14
    val sumOfGameIds = sumOfPossibleGameIds(filePath, redBalls, greenBalls, blueBalls)
    println("Sum of possible game IDs: $sumOfGameIds")
    val sumOfPowers = sumOfGamePowers(filePath)
    println("Sum of game powers: $sumOfPowers")
}
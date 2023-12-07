import java.io.File
import java.util.Comparator.comparingInt


/**
 * This function calculates the strength of a card.
 * @param withJoker A boolean indicating if the game includes a joker.
 * @return The strength of the card as an integer.
 */
fun Char.cardStrength(withJoker: Boolean): Int = when (this) {
    'A'  -> 15
    'K'  -> 14
    'Q'  -> 13
    'J'  -> 12 * if (withJoker) 0 else 1
    'T'  -> 11
    else -> this.toString().toInt()
}


/**
 * This function determines the type of a hand.
 * @return The type of the hand as an integer.
 */
fun String.type(): Int {
    val counts = this.groupingBy { it }.eachCount()
    val isTwoPair = counts.count { it.value == 2 } == 2
    return when {
        5 in counts.values -> 7
        4 in counts.values -> 6
        3 in counts.values -> if (2 in counts.values) 5 else 4
        isTwoPair          -> 3
        2 in counts.values -> 2
        else               -> 1
    }
}

/**
 * This data class represents an improved hand.
 * @property improved The improved hand as a string.
 * @property original The original hand as a string.
 */
data class ImprovedHand(val improved: String, val original: String)


/**
 * This function compares two ImprovedHand objects.
 * @param withJoker A boolean indicating if the game includes a joker.
 * @return A Comparator for ImprovedHand objects.
 */
fun Comparator<ImprovedHand>.compareCards(withJoker: Boolean) = this
    .thenComparingInt { it.original[0].cardStrength(withJoker) }
    .thenComparingInt { it.original[1].cardStrength(withJoker) }
    .thenComparingInt { it.original[2].cardStrength(withJoker) }
    .thenComparingInt { it.original[3].cardStrength(withJoker) }
    .thenComparingInt { it.original[4].cardStrength(withJoker) }


/**
 * This function improves a hand.
 * @return An ImprovedHand object representing the improved hand.
 */
fun String.improve(): ImprovedHand {
    val improvedHands = this.toCharArray().distinct()
        .map { ImprovedHand(this.replace('J', it), original = this) }
    val jokerComparator = comparingInt<ImprovedHand> { it.improved.type() }.compareCards(withJoker = true)
    return improvedHands.maxWith(jokerComparator)!!
}

/**
 * This function calculates the total winnings.
 * @param improvedToBids A map of ImprovedHand objects to bids.
 * @param comp A Comparator for ImprovedHand objects.
 * @return The total winnings as a long.
 */
fun sumWinnings(improvedToBids: Map<ImprovedHand, Long>, comp: Comparator<ImprovedHand>): Long {
    return improvedToBids
        .toSortedMap(comp)
        .keys
        .mapIndexed { i, improved -> improvedToBids[improved]!! * (i + 1) }
        .sum()
}

/**
 * The main function of the program.
 */
fun main() {
    val lines = File("src/main/resources/Day7_input.txt").readLines()

    val originalComparator = comparingInt<ImprovedHand> { it.original.type() }.compareCards(withJoker = false)
    val jokerComparator = comparingInt<ImprovedHand> { it.improved.type() }.compareCards(withJoker = true)

    val improvedToBids = lines.associate {
        val (hand, bid) = it.split(" ").let { it.first() to it.last().toLong() }
        hand.improve() to bid
    }

    val ans1 = sumWinnings(improvedToBids, originalComparator)
    val ans2 = sumWinnings(improvedToBids, jokerComparator)
    println(ans1)
    println(ans2)
}
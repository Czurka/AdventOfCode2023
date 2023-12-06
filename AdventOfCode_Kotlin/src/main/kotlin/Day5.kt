import java.io.File
import kotlin.math.*

fun main() {
    val dat = File("src/main/resources/Day5_input.txt").readLines().map { it.trim() }

    fun applyMaps(maps: List<List<Triple<Long, Long, Long>>>, seed: Long): Long {
        var preMap = seed
        for (m in maps) {
            for ((ds, ss, rl) in m) {
                if (ss <= preMap && preMap < ss + rl) {
                    preMap = ds + (preMap - ss)
                    break
                }
            }
        }
        return preMap
    }

    fun parse(seedRanges: Boolean): Pair<List<*>, List<List<Triple<Long, Long, Long>>>> {
        val seeds = dat[0].split(": ")[1].split(" ").map { it.toLong() }

        val seedsList = if (seedRanges) {
            seeds.chunked(2).map { it[0]..it[1] }
        } else {
            seeds
        }

        val maps = mutableListOf<List<Triple<Long, Long, Long>>>()
        var currMap = mutableListOf<Triple<Long, Long, Long>>()
        for (d in dat.drop(3)) {
            if (d.isBlank()) {
                continue
            }
            if (":" in d) {
                maps.add(currMap)
                currMap = mutableListOf()
            } else {
                currMap.add(Triple(d.split(" ")[0].toLong(), d.split(" ")[1].toLong(), d.split(" ")[2].toLong()))
            }
        }
        maps.add(currMap)

        return seedsList to maps
    }

    fun part1(): Long {
        val (seeds, maps) = parse(false)
        val locs = seeds.associateWith { applyMaps(maps, it as Long) }
        val minLoc = locs.minByOrNull { it.value }?.value ?: 0L

        locs.forEach { (seed, loc) ->
            if (loc == minLoc) {
                println("Seed $seed maps to location $loc")
            }
        }

        return minLoc
    }

    fun part2(): Long {
    val (seedsAny, maps) = parse(true)
    val seeds = (seedsAny as List<LongRange>).map { it.first to it.last }

    var stepSize = 10.0.pow(ceil(log10((seeds.maxOf { it.second } / 100.0)))).toLong()
    val searchVals = mutableMapOf<Long, Long>()
    for ((start, end) in seeds) {
        for (i in start until end step stepSize) {
            searchVals[i] = applyMaps(maps, i)
        }
    }
    val minSearchVal = searchVals.minWithOrNull(Comparator { o1, o2 -> o1.value.compareTo(o2.value) })
    var roughEst: Long = 0
    var minLoc: Long = 0
    minSearchVal?.let {
        roughEst = it.key
        minLoc = it.value
}

    while (stepSize > 1) {
        val leftSearch = max(minLoc - stepSize, roughEst)
        val rightSearch = min(minLoc + stepSize, roughEst + stepSize)

        stepSize /= 10
        searchVals.clear()
        for (i in leftSearch until rightSearch step stepSize) {
            searchVals[i] = applyMaps(maps, i)
        }
        val minSearchValInLoop = searchVals.minByOrNull { it.value }
        var bestEst: Long = 0
        var bestLoc: Long = 0
        minSearchValInLoop?.let {
            bestEst = it.key
            bestLoc = it.value
}

        roughEst = bestEst
        minLoc = bestLoc
    }

    return minLoc
}

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

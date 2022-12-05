fun main() {
	
	fun part1(input: List<String>) = input.sumOf { rucksack ->
		// Create a set of the items in the first compartment
		val first = rucksack.substring(0, rucksack.length / 2).toSet()
		
		// Create a set of the items in the second compartment
		val second = rucksack.substring(rucksack.length / 2).toSet()
		
		// Find the common item in this rucksack
		val common = first.intersect(second).single()
		
		// Return its priority to be added to the sum
		if (common.isLowerCase()) {
			// Lowercase item types a through z have priorities 1 through 26
			common.code - 96
		} else {
			// Uppercase item types A through Z have priorities 27 through 52
			common.code - 38
		}
	}
	
	
	fun part2(input: List<String>) = input.indices.step(3).sumOf { i ->
		val group = input.subList(i, i + 3)
		val badge = group.map { s -> s.toSet() }.reduce { set1, set2 -> set1.intersect(set2) }.single()
		if (badge.isLowerCase()) badge.code - 'a'.code + 1 else badge.code - 'A'.code + 27
	}
	
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day03_test")
	check(part1(testInput).also { println("Test 1 result was: $it") } == 157)
	check(part2(testInput).also { println("Test 2 result was: $it") } == 70)
	
	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}

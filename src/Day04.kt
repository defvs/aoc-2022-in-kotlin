fun main() {
	fun part1(input: List<String>) = input.count { assignment ->
		val (range1, range2) = assignment.split(",")
		val (lower1, upper1) = range1.split("-").map { it.toInt() }
		val (lower2, upper2) = range2.split("-").map { it.toInt() }
		
		lower1 <= lower2 && upper1 >= upper2 || lower2 <= lower1 && upper2 >= upper1
	}
	
	fun part2(input: List<String>) = input.count { assignment ->
		val (range1, range2) = assignment.split(",")
		val (lower1, upper1) = range1.split("-").map { it.toInt() }
		val (lower2, upper2) = range2.split("-").map { it.toInt() }
		
		upper1 >= lower2 && lower1 <= upper2
	}
	
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day04_test")
	check(part1(testInput).also { println("Test 1 result was: $it") } == 2)
	check(part2(testInput).also { println("Test 2 result was: $it") } == 4)
	
	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}

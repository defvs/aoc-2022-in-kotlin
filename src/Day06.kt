fun main() {
	fun findFirstMarker(input: String, windowSize: Int) =
		input.windowed(windowSize).indexOfFirst { it.toSet().size == windowSize }.plus(windowSize)
	fun part1(input: List<String>) = findFirstMarker(input[0], 4)
	fun part2(input: List<String>) = findFirstMarker(input[0], 14)
	
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day06_test")
	check(part1(testInput).also { println("Test 1 result was: $it") } == 7)
	check(part2(testInput).also { println("Test 2 result was: $it") } == 19)
	
	val input = readInput("Day06")
	println(part1(input))
	println(part2(input))
}

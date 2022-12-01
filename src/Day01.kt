fun main() {
	// Returns a List of Ints containing the sum of calories for each elf.
	fun getAllElvesCalories(input: List<String>) = input.joinToString(separator = "\n")
		.split("\n\n").map { it.split("\n").sumOf { it.toInt() } }

	// Get the maximum value of getAllElvesCalories
	fun part1(input: List<String>) = getAllElvesCalories(input).max()

	// Sum the 3 maximum values of getAllElvesCalories
	fun part2(input: List<String>) = getAllElvesCalories(input).sortedDescending().take(3).sum()

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day01_test")
	check(part1(testInput).also { println("Test result was: $it") } == 24000)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}

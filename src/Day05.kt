fun main() {
	data class Move(val items: Int, val from: Int, val to: Int)
	
	fun <T> List<List<T>>.transpose(): List<List<T>> {
		// Check if the matrix is empty
		if (this.isEmpty()) return emptyList()
		
		// Get the number of rows and columns in the matrix
		val numRows = this.size
		val numCols = this.maxBy { it.size }.size
		
		// Create a new matrix for the transposed matrix
		val transposed = MutableList(numCols) { mutableListOf<T?>() }
		
		// Transpose the matrix, padding each row with null values if necessary
		for (i in 0 until numRows) {
			val row = this[i]
			for (j in 0 until numCols) {
				val value = if (j < row.size) row[j] else null
				transposed[j].add(value)
			}
		}
		
		// Return the transposed matrix as an immutable List
		return transposed.map { it.toList().filterNotNull() }
	}
	
	fun parseCratesInput(input: List<String>): Pair<List<ArrayDeque<Char?>>, List<Move>> {
		// Parse the input
		val initialStacks = input
			.takeWhile { it.isNotEmpty() }.dropLast(1)
			.map { it.chunked(4) { it.singleOrNull { it != '[' && it != ']' && it != ' ' } } }
			.transpose()
			.map { it.reversed() }
			.map { ArrayDeque(it) }
		val moves = input
			.dropWhile { it.isNotBlank() }.drop(1)
			.map { it.split(" ") }
			.map { Move(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1) }
		return Pair(initialStacks, moves)
	}
	
	fun part1(input: List<String>): String {
		val (initialStacks, moves) = parseCratesInput(input)
		moves.forEach { (items, from, to) ->
			for (i in 1..minOf(items, initialStacks[from].size)) initialStacks[to].add(initialStacks[from].removeLast())
		}
		return initialStacks.joinToString("") { it.removeLast()!!.toString() }
	}
	
	fun part2(input: List<String>): String {
		val (initialStacks, moves) = parseCratesInput(input)
		moves.forEach { (items, from, to) ->
			(1..minOf(items, initialStacks[from].size)).map { _ ->
				initialStacks[from].removeLast()
			}.reversed().forEach { initialStacks[to].add(it) }
		}
		return initialStacks.joinToString("") { it.removeLast()!!.toString() }
	}
	
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day05_test")
	check(part1(testInput).also { println("Test 1 result was: $it") } == "CMZ")
	check(part2(testInput).also { println("Test 2 result was: $it") } == "MCD")
	
	val input = readInput("Day05")
	println(part1(input))
	println(part2(input))
}

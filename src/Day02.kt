import RPSResult.Companion.getShapeForResult
import Shapes.Companion.getResult

enum class RPSResult(val value: Int) {
	Loss(0), Draw(3), Win(6);
	
	companion object {
		fun fromString(str: String) = when (str) {
			"X" -> Loss
			"Y" -> Draw
			"Z" -> Win
			else -> throw IllegalArgumentException("RPSResult fromString should be any of X,Y,Z.")
		}
		
		fun RPSResult.getShapeForResult(other: Shapes) = Shapes.values().single { it.getResult(other) == this }
	}
}

enum class Shapes(val value: Int) {
	Rock(1), Paper(2), Scissors(3);
	
	companion object {
		fun fromString(str: String) = when (str) {
			"A", "X" -> Rock
			"B", "Y" -> Paper
			"C", "Z" -> Scissors
			else -> throw IllegalArgumentException("Shapes fromString should be any of A,B,C,X,Y,Z.")
		}
		
		fun Shapes.getResult(other: Shapes) = when {
			this == other -> RPSResult.Draw
			this == Rock && other == Paper -> RPSResult.Loss
			this == Paper && other == Scissors -> RPSResult.Loss
			this == Scissors && other == Rock -> RPSResult.Loss
			else -> RPSResult.Win
		}
	}
}

fun main() {
	fun part1(input: List<String>) = input.map { it.split(" ").map { Shapes.fromString(it) }.zipWithNext().single() }
		.sumOf { it.second.getResult(it.first).value + it.second.value }
	
	fun part2(input: List<String>) = input.map {
			it.split(" ").zipWithNext().single().let { Shapes.fromString(it.first) to RPSResult.fromString(it.second) }
		}.sumOf {
			it.second.getShapeForResult(it.first).value + it.second.value
		}
	
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day02_test")
	check(part1(testInput).also { println("Test 1 result was: $it") } == 15)
	check(part2(testInput).also { println("Test 2 result was: $it") } == 12)
	
	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}

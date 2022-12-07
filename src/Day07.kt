/**
 * Data class representing a command that has been executed in a terminal, along with its output.
 *
 * @param command the command that was executed
 * @param response the output of the command, as a list of strings
 */
data class ExecutedCommand(
	val command: String,
	val response: List<String>,
) {
	/**
	 * Returns the argument at the specified index of the command.
	 *
	 * @param index the index of the argument to return
	 * @return the argument at the specified index
	 */
	fun getArgument(index: Int) = command.split(" ")[index]
	
	/**
	 * Companion object containing utility methods for parsing a list of strings into a list of
	 * `ExecutedCommand` instances.
	 */
	companion object {
		/**
		 * Parses a list of strings into a list of `ExecutedCommand` instances.
		 *
		 * @param input the list of strings to parse
		 * @return the list of parsed `ExecutedCommand` instances
		 */
		fun parseCommands(input: List<String>): List<ExecutedCommand> {
			val result = mutableListOf<ExecutedCommand>()
			var command = ""
			var response = mutableListOf<String>()
			
			// Iterate over the input lines
			input.forEach { line ->
				// If the line starts with a '$', it is the start of a new command
				if (line.startsWith("$")) {
					// If we're already processing a command, add it to the result list
					if (command.isNotEmpty()) result.add(ExecutedCommand(command, response))
					
					// Start processing a new command
					command = line.substring(1).trim() // remove the leading '$' and any whitespace after it
					response = mutableListOf()
				} else {
					// Add the line to the response of the current command
					response.add(line)
				}
			}
			
			// Add the last command to the result list
			if (command.isNotEmpty()) {
				result.add(ExecutedCommand(command, response))
			}
			
			return result
		}
	}
}

/**
 * Abstract class representing a filesystem entry, either a file or a directory.
 */
abstract class FilesystemEntry {
	/**
	 * The name of the filesystem entry.
	 */
	abstract val name: String
	
	/**
	 * The size of the filesystem entry in bytes.
	 */
	abstract val size: Int
}

/**
 * Data class representing a file in the filesystem.
 *
 * @param name the name of the file
 * @param size the size of the file in bytes
 */
data class File(
	override val name: String,
	override val size: Int,
): FilesystemEntry() {
	/**
	 * Constructs a new `File` instance from a line of output from the `ls` command.
	 *
	 * @param lsLine the line of output from the `ls` command
	 */
	constructor(lsLine: String): this(
		name = lsLine.substringAfterLast(" ").trim(),
		size = lsLine.substringBefore(" ").toInt(),
	)
}

/**
 * Data class representing a directory in the filesystem.
 *
 * @param name the name of the directory
 * @param children the child entries contained in the directory
 * @param parent the parent directory, or `null` if this is the root directory
 */
data class Directory(
	override val name: String,
	val children: ArrayList<FilesystemEntry>,
	val parent: Directory?,
): FilesystemEntry() {
	/**
	 * Constructs a new `Directory` instance from a line of output from the `ls` command.
	 *
	 * @param lsLine the line of output from the `ls` command
	 * @param parent the parent directory, or `null` if this is the root directory
	 */
	constructor(lsLine: String, parent: Directory?): this(
		name = lsLine.substringAfterLast(" ").trim(),
		children = arrayListOf(),
		parent = parent,
	)
	
	/**
	 * The size of the directory, calculated as the sum of the sizes of all of its children.
	 */
	override val size: Int by lazy { children.sumOf { it.size } }
}

fun main() {
	fun getDirectorySizes(input: List<String>): List<Int> {
		val commands = ExecutedCommand.parseCommands(input)
		val filesystem = Directory("/", ArrayList(), null)
		var currentDir = filesystem
		
		commands.drop(1).forEach {
			when (it.getArgument(0)) {
				"ls" -> {
					currentDir.children.addAll(it.response.map { lsItem ->
						when {
							lsItem.startsWith("dir") -> Directory(lsItem, currentDir)
							else -> File(lsItem)
						}
					})
				}
				
				"cd" -> {
					currentDir = when (it.getArgument(1)) {
						".." -> currentDir.parent!!
						else -> currentDir.children.first { children -> children.name == it.getArgument(1) } as Directory
					}
				}
				
				else -> throw Exception("Unknown command: ${it.command}")
			}
		}
		
		val allSizes = arrayListOf<Int>()
		fun exploreDir(dir: Directory) {
			allSizes.add(dir.size)
			dir.children.forEach { child ->
				if (child is Directory) exploreDir(child)
			}
		}
		exploreDir(filesystem)
		
		return allSizes
	}
	
	fun part1(input: List<String>) = getDirectorySizes(input).filter { it < 100000 }.sum()
	fun part2(input: List<String>): Int {
		val directorySizes = getDirectorySizes(input)
		val needToDelete = directorySizes[0] - (70000000 - 30000000)
		return directorySizes.filter { it >= needToDelete }.minBy { it - needToDelete }
	}
	
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day07_test")
	check(part1(testInput).also { println("Test 1 result was: $it") } == 95437)
	check(part2(testInput).also { println("Test 2 result was: $it") } == 24933642)
	
	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}

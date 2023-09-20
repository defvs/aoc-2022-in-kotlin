class CPU {
    var regX: Int = 1
    var pc: Int = 0

    enum class InstructionType(val duration: Int) {
        ADDX(2),
        NOOP(1),
    }

    class Instruction(val instruction: InstructionType, vararg val args: Int)

    fun execute(instruction: Instruction) {
        when (instruction.instruction) {
            InstructionType.ADDX -> {
                regX += instruction.args[0]
            }

            InstructionType.NOOP -> {}
        }
        pc += instruction.instruction.duration
    }
}

fun main() {
    fun getPc2X(input: List<String>): List<Pair<Int, Int>> {
        val myCPU = CPU()
        val instructions = input.map { line ->
            line.split(" ").let {
                val instructionType = CPU.InstructionType.valueOf(it[0].uppercase())
                when (it.size) {
                    1 -> CPU.Instruction(instructionType)
                    2 -> CPU.Instruction(instructionType, it[1].toInt())
                    else -> throw Exception("Invalid argument count: \"$line\"")
                }
            }
        }

        return (myCPU.pc to myCPU.regX).let {
            instructions.map { instruction ->
                myCPU.execute(instruction)
                myCPU.pc to myCPU.regX
            } + it
        }.sortedBy { it.first }
    }

    fun part1(input: List<String>): Int {
        val pc2x = getPc2X(input)

        fun findFor(pc: Int) = pc2x.findLast { it.first < pc }!!.let { pc * it.second }

        val signalStrengths = listOf(20, 60, 100, 140, 180, 220).map { findFor(it) }
        return signalStrengths.sum()
    }

    fun part2(input: List<String>): List<Boolean> {
        // FIXME: topleft pixel never shows up
        val display = MutableList(240) { false }
        val pc2x = getPc2X(input)
        fun findFor(pc: Int) = pc2x.findLast { it.first <= pc }!!

        for (i in 1..240) {
            val current = findFor(i)
            val printingValues = listOf(-1, 0, 1).map { (it + i).mod(40) }

            if (printingValues.contains(current.second.mod(40))) display[i] = true
        }

        return display
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput).also { println("Test 1 result was: $it") } == 13140)

    val input = readInput("Day10")
    println(part1(input))
    println(
            part2(input)
                    .map { if (it) '#' else '.' }
                    .chunked(40)
                    .joinToString(separator = "") { it.joinToString("") + "\n" }
    )
}
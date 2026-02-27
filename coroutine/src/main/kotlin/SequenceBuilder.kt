object SequenceBuilder {
    fun run() {
        val sequence = sequence {
            println("first")
            yield(1)
            println("second")
            yield(2)
            println("third")
            yield(3)
            println("done")
        }

        // 중단이 가능한것을 볼수 있다.
        for (number in sequence) {
            println("next number is $number")
        }
    }

    fun fibonacci(): Sequence<Int> = sequence {
        var first = 0
        var second = 1

        while (true) {
            yield(first)
            val temp = first
            first = second
            second += temp
        }
    }
}
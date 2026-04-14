package dean.ai

import kotlin.math.sqrt

object Chapter2 {
    /**
     * 문제 2. 약수 빠르게 구하기 ( #1978, #11653, #14232 )
     *
     * 숫자 N이 주어진다.
     *
     * 이 숫자의 약수가 총 몇 개가 포함되어 있는지 계산하고 싶다.
     *
     * 약수의 개수와, 약수들을 모두 출력하는 프로그램을 작성하시오.
     *
     *
     * 15
     *
     * 2
     * 3 5
     */
    fun problem2(): Int {
        val input = readln().toInt()
        var answer = 0
        for (i in 1 .. sqrt(input.toFloat()).toInt()) {
            if (input % i == 0) answer++
        }
        return answer
    }
}
package dean.ai

object Problem {
    /**
     * 문제 2. 사탕  ( # 14568 )
     * 친구 A,B,C에게 사탕을 나누어 주려고 합니다.
     * 조건은 아래와 같습니다.
     * 1.     남는 사탕이 없어야 합니다.
     * 2.     A는 B보다 2개 이상 많은 사탕을 가져야 합니다.
     * 3.     셋 중 사탕을 하나도 못 받는 친구는 없어야 합니다.
     * 4.     C가 받는 사탕의 수는 짝수입니다.
     * 분배 가능한 경우의 수를 출력하는 프로그램을 작성해주세요.
     *
     */
    fun problem2(): Int {
        val candyCount = readln().toInt()
        var answer = 0
        for (a in 0 .. candyCount) {
            for (b in 0 .. candyCount) {
                for (c in 0 .. candyCount) {
                    if (a + b + c == candyCount) {
                        if (a >= b + 2) {
                            if (a != 0 && b != 0 && c != 0) {
                                if (c % 2 == 0) {
                                    answer++
                                }
                            }
                        }
                    }
                }
            }
        }

        return answer
    }

    /**
     * 문제 3. 연립방정식 ( # 19532 )
     * 숫자 A,B,C,D,E,F 가 주어집니다.
     *
     * 다음 연립방정식에서 x와 y값을 계산하는 프로그램을 작성하세요.
     *
     * ax + by = c
     * dx + ey = f
     *
     * 범위
     * X와 Y는 -10000이상 10000이하인 정수이다.
     *
     * 1 3 -1 4 1 7
     *
     * 2 -1
     */
    fun problem3() {
        val input = readln().split(" ").map { it.toInt() }
//        val (a, b, c, d, e, f) = input
//
//        for (x in -10000 .. 10000) {
//            for (y in -10000 .. 10000) {
//                if (a * x + b * y == c) {
//                    if (d * x + e * y == f) {
//                        println("$x $y")
//                    }
//                }
//            }
//        }
    }

    /**
     * 문제 4. 숫자야구 ( # 2503 )
     *
     * A는 3자리 숫자로 된 정답을 하나 정합니다.
     *
     * B는 3자리 숫자를 제시해서 A가 생각하고 있는 정답을 맞히려고 합니다.
     *
     * B가 말한 숫자가 정답에 포함되어 있다면 1 Ball입니다.
     * B가 말한 숫자가 정답에 포함되어 있고, 자리도 동일하다면 1 Strike입니다.
     *
     * 다른 숫자로 이루어진 세 자리수
     *
     * Strike와 Ball의 결과를 보고, 가능한 숫자를 계산하는 프로그램을 작성하세요.
     *
     * 4
     * 123 1 1
     * 356 1 0
     * 327 2 0
     * 489 0 1
     *
     * 2
     */

    fun problem4() {
        val tryCount = readln().toInt()
        val hints = buildList {
            for (i in 0 until tryCount) {
                add(readln().split(" ").map { it.toInt() })
            }
        }

        for (a in 1 until 10) {
            for (b in 0 until 10) {
                for (c in 0 until 10) {
                    if (a == b || b == c || c == a) {
                        continue
                    }

                    hints.forEach {
                        val number = it[0]
                        val ball = it[1]
                        val strike = it[2]

                        var ballCount = 0
                        var strikeCount = 0

                        val third = number % 10
                        val second = number / 10 % 10
                        val first = number / 10 / 10 % 10

                        if (a == first) {
                            strikeCount ++
                        }

                        if (b == second) {
                            strikeCount ++
                        }

                        if (c == third) {
                            strikeCount ++
                        }
                    }
                }
            }
        }
    }

    /**
     * 문제 5. 모이기 ( # 1090 )
     *
     * N명의 학생들이 모각코를 하기 위해서 한 곳에서 모이려고 합니다.
     *
     * 학생들은 어디에 모여도 괜찮으나, 모든 사람들의 이동 거리를 합쳤을 때, 가장 적은 이동 거리였으면 좋겠다고 주장합니다.
     * N명의 학생의 집의 위치가 Y, X 2차원으로 주어졌을 때,
     * 1,2,3, …, N명의 학생들이 모일 수 있는 최소 거리를 계산하는 프로그램을 작성하세요.
     *
     * 조건
     * N - 50 이하의 수
     * X,Y좌표는 1_000_000이하의 수
     *
     * 4
     * 15 14 - A 짱구
     * 15 16 - B 철수
     * 14 15 - C 맹구
     * 16 15 - D 유리
     * 훈이x
     *
     * 0 2 3 4
     */
    fun problem5() {

    }


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
}
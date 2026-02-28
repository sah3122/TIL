import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Launch {
    /**
     * A, D, B, C, E 순서로 출력이 된다.
     * B, C는 별도의 코루틴으로 동시에 실행이 되지만, 코루틴이 생성되는 시간이 약간 소요되기 때문에
     * D가 먼저 실행된다. 
     */
    fun launchTest() = runBlocking {
        println("A")

        launch {
            println("B")
        }
        launch {
            println("C")
        }

        println("D")
        delay(5000L)
        println("E")
    }
}
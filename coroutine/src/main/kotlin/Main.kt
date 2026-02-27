package io.weverse

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors


val LoomDispatcher = Executors
    .newVirtualThreadPerTaskExecutor()
    .asCoroutineDispatcher()

fun main() {
    SequenceBuilder.run()
    print(SequenceBuilder.fibonacci().take(10).toList())
}
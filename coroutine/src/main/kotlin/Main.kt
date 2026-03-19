package io.weverse

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors


val LoomDispatcher = Executors
    .newVirtualThreadPerTaskExecutor()
    .asCoroutineDispatcher()

fun main() {
    Launch.launchBuilder()
}
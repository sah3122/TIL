package dean.spring.sandbox

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SandboxApplication

fun main(args: Array<String>) {
    runApplication<SandboxApplication>(*args)
}

val <T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(javaClass)

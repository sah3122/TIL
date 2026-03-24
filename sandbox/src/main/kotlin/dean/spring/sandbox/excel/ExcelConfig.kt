package dean.spring.sandbox.excel

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ExcelConfig : WebMvcConfigurer {

    @Bean("excelTaskExecutor")
    fun excelTaskExecutor(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 5
            queueCapacity = 10
            setThreadNamePrefix("excel-export-")
            initialize()
        }
    }

    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
        configurer.setDefaultTimeout(600_000)
        configurer.setTaskExecutor(excelTaskExecutor())
    }
}

package com.nahasops.imagemover.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nahasops.imagemover.exception.AsyncExceptionHandler;

@Configuration
@EnableAsync()
public class ImageMoverExecutor implements AsyncConfigurer {

	@Value("${imagemover.thread.core-pool}")
	private int corePoolSize;

	@Value("${imagemover.thread.max-pool}")
	private int maxPoolSize;

	@Value("${imagemover.thread.timeout}")
	private int threadTimeout;

	@Bean
	@Qualifier("imageMoverExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
		threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
		threadPoolTaskExecutor.setKeepAliveSeconds(threadTimeout);
		threadPoolTaskExecutor.setThreadNamePrefix("mover_task_executor_thread");

		return threadPoolTaskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}

}

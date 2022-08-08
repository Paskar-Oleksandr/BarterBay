package com.barterbay.app.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.concurrent.DelegatingSecurityContextScheduledExecutorService;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import tech.jhipster.async.ExceptionHandlingAsyncTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
@EnableAsync
@EnableScheduling
@AllArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

  private final TaskExecutionProperties taskExecutionProperties;

  @Override
  @Bean(name = "taskExecutor")
  public Executor getAsyncExecutor() {
    log.debug("Creating Async Task Executor");
    final var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
    executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
    executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
    executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
    executor.initialize();
    return new ExceptionHandlingAsyncTaskExecutor(
      new DelegatingSecurityContextAsyncTaskExecutor(executor));
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(getScheduledTaskExecutor());
  }

  @Bean(name = "scheduledTaskExecutor")
  public ScheduledExecutorService getScheduledTaskExecutor() {
    var delegateExecutor = Executors.newSingleThreadScheduledExecutor();
    return new DelegatingSecurityContextScheduledExecutorService(delegateExecutor);
  }
}

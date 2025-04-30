package com.csvtodb.csvtodb.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.lang.NonNull;

@Component
public class JobCompletionListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(JobCompletionListener.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Map<Long, Duration> chunkDurations = new ConcurrentHashMap<>();
    private final AtomicInteger chunkCounter = new AtomicInteger(0);

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        logger.info("=========== BATCH JOB STARTING ===========");
        logger.info("Job {} starting at: {}",
                jobExecution.getJobInstance().getJobName(),
                LocalDateTime.now().format(dateFormatter));
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            long totalMillis = jobExecution.getEndTime() != null ? Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis() : 0;
            LocalDateTime startTime = jobExecution.getStartTime();
            LocalDateTime endTime = jobExecution.getEndTime();
            
            if (endTime != null && startTime != null) {
                Duration duration = Duration.between(startTime, endTime);
                totalMillis = duration.toMillis();
            }
            
            int totalRecordsRead = 0;
            int totalRecordsProcessed = 0;
            int totalRecordsWritten = 0;
            int totalRecordsSkipped = 0;
            
            for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                totalRecordsRead += stepExecution.getReadCount();
                totalRecordsProcessed += stepExecution.getFilterCount() + stepExecution.getWriteCount();
                totalRecordsWritten += stepExecution.getWriteCount();
                totalRecordsSkipped += stepExecution.getSkipCount();
            }
            
            logger.info("=========== BATCH JOB COMPLETED SUCCESSFULLY ===========");
            logger.info("Job completed at: {}", 
                    endTime != null ? endTime.format(dateFormatter) : "Unknown");
            logger.info("Total execution time: {} ms", totalMillis);
            logger.info("Records read: {}", totalRecordsRead);
            logger.info("Records processed: {}", totalRecordsProcessed);
            logger.info("Records written to database: {}", totalRecordsWritten);
            logger.info("Records skipped: {}", totalRecordsSkipped);
            logger.info("Average throughput: {} records/second", 
                    totalMillis > 0 ? (totalRecordsWritten * 1000) / totalMillis : 0);

            logger.info("=== Performance Metrics ===");
            logger.info("Total chunks processed: {}", chunkCounter.get());
            logger.info("Average chunk time: {} ms", chunkDurations.values().stream().mapToLong(Duration::toMillis).average().orElse(0));
            logger.info("Max chunk time: {} ms", chunkDurations.values().stream().mapToLong(Duration::toMillis).max().orElse(0));
            
        } else {
            logger.error("=========== BATCH JOB FAILED ===========");
            logger.error("Job exited with status: {}", jobExecution.getStatus());
            logger.error("Exit description: {}", jobExecution.getExitStatus().getExitDescription());
            
            for (Throwable throwable : jobExecution.getAllFailureExceptions()) {
                logger.error("Exception during job execution:", throwable);
            }
        }
        
    }
}
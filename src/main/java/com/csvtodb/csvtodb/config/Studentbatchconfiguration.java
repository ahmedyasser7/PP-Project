package com.csvtodb.csvtodb.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import com.csvtodb.csvtodb.listener.JobCompletionListener;
import com.csvtodb.csvtodb.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class Studentbatchconfiguration {
    private static final Logger logger = LoggerFactory.getLogger(Studentbatchconfiguration.class);
    
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JobCompletionListener jobCompletionListener;

    @Bean
    public FlatFileItemReader<Student> readFromCsv() {
        FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("src/main/resources/data_extended.csv"));
        reader.setLineMapper(new DefaultLineMapper<Student>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(Student.fields());
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Student>() {
                    {
                        setTargetType(Student.class);
                    }
                });
            }
        });
        reader.setLinesToSkip(1); // Skip the header
        return reader;
    }
    
    @Bean
    public ItemProcessor<Student, Student> processor() {
        return student -> {
            if (student.getEmail() == null || !student.getEmail().contains("@")) {
                logger.warn("Skipping invalid student record: {}", student.getId());
                return null;
            }
            
            if (student.getId() <= 0) {
                logger.warn("Invalid ID: {}", student.getId());
                return null;
            }
            
            if (student.getFirstName() == null || student.getFirstName().isEmpty()) {
                logger.warn("Missing first name for ID: {}", student.getId());
                return null;
            }

            student.setFirstName(student.getFirstName().trim());
            student.setLastName(student.getLastName().trim());
            
            logger.debug("Processing student: ID={}, Name={} {}", 
                    student.getId(), student.getFirstName(), student.getLastName());
            
            return student;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Student> writerIntoDB() {
        JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(
                "INSERT INTO csvtodbdata (id, firstName, lastName, email) VALUES (:id, :firstName, :lastName, :email)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }
    
    @Bean
    @Profile("sequential")
    public TaskExecutor taskExecutor() {
        return new SyncTaskExecutor();
    }

    @Bean
    public Step step() {
        return new StepBuilder("step", jobRepository)
            .<Student, Student>chunk(20, transactionManager)
            .reader(readFromCsv())
            .processor(processor())
            .writer(writerIntoDB())
            .faultTolerant()
            .skipLimit(10)
            .skip(Exception.class)
            .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .start(step())
                .listener(jobCompletionListener)
                .build();
    }

    @Bean
    public CommandLineRunner run(JobLauncher jobLauncher, Job job) {
        return args -> {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        };
    }
}
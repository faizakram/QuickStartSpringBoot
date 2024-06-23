package com.app.config;

import com.app.model.PatronElastic;
import com.app.repository.PatronElasticRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobBean(JobRepository jobRepository, JobListener listener, Step steps) {
        return new JobBuilder("First Job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

    @Bean
    public Step steps(JobRepository jobRepository, DataSourceTransactionManager dataSourceTransactionManager,
                      ItemReader<PatronElastic> reader, ItemProcessor<PatronElastic, PatronElastic> itemProcessor, ItemWriter<PatronElastic> writer) {
        return new StepBuilder("Job Step 1", jobRepository)
                .<PatronElastic, PatronElastic>chunk(10, dataSourceTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public RepositoryItemReader<PatronElastic> reader(PatronElasticRepository patronElasticRepository) {
        RepositoryItemReader<PatronElastic> reader = new RepositoryItemReader<>();
        reader.setRepository(patronElasticRepository);
        reader.setMethodName("findAll");
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    public ItemProcessor<PatronElastic, PatronElastic> processor() {
        return new PatronItemProcessor();
    }

    @Bean
    public ItemWriter<PatronElastic> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<PatronElastic>().sql("""
                        INSERT INTO patrons (first, last, dob, created, modified, email, player_id, gender, parent_id) 
                        VALUES ( :first, :last, :dob, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :email, :playerId, :gender, :parentId)
                        """)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }


}

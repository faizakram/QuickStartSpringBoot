package com.app.config;

import com.app.model.PatronElastic;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
@Log4j2
public class PatronItemProcessor implements ItemProcessor<PatronElastic, PatronElastic> {

    @Override
    public PatronElastic process(PatronElastic item) throws Exception {
        log.info("Hello {}", item);
        return item;
    }
}

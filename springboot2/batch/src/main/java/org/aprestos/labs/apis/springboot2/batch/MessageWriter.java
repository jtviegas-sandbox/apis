package org.aprestos.labs.apis.springboot2.batch;

import lombok.extern.slf4j.Slf4j;
import org.aprestos.labs.apis.springboot2.model.dto.MessageDto;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class MessageWriter implements ItemWriter<MessageDto> {

    @Override
    public void write(List<? extends MessageDto> items) throws Exception {
        log.info("[write|in] ({})", items);
        items.forEach( o -> log.info("msg ts: {}", o.getTimestamp()));
        log.info("[write|out]");
    }
}

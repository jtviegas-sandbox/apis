package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.dto.MessageDto;
import org.aprestos.labs.apis.springboot2.model.entities.Message;

import java.util.function.Function;

public class Message2Entity implements Function<MessageDto, Message> {
    @Override
    public Message apply(MessageDto dto) {
        Message result = new Message();
        result.setIdent(dto.getIdent());
        result.setTimestamp(dto.getTimestamp());
        result.setText(dto.getText());
        return result;
    }
}

package org.aprestos.labs.apis.springboot2.testtools;

import org.apache.commons.lang3.RandomStringUtils;
import org.aprestos.labs.apis.springboot2.model.dto.MessageDto;

import java.util.Date;

public class UtilsMessage {

    public static MessageDto create(){
        MessageDto result = new MessageDto();
        result.setIdent(RandomStringUtils.random(12, true, true));
        result.setTimestamp(new Date().getTime());
        result.setText(RandomStringUtils.random(32, true, false));
        return result;
    }

}

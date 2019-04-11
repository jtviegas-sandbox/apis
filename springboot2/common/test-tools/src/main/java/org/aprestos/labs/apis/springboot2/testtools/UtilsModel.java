package org.aprestos.labs.apis.springboot2.testtools;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.aprestos.labs.apis.springboot2.model.dto.MessageDto;
import org.aprestos.labs.apis.springboot2.model.dto.Problem;
import org.aprestos.labs.apis.springboot2.model.dto.Task;
import org.aprestos.labs.apis.springboot2.model.dto.Item;

import java.util.Arrays;
import java.util.Date;

public class UtilsModel {

    public static MessageDto createMessage(){
        MessageDto result = new MessageDto();
        result.setIdent(RandomStringUtils.random(12, true, true));
        result.setTimestamp(new Date().getTime());
        result.setText(RandomStringUtils.random(32, true, false));
        return result;
    }

    public static Task createTask(){

        Task result = new Task(new Problem(RandomUtils.nextInt(),
                new Item[]{
                        new Item( RandomUtils.nextInt(), RandomUtils.nextInt())
                        , new Item( RandomUtils.nextInt(), RandomUtils.nextInt())
                        , new Item( RandomUtils.nextInt(), RandomUtils.nextInt())
        }));

        return result;
    }

}

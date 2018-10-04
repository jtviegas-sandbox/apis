package org.aprestos.labs.apis.springboot.datamodel.transform;

import java.time.LocalDate;
import java.util.function.Function;

import org.aprestos.labs.apis.springboot.datamodel.dto.Message;
import org.aprestos.labs.apis.springboot.datamodel.schemas.Data;

public class Message2Data implements Function<Message, Data> {


  @Override
  public Data apply(final Message msg) {
    final Data result = new Data();
    
    result.setDescription(msg.getText());
    if( null != msg.getId() )
      result.setId(Long.parseLong(msg.getId()));
    
    result.setTimestamp(LocalDate.now());
    
    return result;
  }

}

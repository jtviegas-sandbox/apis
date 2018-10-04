package org.aprestos.labs.apis.springboot.datamodel.transform;

import java.time.LocalDate;
import java.util.function.Function;

import org.aprestos.labs.apis.springboot.datamodel.dto.Message;
import org.aprestos.labs.apis.springboot.datamodel.schemas.Data;

public class Message2Data implements Function<Message, Data> {


  @Override
  public Data apply(Message t) {
    Data result = new Data();
    
    result.setDescription(t.getText());
    if( null != t.getId() )
      result.setId(Long.parseLong(t.getId()));
    
    result.setTimestamp(LocalDate.now());
    
    return result;
  }

}

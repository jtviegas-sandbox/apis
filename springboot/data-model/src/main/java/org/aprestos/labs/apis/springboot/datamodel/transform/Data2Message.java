package org.aprestos.labs.apis.springboot.datamodel.transform;

import java.util.function.Function;

import org.aprestos.labs.apis.springboot.datamodel.dto.Message;
import org.aprestos.labs.apis.springboot.datamodel.schemas.Data;

public class Data2Message implements Function<Data, Message> {


  @Override
  public Message apply(Data t) {
    Message result = new Message();
    if( null != t.getId() )
      result.setId(t.getId().toString());
    result.setText(t.getDescription());
    return result;
  }

}

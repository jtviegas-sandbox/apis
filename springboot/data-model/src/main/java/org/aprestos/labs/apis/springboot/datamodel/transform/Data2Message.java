package org.aprestos.labs.apis.springboot.datamodel.transform;

import java.util.function.Function;

import org.aprestos.labs.apis.springboot.datamodel.dto.Message;
import org.aprestos.labs.apis.springboot.datamodel.schemas.Data;

public class Data2Message implements Function<Data, Message> {


  @Override
  public Message apply(final Data msg) {
    final Message result = new Message();
    if( null != msg.getId() )
      result.setId(msg.getId().toString());
    result.setText(msg.getDescription());
    return result;
  }

}

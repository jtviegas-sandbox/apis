package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.entities.Item;

import java.util.function.Function;

public class Item2Dto implements Function<Item, org.aprestos.labs.apis.springboot2.model.dto.Item> {


    @Override
    public org.aprestos.labs.apis.springboot2.model.dto.Item apply(Item item) {
        return new org.aprestos.labs.apis.springboot2.model.dto.Item(item.getIdent() , item.getValue(), item.getWeight());
    }
}

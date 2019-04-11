package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.entities.Item;

import java.util.function.Function;

public class Item2entity implements Function<org.aprestos.labs.apis.springboot2.model.dto.Item, Item> {


    @Override
    public Item apply(org.aprestos.labs.apis.springboot2.model.dto.Item item) {
        Item result = new Item();

        result.setIdent(item.getId());
        result.setValue(item.getValue());
        result.setWeight(item.getWeight());

        return result;
    }
}

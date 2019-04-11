package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.entities.Item;
import org.aprestos.labs.apis.springboot2.model.entities.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Problem2entity implements Function<org.aprestos.labs.apis.springboot2.model.dto.Problem, Problem> {

    @Override
    public Problem apply(org.aprestos.labs.apis.springboot2.model.dto.Problem problem) {
        Problem result = new Problem();

        List<Item> items = new ArrayList<>();
        result.setItems(items);
        if(null != problem.getItems() && 0 < problem.getItems().length ){
            Item2entity mapper = new Item2entity();
            for( org.aprestos.labs.apis.springboot2.model.dto.Item item: problem.getItems() )
                items.add(mapper.apply(item));
        }
        result.setIdent(problem.getId());
        result.setCapacity(problem.getCapacity());

        return result;
    }
}

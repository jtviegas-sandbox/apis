package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.dto.Item;
import org.aprestos.labs.apis.springboot2.model.dto.Problem;

import java.util.function.Function;

public class Problem2Dto implements Function<org.aprestos.labs.apis.springboot2.model.entities.Problem, Problem> {

    @Override
    public Problem apply(org.aprestos.labs.apis.springboot2.model.entities.Problem problem) {
        Item[] items;
        if(null != problem.getItems() && !problem.getItems().isEmpty()){
            Item2Dto mapper = new Item2Dto();
            items = new Item[problem.getItems().size()];
            int i = 0;
            for( org.aprestos.labs.apis.springboot2.model.entities.Item item: problem.getItems() )
                items[i++] = mapper.apply(item);
        }
        else {
            items = new Item[]{};
        }
        return new Problem(problem.getIdent(), problem.getCapacity(), items);
    }
}

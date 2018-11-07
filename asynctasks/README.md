# knapsack challenge

## how to run (linux only)

  * load docker images: ``./setup.sh``
  * run docker compose: ``./run.sh``
  * test:
      
    `$ curl -XPOST -H 'Content-type: application/json' http://localhost:6543/knapsack/tasks -d '{"problem": {"capacity": 60, "weights": [10, 20, 33], "values": [10, 3, 30]}}'`
    
    {"task":"1","status":"submitted","timestamps":{"submitted":1540755162,"started":null,"completed":null}}
  

    `$ curl -XGET -H 'Content-type: application/json' http://localhost:6543/knapsack/tasks/1`
    
    {"task":"1","status":"completed","timestamps":{"submitted":1540755162,"started":1540755163,"completed":1540755163}}

    `$ curl -XGET -H 'Content-type: application/json' http://localhost:6543/knapsack/solutions/1`
    
    {"task":"1","problem":{"capacity":60,"weights":[10,20,33],"values":[10,3,30]},"solution":{"items":[0,2],"time":0}}


    `$ curl -XGET -H 'Content-type: application/json' http://localhost:6543/knapsack/admin/tasks`
    
    {"submitted":[],"started":[],"completed":[{"task":"1","status":"completed","timestamps":{"submitted":1540755162,"started":1540755163,"completed":1540755163}}]}


## notes on the solution

  * there are 3 microservices:
    * stateless main _api_ - http://localhost:6543 ([swagger](http://localhost:6543/swagger-ui.html)) - works as a gateway for the tasks processing, gets the tasks state from the store, using an injected stateManager of which implementation is oblivious(apart from its interface), 
    and submits the new tasks to the knapsack 'core' service using an injected knapsack service of which implementation, again, is oblivious (apart from its interface). 
    This service can scale, as per its stateless nature, we can create a cluster of this services with a proxy;
    * stateless _core_ api - http://localhost:8920 ([swagger](http://localhost:8920/swagger-ui.html)) - accepts tasks, manages its execution implementing the knapsack algorithm in a threadpool and notifies about the task state using an injected stateManager of which implementation 
    is oblivious (apart from its interface). At this moment it will only update the task state once is completed, intermediary states update, as for instance __started__, are not implemented 
    in its current implementation as it would force us to at this stage manage an additional thread for the updating/notification. 
    This state notification lends itself perfectly to the use of an EventBus (Guava library would be an option),
    with a separate thread not to affect the task solver, this would probably considered in a next step as an improvement;
    This service can scale, as per its stateless nature, we can create a cluster of this services with a proxy;
    * stateful _store_ api - http://localhost:8910 ([swagger](http://localhost:8910/swagger-ui.html)) - this is now being implememented as an in-memory store, not scalable, but it is interfaced through an injected service in the other api's, which means that we can reimplement this service
    in a more convenient way without any impact in the remaining api's, for instance as a cassandra ring, and provide redundancy and high availability;
  * services shutdown was not implemented, in this case we would want to gracefully save state and manage the ressurrection of the tasks, once the services are restarted, 
    this would be considered in a further development. Other than that the containers are shutdown gracefully, still in this case the `core` service threadpool finishes all the dangling threads gracefully. 



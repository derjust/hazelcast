

## Getting Started (Tutorial)


In this short tutorial, we will create simple Java application using Hazelcast distributed map and queue. Then, we will run our application twice to have two nodes (JVMs) clustered and finalize this tutorial with connecting to our cluster from another Java application by using Hazelcast Native Java Client API.

-   Download the latest [Hazelcast ZIP](http://www.hazelcast.org/download/).

-   Unzip it and add the `lib/hazelcast.jar` to your class path.

-	Deploy the `mancenter-`*version*`.war` included in the ZIP file to your web server, to be able to use the Management Center.

-	Following code will start the first node and create and use `customers` map and queue.

```java
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Queue;

public class GettingStarted {

    public static void main(String[] args) {
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<Integer, String> mapCustomers = instance.getMap("customers");
        mapCustomers.put(1, "Joe");
        mapCustomers.put(2, "Ali");
        mapCustomers.put(3, "Avi");

        System.out.println("Customer with key 1: "+ mapCustomers.get(1));
        System.out.println("Map Size:" + mapCustomers.size());

        Queue<String> queueCustomers = instance.getQueue("customers");
        queueCustomers.offer("Tom");
        queueCustomers.offer("Mary");
        queueCustomers.offer("Jane");
        System.out.println("First customer: " + queueCustomers.poll());
        System.out.println("Second customer: "+ queueCustomers.peek());
        System.out.println("Queue size: " + queueCustomers.size());
    }
}
```
-   Run this class second time to get the second node started.

-   Have you seen they formed a cluster? You should see something like this:

```
Members [2] {
    Member [127.0.0.1:5701]
    Member [127.0.0.1:5702] this
}                              
```

-	Now, you can use the **Management Center** to monitor your maps, queues, other distributed data structures and nodes. Please see [Management Center](#management-center) for usage explanations. 

**Connecting Hazelcast Cluster with Java Client API**


-   Besides `hazelcast.jar` you should also add `hazelcast-client.jar` to your classpath.

-   Following code will start a Hazelcast Client, connect to our two node cluster and print the size of our `customers` map.

```java    
package com.hazelcast.test;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class GettingStartedClient {

    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("127.0.0.1:5701");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap map = client.getMap("customers");
        System.out.println("Map Size:" + map.size());
    }
}
```
-   When you run it, you will see the client properly connecting to the cluster and printing the map size as **3**.

## Resources


-	Hazelcast source code can be found at [Github/Hazelcast](https://github.com/hazelcast/hazelcast).
-	Hazelcast API can be found at [Hazelcast.org](http://www.hazelcast.org/docs/latest/javadoc/).
-	More use cases and resources can be found at [Hazelcast.com](http://www.hazelcast.com).
-	Questions and discussions can be post in [Hazelcast mail group](https://groups.google.com/forum/#!forum/hazelcast).




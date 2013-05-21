package com.hazelcast.partition;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.DataSerializerHook;
import com.hazelcast.nio.serialization.FactoryIdHelper;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.partition.client.AddMigrationListenerRequest;
import com.hazelcast.partition.client.GetPartitionsRequest;
import com.hazelcast.partition.client.PartitionsResponse;

/**
 * @mdogan 5/13/13
 */
public final class PartitionDataSerializerHook implements DataSerializerHook {

    public static final int F_ID = FactoryIdHelper.getFactoryId(FactoryIdHelper.PARTITION_DS_FACTORY, -2);

    public static final int GET_PARTITIONS = 1;
    public static final int PARTITIONS = 2;
    public static final int ADD_LISTENER = 3;


    public int getFactoryId() {
        return F_ID;
    }

    public DataSerializableFactory createFactory() {
        return new DataSerializableFactory() {
            public IdentifiedDataSerializable create(int typeId) {
                switch (typeId) {
                    case GET_PARTITIONS:
                        return new GetPartitionsRequest();
                    case PARTITIONS:
                        return new PartitionsResponse();
                    case ADD_LISTENER:
                        return new AddMigrationListenerRequest();
                }
                return null;
            }
        };
    }
}
/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.map;

import com.hazelcast.impl.DefaultRecord;
import com.hazelcast.nio.Data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TryPutOperation extends BasePutOperation {
    long timeout;
    boolean successful;

    public TryPutOperation(String name, Data dataKey, Data value, String txnId, long timeout) {
        super(name, dataKey, value, txnId);
        this.timeout = timeout;
    }

    public TryPutOperation() {
    }

    public void doOp() {
        if (prepareTransaction()) {
            return;
        }
        successful = recordStore.tryPut(dataKey, dataValue, ttl);
    }

    @Override
    public void writeInternal(DataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeLong(timeout);
    }

    @Override
    public void readInternal(DataInput in) throws IOException {
        super.readInternal(in);
        timeout = in.readLong();
    }

    public long getWaitTimeoutMillis() {
        return timeout;
    }

    public boolean shouldBackup() {
        return successful;
    }

    public void onWaitExpire() {
        getResponseHandler().sendResponse(false);
    }

    public Object getResponse() {
        return successful;
    }

    @Override
    public String toString() {
        return "TryPutOperation{" + name + "}";
    }
}
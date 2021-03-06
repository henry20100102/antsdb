/*-------------------------------------------------------------------------------------------------
 _______ __   _ _______ _______ ______  ______
 |_____| | \  |    |    |______ |     \ |_____]
 |     | |  \_|    |    ______| |_____/ |_____]

 Copyright (c) 2016, antsdb.com and/or its affiliates. All rights reserved. *-xguo0<@

 This program is free software: you can redistribute it and/or modify it under the terms of the
 GNU Affero General Public License, version 3, as published by the Free Software Foundation.

 You should have received a copy of the GNU Affero General Public License along with this program.
 If not, see <https://www.gnu.org/licenses/agpl-3.0.txt>
-------------------------------------------------------------------------------------------------*/
package com.antsdb.saltedfish.sql.vdm;

import java.util.concurrent.atomic.AtomicLong;

import com.antsdb.saltedfish.nosql.Row;
import com.antsdb.saltedfish.nosql.RowIterator;
import com.antsdb.saltedfish.nosql.SpaceManager;

public class DumbCursor extends CursorWithHeap {
    RowIterator iter;
    int[] mapping;
    private boolean isClosed = false;
    private AtomicLong counter;

    public DumbCursor(SpaceManager memman, CursorMeta meta, RowIterator iter, int[] mapping, AtomicLong counter) {
        super(meta);
        this.iter = iter;
        this.mapping = mapping;
        this.counter = counter;
    }

    @Override
    public long next() {
        if (isClosed) {
            return 0;
        }
        boolean hasNext = iter.next();
        if (!hasNext) {
            return 0;
        }
        long pRecord = newRecord();
        Row row = iter.getRow();
        if (row == null) {
            return 0;
        }
        Record.setKey(pRecord, row.getKeyAddress());
        for (int i = 0; i < this.meta.getColumnCount(); i++) {
            long pValue = row.getFieldAddress(this.mapping[i]);
            Record.set(pRecord, i, pValue);
        }
        this.counter.incrementAndGet();
        return pRecord;
    }

    @Override
    public void close() {
        super.close();
        this.isClosed = true;
    }

    @Override
    public String toString() {
        return this.iter.toString();
    }
}

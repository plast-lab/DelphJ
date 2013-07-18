/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2000,2008 Oracle.  All rights reserved.
 *
 * $Id: PrimaryKeyAssigner.java,v 1.30 2008/01/07 14:28:45 cwl Exp $
 */

package com.sleepycat.collections;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;

/**
 * An interface implemented to assign new primary key values.
 * An implementation of this interface is passed to the {@link StoredMap}
 * or {@link StoredSortedMap} constructor to assign primary keys for that
 * store. Key assignment occurs when <code>StoredMap.append()</code> is called.
 *
 * @author Mark Hayes
 */
public interface PrimaryKeyAssigner {

    /**
     * Assigns a new primary key value into the given data buffer.
     */
    void assignKey(DatabaseEntry keyData)
        throws DatabaseException;
}

/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002,2008 Oracle.  All rights reserved.
 *
 * $Id: EvolveInternal.java,v 1.6 2008/02/05 23:28:21 mark Exp $
 */

package com.sleepycat.persist.evolve;

/**
 * <!-- begin JE only -->
 * @hidden
 * <!-- end JE only -->
 * Internal access class that should not be used by applications.
 *
 * @author Mark Hayes
 */
public class EvolveInternal {

    /**
     * Internal access method that should not be used by applications.
     */
    public static EvolveEvent newEvent() {
        return new EvolveEvent();
    }

    /**
     * Internal access method that should not be used by applications.
     */
    public static void updateEvent(EvolveEvent event,
                                   String entityClassName,
                                   int nRead,
                                   int nConverted) {
        event.update(entityClassName);
        event.getStats().add(nRead, nConverted);
    }
}

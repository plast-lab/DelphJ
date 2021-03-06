/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002,2008 Oracle.  All rights reserved.
 *
 */

package com.sleepycat.persist.impl;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBase;
import com.sleepycat.je.DatabaseEntry;

/**
 * Modified PersistKeyBinding.  Get rid of looking up Format, and instead
 * use parameterization. 
 *
 * @author Shan Shan Huang
 */
public class MorphingPersistKeyBinding<K> implements EntryBinding<K> {

    Catalog catalog;
    Format keyFormat;
    boolean rawAccess;

    /**
     * Creates a key binding for a given key class.
     *
     * PersistKeyBinging is created in Store.getPrimaryIndex.
     */
    public PersistKeyBinding(Catalog catalog,
                             String clsName,
                             boolean rawAccess) {
        this.catalog = catalog;
        keyFormat = catalog.getFormat(clsName);
        if (keyFormat == null) {
            throw new IllegalArgumentException
                ("Class is not persistent: " + clsName);
        }
        if (!keyFormat.isSimple() &&
            (keyFormat.getClassMetadata() == null ||
             keyFormat.getClassMetadata().getCompositeKeyFields() == null)) {
            throw new IllegalArgumentException
                ("Key class is not a simple type or a composite key class " +
                 "(composite keys must include @KeyField annotations): " +
                 clsName);
        }
        this.rawAccess = rawAccess;
    }

    /**
     * Creates a key binding dynamically for use by PersistComparator.  Formats
     * are created from scratch rather than using a shared catalog.
     */
    PersistKeyBinding(Class cls, String[] compositeFieldOrder) {
        catalog = SimpleCatalog.getInstance();
        if (compositeFieldOrder != null) {
            assert !SimpleCatalog.isSimpleType(cls);
            keyFormat = new CompositeKeyFormat(cls, null, compositeFieldOrder);
        } else {
            assert SimpleCatalog.isSimpleType(cls);
            keyFormat =
                catalog.getFormat(cls, false /*openEntitySubclassIndexes*/);
        }
        keyFormat.initializeIfNeeded(catalog);
    }

    /**
     * Binds bytes to an object for use by PersistComparator as well as
     * entryToObject.
     */
    Object bytesToObject(byte[] bytes, int offset, int length) {
        return readKey(keyFormat, catalog, bytes, offset, length, rawAccess);
    }

    /**
     * Binds bytes to an object for use by PersistComparator as well as
     * entryToObject.
     */
    static Object readKey(Format keyFormat,
                          Catalog catalog,
                          byte[] bytes,
                          int offset,
                          int length,
                          boolean rawAccess) {
        EntityInput input = new RecordInput
            (catalog, rawAccess, null, 0, bytes, offset, length);
        return input.readKeyObject(keyFormat);
    }

    public E entryToObject(DatabaseEntry entry) {
        return bytesToObject
            (entry.getData(), entry.getOffset(), entry.getSize());
    }

    public void objectToEntry(Object object, DatabaseEntry entry) {
        RecordOutput output = new RecordOutput(catalog, rawAccess);
        output.writeKeyObject(object, keyFormat);
        TupleBase.outputToEntry(output, entry);
    }
}

/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002,2008 Oracle.  All rights reserved.
 *
 * $Id: ReflectionAccessor.java,v 1.23 2008/03/18 18:38:08 mark Exp $
 */

package com.sleepycat.persist.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Replaces all com.sleepycat.persist.impl.Accessor
 *
 * @author Shan Shan Huang
 */
class MorphingAccessor<T> {

    private static final FieldAccess[] EMPTY_KEYS = {};

    private Class type;
    private Accessor superAccessor;
    private Constructor constructor;

    // primary key.
    private FieldAccess priKey;

    // secondary keys.
    private FieldAccess[] secKeys;

    // non key fields.
    private FieldAccess[] nonKeys;


    // TODO: needs to force primary key to be a primitive type.
    
    public T newInstance() {
	errorif no ( T () : T.methods )
	return new T();
    }

    public T[] newArray(int len) {
	return new T[len];
    }

    public boolean isPriKeyFieldNullOrZero(T o) {
	<F>[f] errorif no ( @primary F f : T.fields );

	<F extends Object>[f]for ( @primary F f : T.fields )
	return o.f == null;

	<primitive F>[f] for ( @primary F f : T.fields )
	return ((long) o.f) == null;

	// no need to look to superclass because T.fields covers all
	// of T's fields, including its superclasses.
    }

    public void writePriKeyField(T o, EntityOutput output) {
	<F>[f] errorif no ( @primary F f : T.fields );

	<F extends Object>[f]for ( @primary F f : T.fields )
	output.writeKeyObject(o.f, 
			      // TODO: format 
			      format);
    }

    public void readPriKeyField(Object o, EntityInput input) {
        try {
            if (priKey != null) {
                priKey.read(o, input);
            } else if (superAccessor != null) {
                superAccessor.readPriKeyField(o, input);
            } else {
                throw new IllegalStateException("No primary key field");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeSecKeyFields(Object o, EntityOutput output) {
        try {
            if (priKey != null && !priKey.isPrimitive) {
                output.registerPriKeyObject(priKey.field.get(o));
            }
            if (superAccessor != null) {
                superAccessor.writeSecKeyFields(o, output);
            }
            for (int i = 0; i < secKeys.length; i += 1) {
                secKeys[i].write(o, output);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void readSecKeyFields(Object o,
                                 EntityInput input,
                                 int startField,
                                 int endField,
                                 int superLevel) {
        try {
            if (priKey != null && !priKey.isPrimitive) {
                input.registerPriKeyObject(priKey.field.get(o));
            }
            if (superLevel != 0 && superAccessor != null) {
                superAccessor.readSecKeyFields
                    (o, input, startField, endField, superLevel - 1);
            } else {
                if (superLevel > 0) {
                    throw new IllegalStateException
                        ("Superclass does not exist");
                }
            }
            if (superLevel <= 0) {
                for (int i = startField;
                     i <= endField && i < secKeys.length;
                     i += 1) {
                    secKeys[i].read(o, input);
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeNonKeyFields(Object o, EntityOutput output) {
        try {
            if (superAccessor != null) {
                superAccessor.writeNonKeyFields(o, output);
            }
            for (int i = 0; i < nonKeys.length; i += 1) {
                nonKeys[i].write(o, output);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void readNonKeyFields(Object o,
                                 EntityInput input,
                                 int startField,
                                 int endField,
                                 int superLevel) {
        try {
            if (superLevel != 0 && superAccessor != null) {
                superAccessor.readNonKeyFields
                    (o, input, startField, endField, superLevel - 1);
            } else {
                if (superLevel > 0) {
                    throw new IllegalStateException
                        ("Superclass does not exist");
                }
            }
            if (superLevel <= 0) {
                for (int i = startField;
                     i <= endField && i < nonKeys.length;
                     i += 1) {
                    nonKeys[i].read(o, input);
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object getField(Object o,
                           int field,
                           int superLevel,
                           boolean isSecField) {
        if (superLevel > 0) {
            return superAccessor.getField
                (o, field, superLevel - 1, isSecField);
        }
        try {
            Field fld =
		isSecField ? secKeys[field].field : nonKeys[field].field;
            return fld.get(o);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setField(Object o,
                         int field,
                         int superLevel,
                         boolean isSecField,
                         Object value) {
        if (superLevel > 0) {
            superAccessor.setField
                (o, field, superLevel - 1, isSecField, value);
	    return;
        }
        try {
            Field fld =
		isSecField ? secKeys[field].field : nonKeys[field].field;
            fld.set(o, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Abstract base class for field access classes.
     */
    private static abstract class FieldAccess {

        Field field;
        boolean isPrimitive;

        FieldAccess(Field field) {
            this.field = field;
            isPrimitive = field.getType().isPrimitive();
        }

        /**
         * Writes a field.
         */
        abstract void write(Object o, EntityOutput out)
            throws IllegalAccessException;

        /**
         * Reads a field.
         */
        abstract void read(Object o, EntityInput in)
            throws IllegalAccessException;

        /**
         * Returns whether a field is null (for reference types) or zero (for
         * primitive integer types).  This implementation handles the reference
         * types.
         */
        boolean isNullOrZero(Object o)
            throws IllegalAccessException {

            return field.get(o) == null;
        }
    }

    /**
     * Access for fields with object types.
     */
    private static class ObjectAccess extends FieldAccess {

        ObjectAccess(Field field) {
            super(field);
        }

        @Override
        void write(Object o, EntityOutput out)
            throws IllegalAccessException {

            out.writeObject(field.get(o), null);
        }

        @Override
        void read(Object o, EntityInput in)
            throws IllegalAccessException {

            field.set(o, in.readObject());
        }
    }

    /**
     * Access for primary key fields and composite key fields with object
     * types.
     */
    private static class KeyObjectAccess extends FieldAccess {

        private Format format;

        KeyObjectAccess(Field field, Format format) {
            super(field);
            this.format = format;
        }

        @Override
        void write(Object o, EntityOutput out)
            throws IllegalAccessException {

            out.writeKeyObject(field.get(o), format);
        }

        @Override
        void read(Object o, EntityInput in)
            throws IllegalAccessException {

            field.set(o, in.readKeyObject(format));
        }
    }

    /**
     * Access for fields with primitive types.
     */
    private static class PrimitiveAccess extends FieldAccess {

        private SimpleFormat format;

        PrimitiveAccess(Field field, SimpleFormat format) {
            super(field);
            this.format = format;
        }

        @Override
        void write(Object o, EntityOutput out)
            throws IllegalAccessException {

            format.writePrimitiveField(o, out, field);
        }

        @Override
        void read(Object o, EntityInput in)
            throws IllegalAccessException {

            format.readPrimitiveField(o, in, field);
        }

        @Override
        boolean isNullOrZero(Object o)
            throws IllegalAccessException {

            return field.getLong(o) == 0;
        }
    }
}

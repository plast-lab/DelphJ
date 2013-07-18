/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002,2008 Oracle.  All rights reserved.
 *
 * $Id: DbInternal.java,v 1.56 2008/03/18 15:53:04 mark Exp $
 */

package com.sleepycat.je;

import java.io.File;
import java.util.Properties;

import com.sleepycat.je.dbi.CursorImpl;
import com.sleepycat.je.dbi.DatabaseImpl;
import com.sleepycat.je.dbi.EnvironmentImpl;
import com.sleepycat.je.dbi.GetMode;
import com.sleepycat.je.txn.Locker;

/**
 * @hidden
 * For internal use only. It serves to shelter methods that must be public to
 * be used by other BDB JE packages but that are not part of the public API
 * available to applications.
 */
public class DbInternal {

    /**
     * Proxy to Database.invalidate()
     */
    public static void dbInvalidate(Database db) {
        db.invalidate();
    }

    /**
     * Proxy to Database.setHandleLockOwnerTxn
     */
    public static void dbSetHandleLocker(Database db, Locker locker) {
        db.setHandleLocker(locker);
    }

    /**
     * Proxy to Environment.getDbEnvironment
     */
    public static EnvironmentImpl envGetEnvironmentImpl(Environment env) {
        return env.getEnvironmentImpl();
    }

    /**
     * Proxy to Cursor.retrieveNext().
     */
    public static OperationStatus retrieveNext(Cursor cursor,
                                               DatabaseEntry key,
                                               DatabaseEntry data,
                                               LockMode lockMode,
                                               GetMode getMode)
        throws DatabaseException {

        return cursor.retrieveNext(key, data, lockMode, getMode);
    }

    /**
     * Proxy to Cursor.advanceCursor()
     */
    public static boolean advanceCursor(Cursor cursor,
                                        DatabaseEntry key,
                                        DatabaseEntry data) {
	return cursor.advanceCursor(key, data);
    }

    /**
     * Proxy to Cursor.getCursorImpl()
     */
    public static CursorImpl getCursorImpl(Cursor cursor) {
        return cursor.getCursorImpl();
    }

    /**
     * Proxy to Database.getDatabase()
     */
    public static DatabaseImpl dbGetDatabaseImpl(Database db) {
        return db.getDatabaseImpl();
    }

    /**
     * Proxy to JoinCursor.getSortedCursors()
     */
    public static Cursor[] getSortedCursors(JoinCursor cursor) {
	return cursor.getSortedCursors();
    }

    /**
     * Proxy to EnvironmentConfig.setLoadPropertyFile()
     */
    public static void setLoadPropertyFile(EnvironmentConfig config,
                                           boolean loadProperties) {
        config.setLoadPropertyFile(loadProperties);
    }

    /**
     * Proxy to EnvironmentConfig.setCreateUP()
     */
    public static void setCreateUP(EnvironmentConfig config,
                                   boolean checkpointUP) {
        config.setCreateUP(checkpointUP);
    }

    /**
     * Proxy to EnvironmentConfig.getCreateUP()
     */
    public static boolean getCreateUP(EnvironmentConfig config) {
        return config.getCreateUP();
    }

    /**
     * Proxy to EnvironmentConfig.setCheckpointUP()
     */
    public static void setCheckpointUP(EnvironmentConfig config,
                                       boolean checkpointUP) {
        config.setCheckpointUP(checkpointUP);
    }

    /**
     * Proxy to EnvironmentConfig.getCheckpointUP()
     */
    public static boolean getCheckpointUP(EnvironmentConfig config) {
        return config.getCheckpointUP();
    }

    /**
     * Proxy to EnvironmentConfig.setTxnReadCommitted()
     */
    public static void setTxnReadCommitted(EnvironmentConfig config,
                                           boolean txnReadCommitted) {
        config.setTxnReadCommitted(txnReadCommitted);
    }

    /**
     * Proxy to EnvironmentConfig.setTxnReadCommitted()
     */
    public static boolean getTxnReadCommitted(EnvironmentConfig config) {
        return config.getTxnReadCommitted();
    }

    /**
     * Proxy to EnvironmentConfig.cloneConfig()
     */
    public static EnvironmentConfig cloneConfig(EnvironmentConfig config) {
        return config.cloneConfig();
    }

    /**
     * Proxy to EnvironmentMutableConfig.cloneMutableConfig()
     */
    public static
        EnvironmentMutableConfig cloneMutableConfig(EnvironmentMutableConfig
                                                    config) {
        return config.cloneMutableConfig();
    }

    /**
     * Proxy to EnvironmentMutableConfig.checkImmutablePropsForEquality()
     */
    public static void
        checkImmutablePropsForEquality(EnvironmentMutableConfig config,
                                       EnvironmentMutableConfig passedConfig)
        throws IllegalArgumentException {

        config.checkImmutablePropsForEquality(passedConfig);
    }

    /**
     * Proxy to EnvironmentMutableConfig.copyMutablePropsTo()
     */
    public static void copyMutablePropsTo(EnvironmentMutableConfig config,
                                          EnvironmentMutableConfig toConfig) {
        config.copyMutablePropsTo(toConfig);
    }

    /**
     * Proxy to EnvironmentMutableConfig.validateParams.
     */
    public static void disableParameterValidation
	(EnvironmentMutableConfig config) {
	config.setValidateParams(false);
    }

    /**
     * Proxy to EnvironmentMutableConfig.getProps
     */
    public static Properties getProps(EnvironmentMutableConfig config) {
        return config.getProps();
    }

    /**
     * Proxy to DatabaseConfig.setUseExistingConfig()
     */
    public static void setUseExistingConfig(DatabaseConfig config,
                                            boolean useExistingConfig) {
        config.setUseExistingConfig(useExistingConfig);
    }

    /**
     * Proxy to DatabaseConfig.match(DatabaseConfig()
     */
    public static void databaseConfigValidate(DatabaseConfig config1,
						 DatabaseConfig config2)
	throws DatabaseException {

        config1.validate(config2);
    }

    /**
     * Proxy to Transaction.getLocker()
     */
    public static Locker getLocker(Transaction txn)
        throws DatabaseException {

        return txn.getLocker();
    }

    /**
     * Proxy to Environment.getDefaultTxnConfig()
     */
    public static TransactionConfig getDefaultTxnConfig(Environment env) {
        return env.getDefaultTxnConfig();
    }

    /**
     * Get an Environment only if the environment is already open. This
     * will register this Environment in the EnvironmentImpl's reference count,
     * but will not configure the environment.
     * @return null if the environment is not already open.
     */
    public static Environment getEnvironmentShell(File environmentHome) {
        Environment env = null;
        try {
            env = new Environment(environmentHome);

            /* If the environment is not already open, return a null. */
            if (env.getEnvironmentImpl() == null) {
                env = null;
            }
        } catch (DatabaseException e) {

	    /*
	     * Klockwork - ok
             * the environment is not valid.
	     */
        }
        return env;
    }

    public static RunRecoveryException makeNoArgsRRE() {
	return new RunRecoveryException();
    }

    public static ExceptionEvent makeExceptionEvent(Exception e, String n) {
	return new ExceptionEvent(e, n);
    }

    public static Database openLocalInternalDatabase(Environment env,
						String databaseName,
						DatabaseConfig dbConfig)
	throws DatabaseException {

	return env.openLocalInternalDatabase(databaseName, dbConfig);
    }

    public static void removeInternalDatabase(Environment env,
                                              Transaction txn,
                                              String databaseName,
                                              boolean autoTxnIsReplicated)
	throws DatabaseException {

	env.removeDatabaseInternal(txn, databaseName,
                                          autoTxnIsReplicated);
    }

    public static long truncateInternalDatabase(Environment env,
                                                Transaction txn,
                                                String databaseName,
                                                boolean returnCount,
                                                boolean autoTxnIsReplicated)
	throws DatabaseException {

	return env.truncateDatabaseInternal(txn, databaseName, returnCount,
                                            autoTxnIsReplicated);
    }

    public static void setDbConfigReplicated(DatabaseConfig dbConfig,
                                             boolean replicated) {
        dbConfig.setReplicated(replicated);
    }

    public static boolean getDbConfigReplicated(DatabaseConfig dbConfig) {

        return dbConfig.getReplicated();
    }

    public static boolean dbConfigPersistentEquals(DatabaseConfig dbConfig,
                                                   DatabaseConfig other) {

        return dbConfig.persistentEquals(other);
    }

    public static Environment makeEnvironment(File envHome,
                                              EnvironmentConfig config,
                                              boolean replicationIntended)
        throws DatabaseException {

        return new Environment(envHome, config, replicationIntended);
    }
}

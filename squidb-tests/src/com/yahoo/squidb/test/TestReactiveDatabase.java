/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache 2.0 License.
 * See the accompanying LICENSE file for terms.
 */
package com.yahoo.squidb.test;

import android.content.Context;

import com.yahoo.squidb.data.ISQLiteDatabase;
import com.yahoo.squidb.data.SQLiteOpenHelperWrapper;
import com.yahoo.squidb.reactive.ReactiveSquidDatabase;
import com.yahoo.squidb.sql.AttachDetachTest;
import com.yahoo.squidb.sql.Index;
import com.yahoo.squidb.sql.Table;
import com.yahoo.squidb.sql.View;

import org.sqlite.database.sqlite.SQLiteDatabase;

public class TestReactiveDatabase extends ReactiveSquidDatabase {

    private static final Index INDEX_TESTMODELS_LUCKYNUMBER = TestModel.TABLE
            .index("index_testmodels_luckynumber", TestModel.LUCKY_NUMBER);

    public TestReactiveDatabase(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "testDb";
    }

    @Override
    protected Table[] getTables() {
        return new Table[]{
                TestModel.TABLE,
                Thing.TABLE,
                Employee.TABLE,
                TriggerTester.TABLE,
                BasicData.TABLE,
                TestVirtualModel.TABLE
        };
    }

    @Override
    protected Index[] getIndexes() {
        return new Index[]{INDEX_TESTMODELS_LUCKYNUMBER};
    }

    @Override
    protected View[] getViews() {
        return new View[]{
                TestViewModel.VIEW
        };
    }

    @Override
    protected SQLiteOpenHelperWrapper createOpenHelper(String databaseName,
            OpenHelperDelegate delegate, int version) {
        return SquidTestRunner.selectedBinding.getOpenHelper(context, databaseName, delegate, version);
    }

    @Override
    protected int getVersion() {
        return 1;
    }

    @Override
    protected boolean onUpgrade(ISQLiteDatabase db, int oldVersion, int newVersion) {
        return true;
    }

    @Override
    protected void onConfigure(ISQLiteDatabase db) {
        /** @see AttachDetachTest#testAttacherInTransactionOnAnotherThread() */
        Object wrappedObject = db.getWrappedObject();
        if (wrappedObject instanceof SQLiteDatabase) {
            ((SQLiteDatabase) wrappedObject).enableWriteAheadLogging();
        } else if (wrappedObject instanceof android.database.sqlite.SQLiteDatabase) {
            ((android.database.sqlite.SQLiteDatabase) wrappedObject).enableWriteAheadLogging();
        }
    }
}

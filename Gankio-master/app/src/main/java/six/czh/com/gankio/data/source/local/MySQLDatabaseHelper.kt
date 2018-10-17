package six.czh.com.gankio.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import six.czh.com.gankio.util.LogUtils

/**
 * Created by oneplus on 18-10-9.
 * Email: six.cai@oneplus.com
 */
val VERSION = 1
val DB_NAME = "data.db"
val TABLE_NAME = "fuli"

class MySQLDatabaseHelper(private val context: Context): SQLiteOpenHelper(context, DB_NAME, null, VERSION) {


    private val CREATE_FULI_SQL = "CREATE TABLE $TABLE_NAME (\n" +
            " _id TEXT PRIMARY KEY,\n" +
            " createdAt TEXT,\n" +
            " publishedAt TEXT,\n" +
            " desc TEXT,\n" +
            " source TEXT,\n" +
            " type TEXT,\n" +
            " url TEXT,\n" +
            " used INTEGER,\n" +
            " who TEXT\n" +
            ")"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FULI_SQL)
        LogUtils.d("MySQLDatabaseHelper1111", CREATE_FULI_SQL)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
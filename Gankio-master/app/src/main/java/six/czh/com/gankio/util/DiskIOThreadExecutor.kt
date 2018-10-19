package six.czh.com.gankio.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by czh on 18-10-17.
 * Email: six.cai@czh.com
 * Executor that runs a task on a new background thread.
 */
class DiskIOThreadExecutor : Executor {

    private val diskIO = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable) { diskIO.execute(command) }
}
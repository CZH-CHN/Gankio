package six.czh.com.gankio.data


/**
 * Created by cai on 18-10-29.
 * Email: baicai94@foxmail.com
 */
@Suppress("unused") // T is used in extending classes
sealed class Resource<T> {
    companion object {
        fun <T> create(error: Throwable): ErrorResource<T> {
            return ErrorResource(error.message ?: "unknown error")
        }

        fun <T> create(bean: T?): Resource<T>? {
            return if (bean == null) {
                EmptyResource()
            } else {
                SuccessResource(bean)
            }
        }
    }
}

//空数据
class EmptyResource<T> : Resource<T>()

//成功的返回数据
data class SuccessResource<T>(val body: T) : Resource<T>()

//失败的返回
data class ErrorResource<T>(val errorMessage: String) : Resource<T>()
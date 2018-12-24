package six.czh.com.gankio.testAdapter;

/**
 * Created by cai on 18-12-6.
 * Email: baicai94@foxmail.com
 */
public interface TypePool {

    <T> void resiger(Class<? extends T> clazz,
                     ItemViewBinder<T, ?> binder);

    <T> void resiger(Class<? extends T> clazz,
                     OneToManyItemViewGroup<T> group);

    <T> boolean unregister(Class<? extends T> clazz);

    <T> int indexOfTypeOf(T item);

    ItemViewBinder<?, ?> getItemViewBinder(int index);
}

package six.czh.com.gankio.testAdapter;

/**
 * Created by oneplus on 18-12-6.
 * Email: six.cai@oneplus.com
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

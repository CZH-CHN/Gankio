package six.czh.com.gankio.testAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oneplus on 18-12-7.
 * Email: six.cai@oneplus.com
 */
public class MultiTypePool implements TypePool {

    private List<String> classNames;

    private List<ItemViewBinder<?, ?>> binders;

    private Map<String, OneToManyItemViewGroup> itemViewGroupMap;

    /**
     * Constructs a MultiTypePool with default lists.
     */
    public MultiTypePool() {
        this.classNames = new ArrayList<>();
        this.binders = new ArrayList<>();
        this.itemViewGroupMap = new HashMap<>();
    }

    /**
     * Constructs a MultiTypePool with default lists and a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public MultiTypePool(int initialCapacity) {
        this.classNames = new ArrayList<>(initialCapacity);
        this.binders = new ArrayList<>(initialCapacity);
        this.itemViewGroupMap = new HashMap<>(initialCapacity);
    }

    /**
     * 多对多的注册方法
     * @param clazz
     * @param binder
     * @param <T>
     */
    @Override
    public <T> void resiger(Class<? extends T> clazz, ItemViewBinder<T, ?> binder) {
        resiger(getClassName(clazz), binder);
    }

    /**
     * 一对多的注册方法
     * @param clazz
     * @param group
     * @param <T>
     */
    @Override
    public <T> void resiger(Class<? extends T> clazz, OneToManyItemViewGroup<T> group) {

        for (ItemViewBinder binder: group.getItemViewBinders()) {
            resiger(getClassNameFromGroup(clazz, group, binder), binder);
        }
        itemViewGroupMap.put(getClassName(clazz), group);
    }

    private void resiger(String className, ItemViewBinder binder) {
        //TODO Need To Check the class name is exist or vaild
        classNames.add(className);
        binders.add(binder);
    }

    @Override
    public <T> boolean unregister(Class<? extends T> clazz) {
        boolean removed = false;

        if (itemViewGroupMap.containsKey(getClassName(clazz))){
//            for ()

        }
//        classNames.remove();
//        binders.remove()

//        int index = classNames.indexOf(clazz.getName());
        return removed;
    }

    /**
     *
     * @param item
     * @param <T>
     * @return
     */
    @Override
    public <T> int indexOfTypeOf(T item) {
        String typeName = getClassName(item.getClass());

        if (itemViewGroupMap.containsKey(typeName)) {
            ItemViewBinder binder = itemViewGroupMap.get(typeName).getItemViewBinder(item);
            typeName = getClassNameFromGroup(item.getClass(), itemViewGroupMap.get(typeName), binder);
        }
        return classNames.indexOf(typeName);
    }

    @Override
    public ItemViewBinder<?, ?> getItemViewBinder(int index) {
        return binders.get(index);
    }


    private String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    private String getClassNameFromGroup(Class<?> cls, OneToManyItemViewGroup group, ItemViewBinder binder) {
        return getClassName(cls) + group.getItemViewBinderTag(binder);
    }
}

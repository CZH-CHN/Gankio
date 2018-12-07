package six.czh.com.gankio.testAdapter;

/**
 * Created by oneplus on 18-12-7.
 * Email: six.cai@oneplus.com
 */
public abstract class OneToManyItemViewGroup<T> {

    private ItemViewBinder[] itemViewBinders;

    public OneToManyItemViewGroup(ItemViewBinder... itemViewBinders) {
        if (itemViewBinders == null || itemViewBinders.length == 0) {
            throw new IllegalArgumentException("check the itemViewBinder content is not empty");
        }
        this.itemViewBinders = itemViewBinders;
    }

    /**
     *
     * @param item the adapter's item data
     * @return viewType
     */
    protected abstract int getViewHolderIndex(T item);

    /**
     *
     * @param itemViewBinders
     * @return itemViewBinder class name
     */
    public String getItemViewBinderTag(ItemViewBinder itemViewBinders) {
        return itemViewBinders.getClass().getName();
    }

    public ItemViewBinder getItemViewBinder(T item) {
        int index = getViewHolderIndex(item);

        return itemViewBinders[index];
    }

    public ItemViewBinder[] getItemViewBinders() {
        return itemViewBinders;
    }
}

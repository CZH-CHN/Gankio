package six.czh.com.gankio.testAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by czh on 18-12-6.
 * Email: caichelin@gmail.com
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> headItems;
    private List<Object> footItems;
    private List<Object> dataitems;
    private TypePool typePool;

    public MultiTypeAdapter() {
        this(Collections.emptyList());
    }

    public MultiTypeAdapter(@NonNull List<? extends Object> items) {
        this(items, new MultiTypePool());
    }

    public MultiTypeAdapter(@NonNull List<? extends Object> items, int initialCapacity) {
        this(items, new MultiTypePool(initialCapacity));
    }

    public MultiTypeAdapter(@NonNull List<? extends Object> items, @NonNull TypePool pool) {
        this.dataitems = (List<Object>) items;
        this.headItems = new ArrayList<>();
        this.footItems = new ArrayList<>();
        this.typePool = pool;
    }

    public void setItems(@NonNull List<? extends Object> items) {
        this.dataitems = (List<Object>) items;
    }

    public @NonNull
    List<?> getItems() {
        return dataitems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int indexViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewBinder<?,?> binder = typePool.getItemViewBinder(indexViewType);
        return binder.onCreateViewHolder(inflater, parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewBinder binder = typePool.getItemViewBinder(viewHolder.getItemViewType());
        Object item = getItem(position);
        //TODO 空指针问题防范
        binder.onBindViewHolder(viewHolder, item);

    }

    @Override
    public int getItemCount() {
        return dataitems.size() + getHeadCount() + getFootCount();
    }

    @Override
    public int getItemViewType(int position) {
        //TODO 合法性判断(IndexOutOfBound)
        return typePool.indexOfTypeOf(getItem(position));
    }

    public Object getItem(int position) {
        if (position < headItems.size()) {
            return headItems.get(position);
        }

        position -= headItems.size();

        if (position < dataitems.size()) {
            return  dataitems.get(position);
        }

        position -= dataitems.size();

        if (position < footItems.size()) {
            return footItems.get(position);
        }

        return null;
    }

    /**
     *
     * @param clazz
     * @param binder
     * @param <T>
     */
    public <T> void register(Class<? extends T> clazz, ItemViewBinder<T, ?> binder) {
        typePool.resiger(clazz, binder);
    }

    /**
     *
     * @param clazz
     * @param group
     * @param <T>
     */
    public <T> void register(Class<? extends T> clazz, OneToManyItemViewGroup<T> group) {
        typePool.resiger(clazz, group);
    }


    /**
     *
     * @param headItem
     */
    public void addHeadItem(Object headItem) {
        headItems.add(headItem);
    }

    /**
     *
     * @param footItem
     */
    public void addFooterItem(Object footItem) {
        footItems.add(footItem);
    }


    public List<Object> getFootItems() {
        return footItems;
    }

    public int getHeadCount() {
        return headItems.size();
    }

    public int getFootCount() {
        return footItems.size();
    }

}

package six.czh.com.gankio.testAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by czh on 18-12-6.
 * Email: caichelin@gmail.com
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<?> items;

    private TypePool typePool;

    public MultiTypeAdapter() {
        this(Collections.emptyList());
    }

    public MultiTypeAdapter(@NonNull List<?> items) {
        this(items, new MultiTypePool());
    }

    public MultiTypeAdapter(@NonNull List<?> items, int initialCapacity) {
        this(items, new MultiTypePool(initialCapacity));
    }

    public MultiTypeAdapter(@NonNull List<?> items, @NonNull TypePool pool) {
        this.items = items;
        this.typePool = pool;
    }

    public void setItems(@NonNull List<?> items) {
        this.items = items;
    }

    public @NonNull List<?> getItems() {
        return items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewBinder<?,?> binder = typePool.getItemViewBinder(position);
        return binder.onCreateViewHolder(inflater, parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewBinder binder = typePool.getItemViewBinder(viewHolder.getItemViewType());
        Object item = items.get(position);
        binder.onBindViewHolder(viewHolder, item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //TODO 合法性判断(IndexOutOfBound)
        return typePool.indexOfTypeOf(items.get(position));
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
}

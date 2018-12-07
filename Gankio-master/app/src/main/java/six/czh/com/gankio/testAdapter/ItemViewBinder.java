package six.czh.com.gankio.testAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

/**
 * Created by czh on 18-12-6.
 * Email: caichelin@gmail.com
 */
public abstract class ItemViewBinder<T, VH extends RecyclerView.ViewHolder> {

    /**
     *
     * @param inflater
     * @param parent
     * @return
     */
    protected abstract @NotNull VH onCreateViewHolder(@NotNull LayoutInflater inflater, @NonNull ViewGroup parent);

    /**
     *
     * @param holder The ViewHolder
     * @param item The item within the Adapter's items data set.
     */
    protected abstract @NotNull void onBindViewHolder(@NonNull VH holder, @NotNull T item);


    /**
     * Get the current ViewHolder layout id.
     * @return The layout id
     */
    protected abstract int getLayoutId();

}

package app.melon.base.ui.lazyload;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * Taken from https://github.com/lianghanzhen/LazyViewPager
 */
public abstract class LazyViewPagerAdapter extends LazyPagerAdapter<View> {

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = getItem(container, position);
        mLazyItems.put(position, itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // we do not want to remove view at #position from container
    }

    @SuppressLint("DefaultLocale")
    private String makeTag(int position) {
        return String.format("Attach #%d to ViewPager", position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public View addLazyItem(ViewGroup container, int position) {
        View itemView = container.findViewWithTag(makeTag(position));
        if (itemView == null) {
            itemView = mLazyItems.get(position);
            itemView.setTag(makeTag(position));
            container.addView(itemView);
            mLazyItems.remove(position);
        }
        return itemView;
    }
}

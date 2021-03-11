package app.melon.base.lazyload;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Modify from https://github.com/lianghanzhen/LazyViewPager
 */
public abstract class LazyFragmentPagerAdapter extends LazyPagerAdapter<Fragment> {

    private static final String TAG = "LazyFragment";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public LazyFragmentPagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (DEBUG) {
                Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            }
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(container, position);
            if (fragment instanceof Laziable) {
                mLazyItems.put(position, fragment);
            } else {
                mCurTransaction.add(container.getId(), fragment, name);
            }
        }
        if (fragment != getCurrentItem()) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) {
            Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object + " v=" + ((Fragment) object).getView());
        }

        final long itemId = getItemId(position);
        final Fragment f = (Fragment) object;
        String name = makeFragmentName(container.getId(), itemId);
        if (mFragmentManager.findFragmentByTag(name) == null && f.isAdded()) {
            mCurTransaction.detach(f);
        } else {
            mLazyItems.remove(position);
        }
    }

    @Override
    public Fragment addLazyItem(ViewGroup container, int position) {
        Fragment fragment = mLazyItems.get(position);
        if (fragment == null) {
            return null;
        }

        final long itemId = getItemId(position);
        String name = makeFragmentName(container.getId(), itemId);
        if (mFragmentManager.findFragmentByTag(name) == null) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            mCurTransaction.add(container.getId(), fragment, name);
            mLazyItems.remove(position);
        }
        return fragment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (isLazyItem(position)) {
            startUpdate(container);
            addLazyItem(container, position);
            finishUpdate(container);
        }

        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentItem) {
            if (mCurrentItem != null) {
                mCurrentItem.setMenuVisibility(false);
                mCurrentItem.setUserVisibleHint(false);
            }

            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);

            mCurrentItem = fragment;
        }
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment) object).getView() == view;
    }

    public long getItemId(int position) {
        return position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    /**
     * mark the fragment can be added lazily
     */
    public interface Laziable {
    }
}

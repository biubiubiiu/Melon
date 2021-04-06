package app.melon.base.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Inspired by https://sriramramani.wordpress.com/2015/05/06/custom-viewgroups/
 */
public class ProfilePhotoLayout extends ViewGroup {
    private final ImageView mProfileImage;
    private final TextView mAuthorText;
    private final TextView mMessageText;
    private final TextView mPostTimeText;

    public ProfilePhotoLayout(Context context) {
        this(context, null);
    }

    public ProfilePhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfilePhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_profile, this, true);
        mProfileImage = findViewById(R.id.avatar);
        mAuthorText = findViewById(R.id.profile_username);
        mMessageText = findViewById(R.id.profile_school);
        mPostTimeText = findViewById(R.id.profile_post_time);
    }

    private void layoutView(View view, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;

        view.layout(leftWithMargins, topWithMargins,
                leftWithMargins + width, topWithMargins + height);
    }

    private int getWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredWidthImage(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int Size = MeasureSpec.getSize(widthMeasureSpec);

        int widthUsed = getPaddingLeft() + getPaddingRight();
        int heightUsed = getPaddingTop();

        measureChildWithMargins(mPostTimeText,
                widthMeasureSpec, widthUsed,
                getMeasuredHeightWithMargins(mPostTimeText), heightUsed);
        heightUsed += getPaddingTop() + getMeasuredHeightWithMargins(mPostTimeText);

        widthUsed += getMeasuredWidthWithMargins(mProfileImage);

        measureChildWithMargins(mProfileImage,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(mProfileImage);
        heightUsed += getMeasuredHeightWithMargins(mProfileImage);

        measureChildWithMargins(mAuthorText,
                getMeasuredWidthWithMargins(mAuthorText), 0,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mAuthorText);

        measureChildWithMargins(mMessageText,
                getMeasuredWidthWithMargins(mMessageText), 0,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mMessageText);


        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        int currentTop = 0;//paddingTop;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mPostTimeText.getLayoutParams();

        layoutView(mPostTimeText, 0, 0,
                mPostTimeText.getMeasuredWidth(),
                mPostTimeText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mPostTimeText);

        layoutView(mProfileImage, 0, currentTop,
                mProfileImage.getMeasuredWidth(),
                mProfileImage.getMeasuredHeight());

        final int contentLeft = getWidthWithMargins(mProfileImage);
        final int contentWidth = r - l - contentLeft - getPaddingRight();

        final MarginLayoutParams lp2 = (MarginLayoutParams) mProfileImage.getLayoutParams();

        currentTop += getHeightWithMargins(mProfileImage);

        layoutView(mAuthorText, 0, currentTop,
                mAuthorText.getMeasuredWidth(),
                mAuthorText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mAuthorText);

        layoutView(mMessageText, (getWidth() / 2) - (getMeasuredWidthWithMargins(mMessageText) / 2), currentTop,
                mMessageText.getMeasuredWidth(),
                mMessageText.getMeasuredHeight());
        //currentTop += getHeightWithMargins(mMessageText);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
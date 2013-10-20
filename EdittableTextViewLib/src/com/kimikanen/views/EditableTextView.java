package com.kimikanen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Displays text to the user and allows them to edit it.
 * 
 * <p>
 * <b>XML attributes</b>
 * <p>
 * See {@link R.styleable#EdittableTextView EdittableTextView Attributes},
 * {@link android.R.styleable#EditText View Attributes}
 * 
 * @attr ref R.styleable#EdittableTextView_showButton
 * @attr ref R.styleable#EdittableTextView_buttonEditDrawable
 * @attr ref R.styleable#EdittableTextView_buttonSaveDrawable
 * @attr ref R.styleable#EdittableTextView_editMode
 */
public class EditableTextView extends EditText {

	private final boolean showButton;
	private final boolean showBackgroundOnViewMode;
	private final Drawable editDrawable;
	private final Drawable saveDrawable;
	private final Drawable backgroundDrawable;
	private final KeyListener keyListener;

	private boolean editMode;
	private OnEditModeChangedListener onEditModeChangedListener;

	public EditableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		final TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.EditableTextView, 0,
				R.style.editabletextview_default_style);
		try {
			editMode = a.getBoolean(R.styleable.EditableTextView_editMode,
					false);
			showBackgroundOnViewMode = a
					.getBoolean(
							R.styleable.EditableTextView_showBackgroundOnViewMode,
							true);
			showButton = a.getBoolean(R.styleable.EditableTextView_showButton,
					true);
			editDrawable = a
					.getDrawable(R.styleable.EditableTextView_buttonEditDrawable);
			saveDrawable = a
					.getDrawable(R.styleable.EditableTextView_buttonSaveDrawable);
		} finally {
			a.recycle();
		}

		keyListener = getKeyListener();
		backgroundDrawable = getBackground();
		refreshState();
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean isEditMode) {
		this.editMode = isEditMode;

		refreshState();
		requestLayout();
		invalidate();
	}

	public void setOnEditModeChangedListener(
			OnEditModeChangedListener onEditModeChangedListener) {
		this.onEditModeChangedListener = onEditModeChangedListener;
	}

	private void refreshState() {
		setKeyListener(editMode ? keyListener : null);
		if (!showBackgroundOnViewMode) {
			setBackgroundDrawable(editMode ? backgroundDrawable : null);
		}
		setCursorVisible(editMode);
		if (showButton) {
			setCompoundDrawablesWithIntrinsicBounds(null, null,
					editMode ? saveDrawable : editDrawable, null);
		}
		if (editMode) {
			requestFocus();
		} else {
			clearFocus();
		}
	}

	private boolean isDrawableClicked(int actionX, int actionY) {
		boolean retval = false;
		if (showButton) {
			final Drawable drawableRight = isEditMode() ? saveDrawable
					: editDrawable;
			final Rect bounds = drawableRight.getBounds();

			int x, y;
			final int extraTapArea = 10;

			x = actionX + extraTapArea;
			y = actionY - extraTapArea;

			x = getWidth() - x;
			if (x <= 0) {
				x += extraTapArea;
			}

			if (y <= 0) {
				y = actionY;
			}
			retval = bounds.contains(x, y);
		}

		return retval;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int actionX = (int) event.getX();
		final int actionY = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isDrawableClicked(actionX, actionY)) {
				// return true here to avoid parent processing the event
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isDrawableClicked(actionX, actionY)) {
				setEditMode(!editMode);

				if (onEditModeChangedListener != null) {
					onEditModeChangedListener.onEditModeChanged(this, editMode);
				}
				event.setAction(MotionEvent.ACTION_CANCEL);
				return true;
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	public interface OnEditModeChangedListener {
		public void onEditModeChanged(EditableTextView view, boolean isEditMode);

	}
}

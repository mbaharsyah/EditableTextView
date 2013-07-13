package com.kimikanen.views.edittabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kimikanen.views.edittabletextviews.R;

public class EditableTextView extends FrameLayout {

	private static final String TAG = "EditableTextView";
	private static final float DEFAULT_TEXT_SIZE = 14.0f;

	private final EditText edittext;
	private final TextView textView;

	private boolean editMode;
	private String text;
	private float textSize;

	private final OnClickListener textViewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			editMode = true;
			init();
		}
	};

	private final OnKeyListener editTextKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (v.equals(edittext)) {
				switch (keyCode) {
				// if it's back don't save the new text
				case KeyEvent.KEYCODE_ENTER:
					text = edittext.getText().toString();
				case KeyEvent.KEYCODE_BACK:
					editMode = false;
					init();
					return true;
				default:
					break;
				}
			}
			return false;
		}
	};

	public EditableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EdittableTextView, 0, 0);
		text = a.getString(R.styleable.EdittableTextView_text);
		editMode = a.getBoolean(R.styleable.EdittableTextView_defaultEdit,
				false);
		textSize = a.getDimension(R.styleable.EdittableTextView_textSize,
				DEFAULT_TEXT_SIZE);

		a.recycle();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.editable_textview, this, true);

		textView = (TextView) findViewById(R.id.text);
		edittext = (EditText) findViewById(R.id.edit);

		textView.setOnClickListener(textViewClickListener);
		edittext.setOnKeyListener(editTextKeyListener);

		textView.setTextSize(textSize);
		edittext.setTextSize(textSize);

		init();
	}

	private void init() {
		Log.v(TAG, "Init. editMode = " + editMode);
		textView.setText(text);
		edittext.setText(text);
		if (editMode) {
			textView.setVisibility(View.INVISIBLE);
			edittext.setVisibility(View.VISIBLE);
			edittext.requestFocus();
			edittext.selectAll();
		} else {
			textView.setVisibility(View.VISIBLE);
			edittext.setVisibility(View.INVISIBLE);
			edittext.clearFocus();
		}
		invalidate();
		requestLayout();
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		init();
	}

	public float getTeextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
		init();
	}

}

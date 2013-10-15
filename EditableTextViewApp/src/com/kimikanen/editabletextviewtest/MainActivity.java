package com.kimikanen.editabletextviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kimikanen.views.EditableTextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditableTextView etv5 = (EditableTextView) findViewById(R.id.etv5);
		final Button btnToggle = (Button) findViewById(R.id.btn5);

		btnToggle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				etv5.setEditMode(!etv5.isEditMode());
			}
		});
	}

}

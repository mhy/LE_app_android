package com.mhy.lefinder.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class BaseDialog extends Dialog {

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(theme);
		setCanceledOnTouchOutside(false);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
	}

}

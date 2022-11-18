package com.luiztools.taskz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

	public static void alert(Context context, String mensagem){
		new AlertDialog.Builder(context)
		               .setMessage(mensagem)
		               .setPositiveButton("Ok", null)
		               .show();
	}
	
	public static void confirm(Context context, String mensagem, DialogInterface.OnClickListener yesClick){
		confirm(context, mensagem, yesClick, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	
	public static void confirm(Context context, String mensagem, DialogInterface.OnClickListener yesClick, DialogInterface.OnClickListener noClick){		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(mensagem)
		       .setCancelable(false)
		       .setPositiveButton("Sim", yesClick)
		       .setNegativeButton("Não", noClick);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
}

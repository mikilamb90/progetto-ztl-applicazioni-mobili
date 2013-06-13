package ztl.Bologna.activity.ztlbolognaActivity;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;




public class DialogActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);

		final Context context = this;
		/*custom alert dialog*/
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Ztl Alert");
		alertDialog.setMessage("Accesso zona ztl non puoi accedere");
		AlertDialog alert = alertDialog.create();

		alert.setButton("Chiudi", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// chiusura del dialog e dell'activity trasparente
				dialog.dismiss();
				finish();
			}
		});
		alert.show();







		/*	public void creaAlertDialog(final Context C){
		Log.e("new ","new");
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(C);
	 Toast.makeText(getApplicationContext(), "ciao", Toast.LENGTH_LONG);
    // Setting Dialog Title
    alertDialog.setTitle("Ztl Alert");

    // Setting Dialog Message
    alertDialog.setMessage("Accesso Ztl non puoi accedere");


    // Setting Negative "NO" Button
    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to invoke NO event
        dialog.cancel();
        }
    });
	}*/
	}

}

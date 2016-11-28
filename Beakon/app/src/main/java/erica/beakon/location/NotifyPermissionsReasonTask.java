package erica.beakon.location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.URL;

import erica.beakon.MainActivity;


public class NotifyPermissionsReasonTask extends AsyncTask<String, Integer[], Integer> {

    Context context;
    DialogInterface.OnClickListener negativeListener;
    DialogInterface.OnClickListener positiveListener;
    String permission;
    AlertDialog.Builder alertDialog;

    public NotifyPermissionsReasonTask(Context context, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener positiveListener) {
        super();
        this.context = context;
        this.negativeListener = negativeListener;
        this.positiveListener = positiveListener;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(this.context);
    }

    protected Integer doInBackground(String[] args) {
        return 1;
    }

    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        showConfirmationAlertDialog();
    }

    private void showConfirmationAlertDialog() {
        alertDialog.setTitle("Location Permissions");
        alertDialog.setMessage("In order to receive location based permissions, you must allow Beakon access to your location. Do you still want to deny permission?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("No", this.positiveListener);
        alertDialog.setNegativeButton("Yes", this.negativeListener);
        alertDialog.show();
    }

}

package masonemobile.restaurantlocator.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.List;

import masonemobile.restaurantlocator.modules.models.Category;
import masonemobile.restaurantlocator.R;

public class Utilities {

    private static volatile Utilities sSoleInstance = new Utilities();

    //private constructor.
    private Utilities(){}

    public static Utilities getInstance() {
        return sSoleInstance;
    }

    public void showAlertView(Context context, String message, String firstButton, String secondButton, DialogInterface.OnClickListener firstOnClickListener, DialogInterface.OnClickListener secondOnClickListener){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Notification");
        alertDialog.setMessage(message);

        if (firstButton != null && secondButton != null) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, firstButton, firstOnClickListener);

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, secondButton, secondOnClickListener);
        } else {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }

        alertDialog.show();
    }

    public String stringArrayToString(String initail, List<String> list, String seperator){
        StringBuilder stringBuilder = new StringBuilder(initail);
        for (int index = 0; index<list.size();index++)  {
            stringBuilder.append(list.get(index));
            if (index < list.size()-1) {
                stringBuilder.append(seperator);
            }
        }
        return stringBuilder.toString();
    }

    public String stringArrayToStringCategory(String initail, List<Category> list, String seperator){
        StringBuilder stringBuilder = new StringBuilder(initail);
        for (int index = 0; index<list.size();index++)  {
            stringBuilder.append(list.get(index).title);
            if (index < list.size()-1) {
                stringBuilder.append(seperator);
            }
        }
        return stringBuilder.toString();
    }
}

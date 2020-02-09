package com.example.womensaftey;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SimDialog extends DialogFragment {

    RadioGroup simrdogp;

    public static final String MY_PREFS_FILENAME = "com.example.womensaftey.myfile";
    Context context;


    public SimDialog(Context context){
        this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = context.getSharedPreferences(MY_PREFS_FILENAME, Context.MODE_PRIVATE);
        int slot = preferences.getInt("slot", 0);

        if (slot == 0) {
            simrdogp.check(R.id.sim1);
        } else if(slot==1) {
            simrdogp.check(R.id.sim2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.aboutdialog_custom_layout,null);

        simrdogp=view.findViewById(R.id.simrdogp);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Choose");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id=simrdogp.getCheckedRadioButtonId();

                if(id==R.id.sim2){
                    SharedPreferences.Editor editor = context.
                            getSharedPreferences(MY_PREFS_FILENAME, Context.MODE_PRIVATE).edit();
                    editor.putInt("slot", 1);
                    editor.commit();
                }
                else if(id==R.id.sim1){
                    SharedPreferences.Editor editor = context.
                            getSharedPreferences(MY_PREFS_FILENAME, Context.MODE_PRIVATE).edit();
                    editor.putInt("slot", 0);
                    editor.commit();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.setCancelable(false);


        return builder.create();
    }
}

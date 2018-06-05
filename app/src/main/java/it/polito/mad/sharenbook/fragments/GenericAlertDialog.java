package it.polito.mad.sharenbook.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import it.polito.mad.sharenbook.MyBookActivity;
import it.polito.mad.sharenbook.R;


public class GenericAlertDialog extends DialogFragment {

    public static GenericAlertDialog newInstance(int title, String fragMes) {
        GenericAlertDialog frag = new GenericAlertDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("fragMessage", fragMes);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        String fragMessage = getArguments().getString("fragMessage");
        String username = getArguments().getString("username");
        String tag = getTag();

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle(title)
                .setMessage(fragMessage)
                .setPositiveButton(R.string.confirm,
                        (dialog, whichButton) -> {
                            Activity activity = getActivity();
                            if(activity instanceof MyBookActivity && tag.equals("undo_borrow_dialog")) {
                                ((RequestsFragment)((MyBookActivity)activity).mSectionsPagerAdapter.getCurrentFragment()).undoRequest();
                            }
                        }
                )
                .setNegativeButton(R.string.undo, (dialog, whichButton) -> dialog.dismiss())
                .create();
    }

}

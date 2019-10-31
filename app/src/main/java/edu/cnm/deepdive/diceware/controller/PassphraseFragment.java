package edu.cnm.deepdive.diceware.controller;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.diceware.R;
import edu.cnm.deepdive.diceware.model.Passphrase;

public class PassphraseFragment extends DialogFragment {

  private OnCompleteListener listener;
  private Passphrase passphrase;

  public static PassphraseFragment newInstance() {
    return newInstance(null);
  }

  public static PassphraseFragment newInstance(Passphrase passphrase) {
    PassphraseFragment fragment = new PassphraseFragment();
    Bundle args = new Bundle();
    if (passphrase != null) {
      args.putSerializable("passphrase", passphrase);
    }
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Passphrase temp = (Passphrase) getArguments().getSerializable("passphrase");
    Passphrase passphrase = (temp != null) ? temp : new Passphrase();
    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_passphrase, null);
    EditText passphraseKey = view.findViewById(R.id.passphrase_key);
    if (passphrase.getKey() != null) {
      passphraseKey.setText(passphrase.getKey());
    }
    return new AlertDialog.Builder(getContext())
        .setTitle("Passphrase Details")
        .setView(view)
        .setNegativeButton("Cancel", (dialog, button) -> {})
        .setPositiveButton("Ok", (dialog, button) -> {
          if (listener != null) {
            passphrase.setKey(passphraseKey.getText().toString());
            listener.complete(passphrase);
          }
        })
        .create();
  }

  public void setListener(OnCompleteListener listener) {
    this.listener = listener;
  }

  @FunctionalInterface
  public interface OnCompleteListener {

    void complete(Passphrase passphrase);

  }

}

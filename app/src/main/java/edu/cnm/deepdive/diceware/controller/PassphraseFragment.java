package edu.cnm.deepdive.diceware.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.diceware.R;
import edu.cnm.deepdive.diceware.model.Passphrase;
import java.util.Arrays;

public class PassphraseFragment extends DialogFragment {

  private Passphrase passphrase;
  private EditText passphraseKey;
  private EditText passphraseWords;

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
    passphrase = (temp != null) ? temp : new Passphrase();
    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_passphrase, null);
    passphraseKey = view.findViewById(R.id.passphrase_key);
    passphraseWords = view.findViewById(R.id.passphrase_words);
    if (savedInstanceState == null) {
      populateFields();
    }
    return new Builder(getContext())
        .setTitle(getContext().getString(R.string.passphrase_details))
        .setView(view)
        .setNegativeButton(getContext().getString(R.string.cancel), (dialog, button) -> {})
        .setPositiveButton(getContext().getString(R.string.ok),
            (dialog, button) -> populatePassphrase())
        .create();
  }

  private void populatePassphrase() {
    passphrase.setKey(passphraseKey.getText().toString().trim());
    String words = passphraseWords.getText().toString().trim();
    if (!words.isEmpty()) {
      passphrase.setWords(Arrays.asList(words.split("\\s+")));
    } else {
      passphrase.setWords(null);
    }
    ((OnCompleteListener) getActivity()).complete(passphrase);
  }

  private void populateFields() {
    if (passphrase.getKey() != null) {
      passphraseKey.setText(passphrase.getKey());
    }
    if (passphrase.getWords() != null) {
      String words = passphrase.getWords().toString();
      passphraseWords.setText(words
          .replaceAll("^\\[|\\]$", "")
          .trim()
          .replaceAll("\\s*,\\s+", " "));
    }
  }

  @FunctionalInterface
  public interface OnCompleteListener {

    void complete(Passphrase passphrase);

  }

}

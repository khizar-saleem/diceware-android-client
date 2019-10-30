package edu.cnm.deepdive.diceware.controller;


import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.diceware.model.Passphrase;

public class PassphraseFragment extends DialogFragment {

  public static PassphraseFragment newInstance(Passphrase passphrase) {
    PassphraseFragment fragment = new PassphraseFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }
}

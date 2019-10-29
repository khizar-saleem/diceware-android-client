package edu.cnm.deepdive.diceware.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.diceware.R;
import edu.cnm.deepdive.diceware.model.Passphrase;
import edu.cnm.deepdive.diceware.service.DicewareService;
import edu.cnm.deepdive.diceware.view.PassphraseAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

  private final MutableLiveData<List<Passphrase>> passphrases =
      new MutableLiveData<>();
  private final MutableLiveData<GoogleSignInAccount> account =
      new MutableLiveData<>();

  public MainViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<List<Passphrase>> getPassphrases() {
    return passphrases;
  }

  public void setAccount(GoogleSignInAccount account) {
    this.account.setValue(account);
    if (account != null) {
      String token = getApplication().getString(R.string.oauth_header, account.getIdToken());
      Log.d("Oauth2.0 token", token); // FIXME Remove before shipping.
      DicewareService.getInstance().getAll(token)
          .subscribeOn(Schedulers.io())
          .subscribe((passphrases) -> {
            this.passphrases.postValue(passphrases);
          });
    } else {
      passphrases.setValue(Collections.emptyList());
    }
  }
}

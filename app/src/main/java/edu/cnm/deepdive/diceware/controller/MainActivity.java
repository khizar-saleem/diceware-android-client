package edu.cnm.deepdive.diceware.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import edu.cnm.deepdive.diceware.R;
import edu.cnm.deepdive.diceware.service.DicewareService;
import edu.cnm.deepdive.diceware.service.GoogleSignInService;
import edu.cnm.deepdive.diceware.view.PassphraseAdapter;
import edu.cnm.deepdive.diceware.viewmodel.MainViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  private ProgressBar waiting;
  private RecyclerView passphraseList;
  private MainViewModel viewModel;
  private GoogleSignInService signInService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupViewModel();
    setupSignIn();
  }

  private void setupViewModel() {
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    viewModel.getPassphrases().observe(this, (passphrases) -> {
      PassphraseAdapter adapter = new PassphraseAdapter(this, passphrases,
          (view, position, passphrase) -> {
            Log.d("Passphrase click", passphrase.getKey());
            PassphraseFragment fragment = PassphraseFragment.newInstance(passphrase);
            fragment.setListener((p) -> {
              waiting.setVisibility(View.VISIBLE);
              viewModel.updatePassphrase(p);
            });
            fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
          },
          (menu, position, passphrase) -> {
            Log.d("Passphrase context", passphrase.getKey());
            getMenuInflater().inflate(R.menu.passphrase_context, menu);
            menu.findItem(R.id.delete_passphrase).setOnMenuItemClickListener(
                (item) -> {
                  Log.d("Delete selected", passphrase.getKey());
                  waiting.setVisibility(View.VISIBLE);
                  refreshSignIn(() -> viewModel.deletePassphrase(passphrase));
                  return true;
                });
          });
      passphraseList.setAdapter(adapter);
      waiting.setVisibility(View.GONE);
    });
    viewModel.getThrowable().observe(this, (throwable) -> {
      if (throwable != null) {
        waiting.setVisibility(View.GONE);
        Toast.makeText(this,
            String.format("Connection to server failed: %s", throwable.getMessage()),
            Toast.LENGTH_LONG).show();
      }
    });
  }

  private void setupSignIn() {
    signInService = GoogleSignInService.getInstance();
    signInService.getAccount().observe(this, (account) ->
        viewModel.setAccount(account));
  }

  private void setupUI() {
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(view -> {
      PassphraseFragment fragment = PassphraseFragment.newInstance();
      fragment.setListener((passphrase) -> {
        waiting.setVisibility(View.VISIBLE);
        viewModel.addPassphrase(passphrase);
      });
      fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
    });
    waiting = findViewById(R.id.waiting);
    passphraseList = findViewById(R.id.keyword_list);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.refresh:
        refreshSignIn(() -> viewModel.refreshPassphrases());
        break;
      case R.id.action_settings:
        break;
      case R.id.sign_out:
        signOut();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void signOut() {
    signInService.signOut()
        .addOnCompleteListener((task) -> {
          Intent intent = new Intent(this, LoginActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        });
  }

  private void refreshSignIn(Runnable runnable) {
    signInService.refresh()
        .addOnSuccessListener((account) -> runnable.run())
        .addOnFailureListener((e) -> signOut());
  }

}

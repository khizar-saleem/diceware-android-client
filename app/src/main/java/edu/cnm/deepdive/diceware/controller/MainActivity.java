package edu.cnm.deepdive.diceware.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
    RecyclerView passphraseList = findViewById(R.id.keyword_list);
    MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    GoogleSignInService.getInstance().getAccount().observe(this, (account) ->
        viewModel.setAccount(account));
    viewModel.getPassphrases().observe(this, (passphrases) -> {
      PassphraseAdapter adapter = new PassphraseAdapter(this, passphrases,
          (view, position, passphrase) -> {
            //TODO Add code to pop up editor.
            Log.d("Passphrase click", passphrase.getKey());
          },
          (menu, position, passphrase) -> {
            Log.d("Passphrase context", passphrase.getKey());
            getMenuInflater().inflate(R.menu.passphrase_context, menu);
            menu.findItem(R.id.delete_passphrase).setOnMenuItemClickListener(
                (item) -> {
                  Log.d("Delete selected", passphrase.getKey());
                  // TODO Send request to server to delete passphrase; refresh view.
                  return true;
                });
          });
      passphraseList.setAdapter(adapter);
    });
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
      case R.id.action_settings:
        break;
      case R.id.sign_out:
        GoogleSignInService.getInstance().signOut()
            .addOnCompleteListener((task) -> {
              Intent intent = new Intent(this, LoginActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
            });
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }
}

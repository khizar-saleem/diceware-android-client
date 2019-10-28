package edu.cnm.deepdive.diceware;

import android.app.Application;
import edu.cnm.deepdive.diceware.service.GoogleSignInService;

public class DicewareApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setApplicationContext(this);
  }
}

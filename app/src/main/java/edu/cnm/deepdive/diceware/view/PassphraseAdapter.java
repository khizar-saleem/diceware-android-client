package edu.cnm.deepdive.diceware.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.diceware.R;
import edu.cnm.deepdive.diceware.model.Passphrase;
import edu.cnm.deepdive.diceware.view.PassphraseAdapter.Holder;
import java.util.List;

public class PassphraseAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final List<Passphrase> passphrases;

  public PassphraseAdapter(Context context,
      List<Passphrase> passphrases) {
    this.context = context;
    this.passphrases = passphrases;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.passphrase_item, parent, false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    Passphrase passphrase = passphrases.get(position);
    holder.bind(passphrase);
  }

  @Override
  public int getItemCount() {
    return passphrases.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final View view;

    private Holder(@NonNull View itemView) {
      super(itemView);
      view = itemView;
    }

    private void bind(Passphrase passphrase) {
      ((TextView) view).setText(passphrase.getKey());
    }

  }

}

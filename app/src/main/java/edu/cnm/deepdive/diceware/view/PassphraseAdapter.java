package edu.cnm.deepdive.diceware.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
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
  private final OnPassphraseClickListener clickListener;
  private final OnPassphraseContextClickListener contextClickListener;

  public PassphraseAdapter(Context context,
      List<Passphrase> passphrases,
      OnPassphraseClickListener clickListener,
      OnPassphraseContextClickListener contextClickListener) {
    this.context = context;
    this.passphrases = passphrases;
    this.clickListener = clickListener;
    this.contextClickListener = contextClickListener;
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
    holder.bind(position, passphrase);
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

    private void bind(int position, Passphrase passphrase) {
      ((TextView) view).setText(passphrase.getKey());
      if (clickListener != null) {
        view.setOnClickListener((v) -> clickListener.click(v, position, passphrase));
      }
      if (contextClickListener != null) {
        view.setOnCreateContextMenuListener((menu, v, menuInfo) ->
            contextClickListener.click(menu, position, passphrase));
      }
    }

  }

  @FunctionalInterface
  public interface OnPassphraseClickListener {

    void click(View view, int position, Passphrase passphrase);

  }

  @FunctionalInterface
  public interface OnPassphraseContextClickListener {

    void click(Menu menu, int position, Passphrase passphrase);
  }

}

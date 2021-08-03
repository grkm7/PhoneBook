package com.info.kisileruygulamasi;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu> {
    private Context mContext;
    private List<Kisiler> kisilerListe;
    private DatabaseReference myRef;

    public KisilerAdapter(Context mContext, List<Kisiler> kisilerListe, DatabaseReference myRef) {
        this.mContext = mContext;
        this.kisilerListe = kisilerListe;
        this.myRef = myRef;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_card_tasarim,parent,false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardTasarimTutucu holder, int position) {
        final Kisiler kisi = kisilerListe.get(position);

        holder.textViewKisiBilgi.setText(kisi.getKisi_ad()+" - "+kisi.getKisi_tel());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.action_Sil:
                                Snackbar.make(holder.imageViewNokta,"Kişi Silinsin mi?",Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                myRef.child(kisi.getKisi_id()).removeValue();
                                            }
                                        })


                                        .show();
                                return  true;
                            case R.id.action_guncelle:
                                alertGoster(kisi);
                                return true;
                            default:
                                return false;

                        }
                    }
                });


                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return kisilerListe.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder{
        private TextView textViewKisiBilgi;
        private ImageView imageViewNokta;

        public CardTasarimTutucu(@NonNull View itemView) {

            super(itemView);
            textViewKisiBilgi = itemView.findViewById(R.id.textViewKisiBilgi);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);
        }
    }
    public void alertGoster(final Kisiler kisi){
        LayoutInflater layout = LayoutInflater.from(mContext);
        View tasarim = layout.inflate(R.layout.alert_tasarim,null);


        final EditText editTextAd = tasarim.findViewById(R.id.editTextAd);
        final EditText editTextTel = tasarim.findViewById(R.id.editTextTel);

        editTextAd.setText(kisi.getKisi_ad());
        editTextTel.setText(kisi.getKisi_tel());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Kişi Güncelle");
        ad.setView(tasarim);
        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String kisi_Ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                Map<String,Object> bilgier = new HashMap<>();
                bilgier.put("kisi_ad",kisi_Ad);
                bilgier.put("kisi_tel",kisi_tel);
                myRef.child(kisi.getKisi_id()).updateChildren(bilgier);

            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.create().show();

    }
}

package com.thiagocme.testesdroid;

import android.content.DialogInterface;
import android.nfc.NfcAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NFCActivity extends AppCompatActivity {

    private NfcAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nfc);

        //Identifica os controles do XML e instancia
        Button botao = (Button) findViewById(R.id.finalButton);
        final EditText texto =  (EditText) findViewById(R.id.texto);

        adapter = NfcAdapter.getDefaultAdapter(this);

        if (adapter == null)
        {
            AlertDialog.Builder NFCalert = new AlertDialog.Builder(this);

            NFCalert.setTitle("NFC Status");
            NFCalert.setMessage("NFC não está disponível nesse dispositivo");
            NFCalert.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            NFCalert.show();
        }
        else
        {
            AlertDialog.Builder NFCalert = new AlertDialog.Builder(this);

            NFCalert.setTitle("NFC Status");
            NFCalert.setMessage("NFC disponível!");
            NFCalert.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            NFCalert.show();
        }

        //Cria uma "escuta" no botão, para executar ações ao clickarem nele
        botao.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Cria uma caixa de diálogo e seta configurações
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NFCActivity.this);

                alertDialogBuilder.setTitle("O botão foi clicado.");
                alertDialogBuilder.setMessage("O texto digitado foi: " + texto.getText());
                alertDialogBuilder.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //Mostra caixa de diálogo
                alertDialogBuilder.show();

                //Pega e configura a data atual
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date hora = Calendar.getInstance().getTime();
                String dataFormatada = sdf.format(hora);

                final TextView descresultado = (TextView) findViewById(R.id.descresult);
                final TextView resultado = (TextView) findViewById(R.id.result);

                //Exibe, na form do aplicativo, a última mensagem enviada e horário
                descresultado.setText("O último texto enviado foi:");
                resultado.setText(texto.getText() + " as " + dataFormatada);

            }
        });
    }
}

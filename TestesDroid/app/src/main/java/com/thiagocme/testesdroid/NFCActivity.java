package com.thiagocme.testesdroid;

import android.content.DialogInterface;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NFCActivity extends AppCompatActivity {

    private NfcAdapter adapter;
    public Tag mytag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nfc);

        //Identifica os controles do XML e instancia
        Button botao = (Button) findViewById(R.id.finalButton);
        final EditText texto =  (EditText) findViewById(R.id.texto);

        //Define o dispositivo como o utilizador do NFC
        adapter = NfcAdapter.getDefaultAdapter(this);

        //Verifica se o adapter (dispositivo) tem NFC
        if (adapter == null)
        {
            //Constrói dialogo de alerta para caso o dispositivo NÃO tenha NFC
            AlertDialog.Builder NFCalert = new AlertDialog.Builder(this);

            NFCalert.setTitle("NFC Status");
            NFCalert.setMessage("NFC não está disponível nesse dispositivo");
            NFCalert.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            //Exibe alerta para o usuário
            NFCalert.show();

            botao.setEnabled(false);
        }
        else
        {
            //Constrói dialogo de alerta para caso o dispositivo tenha NFC
            AlertDialog.Builder NFCalert = new AlertDialog.Builder(this);

            NFCalert.setTitle("NFC Status");
            NFCalert.setMessage("NFC disponível!");
            NFCalert.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            //Exibe alerta para o usuário
            NFCalert.show();
        }

        //Cria uma "escuta" no botão, para executar ações ao clickarem nele
        botao.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try
                {

                    //LINHA DA ESCRITA????????????
                    write(texto.getText().toString(), mytag);

                    
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
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (FormatException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /*public static NdefRecord createTextRecord(String language, String text) {
        byte[] languageBytes;
        byte[] textBytes;
        try {
            languageBytes = language.getBytes("US-ASCII");
            textBytes = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }

        byte[] recordPayload = new byte[1 + (languageBytes.length & 0x03F) + textBytes.length];

        recordPayload[0] = (byte)(languageBytes.length & 0x03F);
        System.arraycopy(languageBytes, 0, recordPayload, 1, languageBytes.length & 0x03F);
        System.arraycopy(textBytes, 0, recordPayload, 1 + (languageBytes.length & 0x03F), textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, recordPayload);
    }*/


    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    private void write(String text, Tag tag) throws IOException, FormatException
    {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage msg = new NdefMessage(records);
        Ndef formatador = Ndef.get(tag);
        formatador.connect();
        formatador.writeNdefMessage(msg);
        formatador.close();
    }
}

package ar.edu.utn.frsf.isi.dam.mgl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener, View.OnFocusChangeListener {

    private Double capital;
    private EditText importe;
    private TextView pesosRetorno;
    private Double dias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boton = (Button)findViewById(R.id.button);
        boton.setOnClickListener(this);

        SeekBar barra = (SeekBar) findViewById(R.id.seekBarDias);
        barra.setOnSeekBarChangeListener(this);

        importe =(EditText) findViewById(R.id.editMonto);
        importe.setOnFocusChangeListener(this);

        pesosRetorno = (TextView) findViewById(R.id.pesosRetorno);

        dias=1.0;
        capital=0.0;

    }

    public void onClick(View v){
        EditText email = (EditText) findViewById(R.id.editEmail);
        String strEmail = email.getText().toString();

        EditText cuit =(EditText) findViewById(R.id.editcuit);
        String strCuit= cuit.getText().toString();

        String strImporte= importe.getText().toString();
        if(strImporte.equals("")){
            return ;
        }
        TextView mensaje = (TextView) findViewById(R.id.mensaje);

        capital = Double.parseDouble(strImporte);

        if(strEmail.equals("") || strCuit.equals("") || strImporte.equals("") || capital<=0 ){
            mensaje.setText("Plazo fijo no realizado");
            int rojo = getResources().getColor(R.color.rojo);
            mensaje.setTextColor(rojo);
            mensaje.setVisibility(View.VISIBLE);
        }
        else{
            String strPesosRetorno= pesosRetorno.getText().toString();

            mensaje.setText("Plazo fijo realizado. "+"Recibirá "+ strPesosRetorno +" al vencimiento!");
            int verde = getResources().getColor(R.color.verde);
            mensaje.setTextColor(verde);
            mensaje.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView diasSeleccionados = (TextView) findViewById(R.id.txtDiasSeleccionados);
        diasSeleccionados.setText(String.valueOf(progress));
        diasSeleccionados.setVisibility(View.VISIBLE);

        dias= (double) progress;
        if (capital!=null || capital !=0 ) {
            Double retorno = calcularRetorno(capital, (double)progress);
            pesosRetorno.setText(String.valueOf(retorno));
            pesosRetorno.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private Double calcularRetorno(Double importe, Double dias) {
        Double tasa;

        double tasa1 = Double.parseDouble(getResources().getString(R.string.tasa1));
        double tasa2 = Double.parseDouble(getResources().getString(R.string.tasa2));
        double tasa3 = Double.parseDouble(getResources().getString(R.string.tasa3));
        double tasa4 = Double.parseDouble(getResources().getString(R.string.tasa4));
        double tasa5 = Double.parseDouble(getResources().getString(R.string.tasa5));
        double tasa6 = Double.parseDouble(getResources().getString(R.string.tasa6));

        double monto0 = Double.parseDouble(getResources().getString(R.string.monto0));
        double monto5000 = Double.parseDouble(getResources().getString(R.string.monto5000));
        double monto99999 = Double.parseDouble(getResources().getString(R.string.monto99999));

        Double cant_dias_tasa = Double.parseDouble(getResources().getString(R.string.cant_dias_tasa));

        if(importe<=monto5000 && dias<cant_dias_tasa)
            tasa=tasa1;
        else if (importe<=monto5000 && dias>=cant_dias_tasa)
            tasa=tasa2;
        else if (importe<=monto99999 && dias<cant_dias_tasa)
            tasa=tasa3;
        else if (importe<=monto99999 && dias>=cant_dias_tasa)
            tasa=tasa4;
        else if (importe>monto99999 && dias<cant_dias_tasa)
            tasa=tasa5;
        else if (importe>monto99999 && dias>=cant_dias_tasa)
            tasa=tasa6;
        else  tasa=0.0;

        return Math.rint((importe * (Math.pow( 1+(tasa) , (dias/360)) - 1))*100)/100;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            String strImporte= importe.getText().toString();
            if(strImporte.equals("")){
                return ;
            }
            capital = Double.parseDouble(strImporte);
            Double retorno = calcularRetorno(capital, dias);
            pesosRetorno.setText(String.valueOf(retorno));
            pesosRetorno.setVisibility(View.VISIBLE);
        }
    }
}

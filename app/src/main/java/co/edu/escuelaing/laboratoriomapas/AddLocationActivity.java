package co.edu.escuelaing.laboratoriomapas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.*;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddLocationActivity extends AppCompatActivity {

    private TextInputLayout textedit1;
    private TextInputLayout textedit2;
    private TextInputLayout textedit3;
    private TextInputLayout textedit4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        Intent intent = getIntent();
        textedit1 = findViewById(R.id.text1);
        textedit2 = findViewById(R.id.text2);
        textedit3 = findViewById(R.id.text3);
        textedit4 = findViewById(R.id.text4);

        final Button button3 = findViewById(R.id.button);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addLocation();
            }
        });

    }


    public boolean verify(){
        boolean ban=false;
        if(textedit1.getEditText().toString()=="" || textedit2.getEditText().toString()=="" || textedit3.getEditText().toString()=="" || textedit4.getEditText().toString()==""){
            textedit1.setError("Complete all inputs");
            textedit2.setError("Complete all inputs");
            textedit3.setError("Complete all inputs");
            textedit4.setError("Complete all inputs");
            ban=true;
        }
        return ban;
    }


    public void addLocation(){
        if(!verify()){
            Intent intent = new Intent( this, MapsActivity.class );
            String name=textedit1.getEditText().getText().toString();
            String des=textedit2.getEditText().getText().toString();
            Double latitude = Double.parseDouble(textedit3.getEditText().getText().toString());
            Double longitude = Double.parseDouble(textedit4.getEditText().getText().toString());
            intent.putExtra( "newLocation", new LocationLocal(name,des,latitude,longitude));
            startActivity( intent );
        }
    }
}

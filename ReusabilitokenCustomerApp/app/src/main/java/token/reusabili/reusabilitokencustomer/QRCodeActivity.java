package token.reusabili.reusabilitokencustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import token.reusabili.reusabilitokencustomer.barcode.BarcodeCaptureActivity;


public class QRCodeActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private static final String FILENAME = "ReusabiliStore";
    private static final String VAL_KEY = "ADDRESS";
    private String cashierProof;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Intent data = getIntent();
        String type = data.getExtras().getString("type");
        final SharedPreferences sharedPrefs = getSharedPreferences(FILENAME, 0);
        String address = sharedPrefs.getString(VAL_KEY, null);

        final TextView caddress;
        caddress = (TextView) findViewById(R.id.cadress);
        caddress.setText(address);

        TextView atoclaim;
        atoclaim = (TextView) findViewById(R.id.atoclaim);
        atoclaim.setText(type);


        String toSend = type + " " + address;
        iv = (ImageView) findViewById(R.id.imageView);


        try {
            bitmap = TextToImageEncode(toSend);
            iv.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Button scan_cashier = findViewById(R.id.scan_cashier);
        scan_cashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                changeToBarcodeScan();
            }
        });
    }


    private void changeToBarcodeScan() {
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);

    }
    private void changeToClaim() {
        Intent returner = new Intent(this, ClaimActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("proof", cashierProof);
        returner.putExtras(bundle);
        startActivity(returner);
    }


    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack) : getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    cashierProof = barcode.displayValue;
                }
            } else
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
        } else
            super.onActivityResult(requestCode, resultCode, data);
        changeToClaim();
    }
}
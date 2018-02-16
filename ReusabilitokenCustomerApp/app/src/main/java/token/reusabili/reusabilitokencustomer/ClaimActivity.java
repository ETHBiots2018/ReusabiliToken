package token.reusabili.reusabilitokencustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Features.operations.HumanConfirmableActionProof;
import Features.operations.HumanConfirmableClaim;
import Features.operations.HumanConfirmableOperation;
import Features.operations.actions.AHumanConfirmableAction;
import Features.operations.actions.IAction;
import tests.Address;
import tests.DummyRepo;
import tests.DummyValueToken;

public class ClaimActivity extends AppCompatActivity {

    private static final String FILENAME = "ReusabiliStore";
    private static final String VAL_KEY = "ADDRESS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        final SharedPreferences sharedPrefs = getSharedPreferences(FILENAME, 0);
        String address = sharedPrefs.getString(VAL_KEY, null);
        final Address customersAddress = new Address(address);

        MainActivity.repo = new DummyRepo();
        MainActivity.hco = new HumanConfirmableOperation(MainActivity.repo);
        final List<IAction> actions= MainActivity.hco.getActions();
        final String[] ids = new String[actions.size()];
        String[] descriptions = new String[actions.size()];
        for (int i = 0; i < actions.size(); i++) {
            descriptions[i]=((AHumanConfirmableAction)actions.get(i)).getDescription();
            ids[i]=((AHumanConfirmableAction)actions.get(i)).getType().toString();

        }

        ListView listView1 = (ListView) findViewById(R.id.listView1);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, descriptions);

        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                MainActivity.choosenAction = (AHumanConfirmableAction) actions.get((int)id);
                MainActivity.choosenAction.setCustomerAddress(customersAddress);
                changeToQrMake(ids[(int)id]);
            }
        });


        Intent data = getIntent();
        Bundle bundle = data.getExtras();

        if(bundle!=null)
        {
            try {
                String proof = bundle.getString("proof");
                String storeidStr = proof.split(" ")[0];
                String signatureStr = proof.substring(storeidStr.length() + 1);
                long storeid = Long.parseLong(storeidStr);
                byte[] signature = signatureStr.getBytes();
                MainActivity.choosenAction.setStoreID(2);
                HumanConfirmableActionProof actionProof = new HumanConfirmableActionProof(signature);
                MainActivity.claimer = new HumanConfirmableClaim(MainActivity.choosenAction, actionProof);
                MainActivity.hco.write(new DummyValueToken(MainActivity.repo), MainActivity.claimer);
                Toast.makeText(getApplicationContext(), "You Claimed " + MainActivity.choosenAction.getType().toString(), Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Something went wrong, maybe false QR-Code scanned!", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void changeToQrMake(String id) {
        Intent qrCode = new Intent(this, QRCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", id);
        qrCode.putExtras(bundle);
        startActivity(qrCode);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        final SharedPreferences sharedPrefs = getSharedPreferences(FILENAME, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(VAL_KEY, null);
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
    }
}

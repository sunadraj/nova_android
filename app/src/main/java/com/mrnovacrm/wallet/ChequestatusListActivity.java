package com.mrnovacrm.wallet;

//public class ChequestatusListActivity {
//}

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.WalletCreditDetailsDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChequestatusListActivity extends AppCompatActivity implements View.OnTouchListener{

    RecyclerView recyclerView;
    private String titleval;
    private String PRIMARYID;
    private String SHORTFORM;
    Context mContext;
    public static EditText edtxt_fromdate, edtxt_todate;
    ImageView search;
    private SimpleDateFormat dfDate;
    private RelativeLayout imgrel;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        titleval = bundle.getString("title");
        setTitle(titleval);
        setContentView(R.layout.layout_creditlist);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        search = findViewById(R.id.search);
        imgrel = findViewById(R.id.imgrel);
        imageview = findViewById(R.id.imageview);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        edtxt_fromdate = findViewById(R.id.edtxt_fromdate);
        edtxt_todate = findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(ChequestatusListActivity.this);
        edtxt_todate.setOnTouchListener(ChequestatusListActivity.this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String FROMDATE=edtxt_fromdate.getText().toString().trim();
                String TODATE=edtxt_todate.getText().toString().trim();

              //  dfDate = new SimpleDateFormat("yyyy-MM-dd");
                dfDate = new SimpleDateFormat("dd-MM-yyyy");
                if(FROMDATE.equals("")|| FROMDATE.equals(null) || TODATE.equals("")|| TODATE.equals(null))
                {
                    Toast.makeText(getApplicationContext(),"Please enter from date and to date",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                            boolean isConnectedToInternet = CheckNetWork
                                    .isConnectedToInternet(getApplicationContext());
                            if(isConnectedToInternet)
                            {
                                getChequeList();
                            }else{
                                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                            }
                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {

                            boolean isConnectedToInternet = CheckNetWork
                                    .isConnectedToInternet(getApplicationContext());
                            if(isConnectedToInternet)
                            {
                                getChequeList();
                            }else{
                                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "From Date Should be above To Date", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(ChequestatusListActivity.this);
        if(isConnectedToInternet)
        {
            getChequeList();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> ordersList = new ArrayList<HashMap<String, String>>();

        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
            this.mContext = mContext;
            ordersList = hashmapList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_creditlistadpater, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            String id = ordersList.get(position).get("id");
            String transaction_date = ordersList.get(position).get("transaction_date");
            String cheque_no = ordersList.get(position).get("cheque_no");
            String deposit_date = ordersList.get(position).get("deposit_date");
            String order_id = ordersList.get(position).get("order_id");

            holder.orderidval_txt.setText(order_id);
            holder.orderedon_txt.setText(transaction_date);

            holder.transactionid_title.setText("Cheque number");
            holder.transactiondate_title.setText("Deposit date");

            holder.transactionid_txt.setText(cheque_no);
            holder.transactiondate_txt.setText(deposit_date);

            holder.viewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(ChequestatusListActivity.this);
                    if (isConnectedToInternet) {
                        String id = ordersList.get(position).get("id");
                        String order_id = ordersList.get(position).get("order_id");

                        Intent intent = new Intent(getApplicationContext(), ChequeDetailsActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("order_id", order_id);

                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersList.size();
            //return 10;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView orderidval_txt,orderedon_txt, date_text,transactiondate_title,transactionid_title,transactionid_txt,transactiondate_txt;
            Button viewbtn;

            MyViewHolder(View view) {
                super(view);
                orderidval_txt = view.findViewById(R.id.orderidval_txt);
                orderedon_txt = view.findViewById(R.id.orderedon_txt);

                transactiondate_title = view.findViewById(R.id.transactiondate_title);
                transactionid_title = view.findViewById(R.id.transactionid_title);
                transactionid_txt = view.findViewById(R.id.transactionid_txt);
                transactiondate_txt = view.findViewById(R.id.transactiondate_txt);


                //   date_text = view.findViewById(R.id.date_text);
                viewbtn = view.findViewById(R.id.viewbtn);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getChequeList() {
        String from_date = edtxt_fromdate.getText().toString().trim();
        String to_date = edtxt_todate.getText().toString().trim();
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = null;
        mService = mApiService.getWalletChequeList(PRIMARYID,from_date,to_date);
        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (Integer.parseInt(status) == 1) {
                        recyclerView.setVisibility(View.VISIBLE);
                        imgrel.setVisibility(View.GONE);
                        List<WalletCreditDetailsDTO> ordersList = mOrderObject.getWalletCreditDetailsDTOList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String transaction_date = ordersList.get(i).getTransaction_date();
                                    String cheque_no = ordersList.get(i).getCheque_no();
                                    String action_date = ordersList.get(i).getAction_date();
                                    String deposit_date = ordersList.get(i).getCheque_deposit_date();
                                    String order_id = ordersList.get(i).getOrder_id();
                                    String uniq_id = ordersList.get(i).getUniq_id();

                                    hashMap.put("id", id);
                                    hashMap.put("transaction_date", transaction_date);
                                    hashMap.put("cheque_no", cheque_no);
                                    hashMap.put("action_date", action_date);
                                    hashMap.put("deposit_date", deposit_date);
                                    hashMap.put("order_id", order_id);
                                    hashMap.put("uniq_id", uniq_id);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.noordersfound);
                            }
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noordersfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashMapArrayList) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, hashMapArrayList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getSupportFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DatePickerFragment();
            tofragment.setArguments(bundle2);
            tofragment.show(getSupportFragmentManager(), "Date Picker");
        }
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String type;

            //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
            if (getArguments() != null) {
                type = getArguments().getString("DateType");
                if (type.equals("fromDate")) {
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);

                } else if (type.equals("toDate")) {
                    return new DatePickerDialog(getActivity(), to_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_fromdate.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                };
        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_todate.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

}

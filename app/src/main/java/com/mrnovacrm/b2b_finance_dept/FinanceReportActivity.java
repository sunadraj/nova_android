package com.mrnovacrm.b2b_finance_dept;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FinanceReportActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private RecyclerView recyclerView;
    public static EditText edtxt_fromdate, edtxt_todate;
    private String PRIMARYID = "";
    private HashMap<String, String> values;
    LinearLayout linear_header;
    // TextView text_nodata;
    private ImageView viewreport_btn;
    private SimpleDateFormat dfDate;
    RelativeLayout imgrel;
    GlobalShare globalShare;
    ImageView imageview;
    private String SHORTFORM;
    Context mContext;
    public static Activity mainfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mainfinish=this;
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.fragment_trackorderlist);

        globalShare = (GlobalShare) getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        edtxt_fromdate = findViewById(R.id.edtxt_fromdate);

        edtxt_todate = findViewById(R.id.edtxt_todate);
        linear_header = findViewById(R.id.linear_header);
        //    text_nodata = rootView.findViewById(R.id.text_nodata);

        imgrel = findViewById(R.id.imgrel);
        imageview = findViewById(R.id.imageview);

        viewreport_btn = findViewById(R.id.search);
        viewreport_btn.setOnClickListener(FinanceReportActivity.this);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }
        edtxt_fromdate.setOnTouchListener(FinanceReportActivity.this);
        edtxt_todate.setOnTouchListener(FinanceReportActivity.this);
        getOrdersList();
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

    private void getOrdersList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();

        recyclerView.setVisibility(View.GONE);
        linear_header.setVisibility(View.GONE);
        // text_nodata.setVisibility(View.VISIBLE);
        imgrel.setVisibility(View.VISIBLE);
        imageview.setImageResource(R.drawable.noordersfound);


//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        String from_date = edtxt_fromdate.getText().toString().trim();
//        String to_date = edtxt_todate.getText().toString().trim();
//        Call<Order> mService = mApiService.getOrdersList(PRIMARYID, from_date, to_date,"track",SHORTFORM);
//
//        mService.enqueue(new Callback<Order>() {
//            @Override
//            public void onResponse(Call<Order> call, Response<Order> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//
//                Order mOrderObject = response.body();
//                try {
//                    String status = mOrderObject.getStatus();
//                    Log.e("ordersstatus", "" + status);
//                    if (Integer.parseInt(status) == 1) {
//                        List<OrdersListDTO> ordersList = mOrderObject.getOrdersListDTOS();
//                        if (ordersList != null) {
//                            if (ordersList.size() > 0) {
//                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
//                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                for (int i = 0; i < ordersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    String id = ordersList.get(i).getId();
//                                    String orderId = ordersList.get(i).getOrder_id();
//                                    String orderedOn = ordersList.get(i).getOrderedon();
//                                    String status1 = ordersList.get(i).getStatus();
//
//                                    hashMap.put("id", id);
//                                    hashMap.put("orderId", orderId);
//                                    hashMap.put("orderedOn", orderedOn);
//                                    hashMap.put("status", status1);
//                                    hashmapList.add(hashMap);
//                                }
//                                showOrdersData(hashmapList);
//                            } else {
//                                recyclerView.setVisibility(View.GONE);
//                                linear_header.setVisibility(View.GONE);
//                                //  text_nodata.setVisibility(View.VISIBLE);
//                                imgrel.setVisibility(View.VISIBLE);
//                                imageview.setImageResource(R.drawable.noordersfound);
//                            }
//                        }
//                    } else {
//                        recyclerView.setVisibility(View.GONE);
//                        linear_header.setVisibility(View.GONE);
//                        // text_nodata.setVisibility(View.VISIBLE);
//                        imgrel.setVisibility(View.VISIBLE);
//                        imageview.setImageResource(R.drawable.noordersfound);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Order> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });

        dialog.dismiss();
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            linear_header.setVisibility(View.VISIBLE);
            // text_nodata.setVisibility(View.GONE);
            imgrel.setVisibility(View.GONE);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, hashmapList);
            recyclerView.setAdapter(adapter);
        } else {
            linear_header.setVisibility(View.GONE);
            // text_nodata.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
        }
    }

    @Override
    public void onClick(View view) {
        String FROMDATE = edtxt_fromdate.getText().toString().trim();
        String TODATE = edtxt_todate.getText().toString().trim();

       // dfDate = new SimpleDateFormat("yyyy-MM-dd");
        dfDate = new SimpleDateFormat("dd-MM-yyyy");
        if (FROMDATE.equals("") || FROMDATE.equals(null) || TODATE.equals("") || TODATE.equals(null)) {
            Toast.makeText(getApplicationContext(), "Please enter from date and to date", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                    getOrdersList();
                } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                    getOrdersList();
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
                    .inflate(R.layout.track_orderlist, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String id = ordersList.get(position).get("id");
            String orderId = ordersList.get(position).get("orderId");
            String orderedOn = ordersList.get(position).get("orderedOn");

            holder.orderid.setText(orderId);
            holder.orderid.setTextColor(getResources().getColor(R.color.bluecolor));
            holder.orderedOn.setText(orderedOn);
        }

        @Override
        public int getItemCount() {
            return ordersList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView orderid, orderedOn;

            MyViewHolder(View view) {
                super(view);
                orderid = view.findViewById(R.id.orderid);
                orderedOn = view.findViewById(R.id.dateval);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                String id = ordersList.get(getAdapterPosition()).get("id");
//                String orderId = ordersList.get(getAdapterPosition()).get("orderId");
//                String status = ordersList.get(getAdapterPosition()).get("status");
//                Intent intent = new Intent(getActivity(), TrackOrderDetailsActivity.class);
//                intent.putExtra("orderId", orderId);
//                intent.putExtra("id", id);
//                intent.putExtra("status", status);
//                startActivity(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (globalShare.getCancelorderfrom() != null) {
            if (globalShare.getCancelorderfrom().equals("track")) {
                globalShare.setCancelorderfrom("other");
                getOrdersList();
            }
        }
    }
}
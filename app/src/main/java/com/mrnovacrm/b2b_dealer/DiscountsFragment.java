package com.mrnovacrm.b2b_dealer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.CompanyAdapter;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.CompaniesDTO;
import com.mrnovacrm.model.DealersDTO;
import com.mrnovacrm.model.DealersRecordListDTO;
import com.mrnovacrm.model.EmployeesDiscountsDTO;
import com.mrnovacrm.model.EmployeesRecordListDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.ProductsDiscountsDTO;
import com.mrnovacrm.model.ProductsRecordListDTO;
import com.mrnovacrm.model.RecordListDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harish on 6/24/2019.
 */

public class DiscountsFragment extends Fragment implements View.OnTouchListener {

    Spinner companySpinner, dealerSpinner, productSpinner, referenceSpinner;
    EditText discountText;
    @SuppressLint("StaticFieldLeak")
    public static EditText edtxt_fromdate, edtxt_todate;
    Button buttonSubmit;


    List<RecordListDTO> sellersList;
    List<DealersRecordListDTO> dealersRecordList;
    List<ProductsRecordListDTO> productsRecordList;
    List<EmployeesRecordListDTO> employeesRecordList;
    ArrayList<String> companyNames, dealerNameList, productsNameList, employeesNameList;


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_disocunts, container, false);


        companySpinner = rootView.findViewById(R.id.company_spinner);
        dealerSpinner = rootView.findViewById(R.id.dealer_spinner);
        productSpinner = rootView.findViewById(R.id.product_spinner);
        referenceSpinner = rootView.findViewById(R.id.reference_spinner);
        discountText = rootView.findViewById(R.id.discount_txt);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(DiscountsFragment.this);
        edtxt_todate.setOnTouchListener(DiscountsFragment.this);

        getDiscountCompany();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDiscountRequest();
            }
        });


        return rootView;
    }


    public void getDiscountCompany() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<CompaniesDTO> mService = mApiService.getDiscounts();

        mService.enqueue(new Callback<CompaniesDTO>() {
            @Override
            public void onResponse(@NonNull Call<CompaniesDTO> call, @NonNull Response<CompaniesDTO> response) {
                dialog.dismiss();
                try {
                    CompaniesDTO mOrderObject = response.body();
                    assert mOrderObject != null;
                    int status = mOrderObject.getStatus();
                    if (status == 1) {
                        sellersList = mOrderObject.getRecordList();
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {

                                companyNames = new ArrayList<>();

                                companyNames.add("Select Company");

                                for (int i = 0; i < sellersList.size(); i++) {
                                    companyNames.add(sellersList.get(i).getCompany());
                                }


                                CompanyAdapter adapter = new CompanyAdapter(getActivity(), companyNames);
                                companySpinner.setAdapter(adapter);


                                companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                        if (!companySpinner.getSelectedItem().equals("Select Company")) {

                                            getDealersList(sellersList.get(pos - 1).getCompanyId());
                                            getProductsList(sellersList.get(pos - 1).getCompanyId());
                                            getEmployeesList(sellersList.get(pos - 1).getCompanyId());

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                            }
                        }
                    } else {
                        String message = mOrderObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompaniesDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDealersList(String companyId) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DealersDTO> mService = mApiService.getDiscountsDealers(companyId);

        mService.enqueue(new Callback<DealersDTO>() {
            @Override
            public void onResponse(@NonNull Call<DealersDTO> call, @NonNull Response<DealersDTO> response) {
                DealersDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        dealersRecordList = mstoresObject.getRecordList();


                        dealerNameList = new ArrayList<>();
                        dealerNameList.add("Select Dealer");

                        for (int i = 0; i < dealersRecordList.size(); i++) {
                            dealerNameList.add(dealersRecordList.get(i).getName());
                        }


                        CompanyAdapter adapter = new CompanyAdapter(getActivity(), dealerNameList);
                        dealerSpinner.setAdapter(adapter);

                        dealerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                if (!dealerSpinner.getSelectedItem().equals("Select Dealer")) {

                                    Toast.makeText(getActivity(), dealersRecordList.get(pos - 1).getName(), Toast.LENGTH_SHORT).show();


//                                    getDealersList(sellersList.get(pos).getCompanyId());

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DealersDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductsList(String companyId) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<ProductsDiscountsDTO> mService = mApiService.getDiscountsProducts(companyId);

        mService.enqueue(new Callback<ProductsDiscountsDTO>() {
            @Override
            public void onResponse(@NonNull Call<ProductsDiscountsDTO> call, @NonNull Response<ProductsDiscountsDTO> response) {
                ProductsDiscountsDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        productsRecordList = mstoresObject.getRecordList();


                        productsNameList = new ArrayList<>();
                        productsNameList.add("Select Products");

                        for (int i = 0; i < productsRecordList.size(); i++) {
                            productsNameList.add(productsRecordList.get(i).getItemName());
                        }


                        CompanyAdapter adapter = new CompanyAdapter(getActivity(), productsNameList);
                        productSpinner.setAdapter(adapter);

                        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                if (!productSpinner.getSelectedItem().equals("Select Products")) {

                                    Toast.makeText(getActivity(), productsRecordList.get(pos - 1).getItemName(), Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductsDiscountsDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEmployeesList(String companyId) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeesDiscountsDTO> mService = mApiService.getDiscountsEmployees(companyId);

        mService.enqueue(new Callback<EmployeesDiscountsDTO>() {
            @Override
            public void onResponse(@NonNull Call<EmployeesDiscountsDTO> call, @NonNull Response<EmployeesDiscountsDTO> response) {
                EmployeesDiscountsDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        employeesRecordList = mstoresObject.getRecordList();


                        employeesNameList = new ArrayList<>();
                        employeesNameList.add("Select Employee");

                        for (int i = 0; i < employeesRecordList.size(); i++) {
                            employeesNameList.add(employeesRecordList.get(i).getUniqId());
                        }


                        CompanyAdapter adapter = new CompanyAdapter(getActivity(), employeesNameList);
                        referenceSpinner.setAdapter(adapter);

                        referenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                if (!referenceSpinner.getSelectedItem().equals("Select Employee")) {

                                    Toast.makeText(getActivity(), employeesRecordList.get(pos - 1).getUniqId(), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeesDiscountsDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            assert getFragmentManager() != null;
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DatePickerFragment();
            tofragment.setArguments(bundle2);
            assert getFragmentManager() != null;
            tofragment.show(getFragmentManager(), "Date Picker");
        }
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String type;

            if (getArguments() != null) {
                type = getArguments().getString("DateType");
                assert type != null;
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


    private void sendDiscountRequest() {

        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();


        Log.e("company", companySpinner.getSelectedItem().toString());
        Log.e("dealer", dealerSpinner.getSelectedItem().toString());
        Log.e("product", productSpinner.getSelectedItem().toString());
        Log.e("requestTo", referenceSpinner.getSelectedItem().toString());
        Log.e("requestBy", SharedDB.USERID);
        Log.e("startDate", edtxt_fromdate.getText().toString());
        Log.e("endDate", edtxt_todate.getText().toString());
        Log.e("discount", discountText.getText().toString());

        Call<Login> mService = mApiService.discountRequest(companySpinner.getSelectedItem().toString()
                , dealerSpinner.getSelectedItem().toString()
                , productSpinner.getSelectedItem().toString(),
                referenceSpinner.getSelectedItem().toString(), SharedDB.USERID
                , edtxt_fromdate.getText().toString(), edtxt_todate.getText().toString(), discountText.getText().toString());
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                Login mLoginObject = response.body();
                Log.e("response", " :" + response);
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        Toast.makeText(getActivity(), mLoginObject.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                Log.e("Throwable", " :" + t.getMessage());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
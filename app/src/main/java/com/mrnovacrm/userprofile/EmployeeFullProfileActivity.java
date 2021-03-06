package com.mrnovacrm.userprofile;

//public class EmployeeFullProfileActivity {
//}

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmpDetailsDTO;
import com.mrnovacrm.model.Login;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeFullProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView profileimage;
    Context context;
    private String base64 = "";
    public static final int RequestPermissionCode = 1;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    private String SHORTFORM = "";
    String USERNAME = "", MOBILE = "", DELIVERY_ADDRESS = "";

    //private TextView profilenametxt, mobilenumbertxt, emailtxt, addresstxt;

    GlobalShare globalShare;
    ImageView camimg;
    private boolean result;
    private static final int OPENGALLERY = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri selectedImageUri;
    private String selectedPath1;
    private Bitmap selectedImageBitmap;

    LinearLayout doblinear,
            sexlinear,
            fathernamelinear,
            mothernamelinear,
            martialllinear,
            companylinear,
            keypersonnamelinear,
            gstinlinear,
            regtypelinear,
            esilinear, empidlinear, dealeridlinear, dojlinear, departmentlinear, designationlinear, companyemaillinear,
            companycontactlinear, totalexplinear, reportingmanagerlinear, ofcemaillinear, officialcontactlinear,
            officialwhatsapplinear, areasalesmanagerlinear, areamanagercontactlinear,pflinear,licencelinear;

    TextView nametxt,dobtxt, sextxt, fathernametxt, mothernametxt, martialtxt, addresstxt,
            countrytxt, statetxt, towncitytxt, pincodetxt, pannumbertxt, pfnumbertxt, esinumbertxt,
            banknametxt, accountnumbertxt, ifsccodetxt, employeeidtxt, dobjoiningtxt, departmenttxt, designationtxt,
            companyemailtxt, companycontacttxt, totalexptxt, reportingmanagertxt, profilenametxt,
            companynametxt,
            key_persontxt,
            dealeridtxt,
            officialemail_txt,
            officialcontact_txt,
            officialwhatsapp_txt,
            areasalesmanager_txt,
            areamanagercontact_txt,
            personalmobile_txt,
            personalemail_txt,regtypetxt,gsttxt,licencenumbertxt;
    private TransparentProgressDialog dialog;
    LinearLayout editlinear;

    public static Activity mainfinish;
    String IDVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_employeefullprofile);
        View includedLayout = findViewById(R.id.include_actionbar);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
        backimg.setOnClickListener(EmployeeFullProfileActivity.this);
        actionbarheadertxt.setText("Employee profile");


        Bundle bundle=getIntent().getExtras();
        IDVAL= bundle.getString("id");

        mainfinish=this;
        editlinear = findViewById(R.id.editlinear);

        profileimage = findViewById(R.id.profileimage);
        licencenumbertxt = findViewById(R.id.licencenumbertxt);
        doblinear = findViewById(R.id.doblinear);
        sexlinear = findViewById(R.id.sexlinear);
        pflinear = findViewById(R.id.pflinear);
        licencelinear = findViewById(R.id.licencelinear);

        fathernamelinear = findViewById(R.id.fathernamelinear);
        mothernamelinear = findViewById(R.id.mothernamelinear);
        martialllinear = findViewById(R.id.martialllinear);
        companylinear = findViewById(R.id.companylinear);
        keypersonnamelinear = findViewById(R.id.keypersonnamelinear);
        gstinlinear = findViewById(R.id.gstinlinear);
        regtypelinear = findViewById(R.id.regtypelinear);
        esilinear = findViewById(R.id.esilinear);
        empidlinear = findViewById(R.id.empidlinear);
        dealeridlinear = findViewById(R.id.dealeridlinear);
        dojlinear = findViewById(R.id.dojlinear);
        departmentlinear = findViewById(R.id.departmentlinear);
        designationlinear = findViewById(R.id.designationlinear);
        companyemaillinear = findViewById(R.id.companyemaillinear);
        companycontactlinear = findViewById(R.id.companycontactlinear);
        totalexplinear = findViewById(R.id.totalexplinear);
        reportingmanagerlinear = findViewById(R.id.reportingmanagerlinear);
        ofcemaillinear = findViewById(R.id.ofcemaillinear);
        officialcontactlinear = findViewById(R.id.officialcontactlinear);
        officialwhatsapplinear = findViewById(R.id.officialwhatsapplinear);
        areasalesmanagerlinear = findViewById(R.id.areasalesmanagerlinear);
        areamanagercontactlinear = findViewById(R.id.areamanagercontactlinear);


        nametxt = findViewById(R.id.nametxt);
        gsttxt = findViewById(R.id.gsttxt);
        regtypetxt = findViewById(R.id.regtypetxt);
        companynametxt = findViewById(R.id.companynametxt);
        key_persontxt = findViewById(R.id.key_persontxt);
        dealeridtxt = findViewById(R.id.dealeridtxt);
        officialemail_txt = findViewById(R.id.officialemail_txt);
        officialcontact_txt = findViewById(R.id.officialcontact_txt);
        officialwhatsapp_txt = findViewById(R.id.officialwhatsapp_txt);
        areasalesmanager_txt = findViewById(R.id.areasalesmanager_txt);
        areamanagercontact_txt = findViewById(R.id.areamanagercontact_txt);
        personalmobile_txt = findViewById(R.id.personalmobile_txt);
        personalemail_txt = findViewById(R.id.personalemail_txt);

        dobtxt = findViewById(R.id.dobtxt);
        sextxt = findViewById(R.id.sextxt);
        fathernametxt = findViewById(R.id.fathernametxt);
        mothernametxt = findViewById(R.id.mothernametxt);
        martialtxt = findViewById(R.id.martialtxt);
        addresstxt = findViewById(R.id.addresstxt);
        countrytxt = findViewById(R.id.countrytxt);
        statetxt = findViewById(R.id.statetxt);
        towncitytxt = findViewById(R.id.towncitytxt);
        pincodetxt = findViewById(R.id.pincodetxt);
        pannumbertxt = findViewById(R.id.pannumbertxt);
        pfnumbertxt = findViewById(R.id.pfnumbertxt);
        esinumbertxt = findViewById(R.id.esinumbertxt);
        banknametxt = findViewById(R.id.banknametxt);
        accountnumbertxt = findViewById(R.id.accountnumbertxt);
        ifsccodetxt = findViewById(R.id.ifsccodetxt);
        employeeidtxt = findViewById(R.id.employeeidtxt);
        dobjoiningtxt = findViewById(R.id.dobjoiningtxt);
        departmenttxt = findViewById(R.id.departmenttxt);
        designationtxt = findViewById(R.id.designationtxt);
        companyemailtxt = findViewById(R.id.companyemailtxt);
        companycontacttxt = findViewById(R.id.companycontacttxt);
        totalexptxt = findViewById(R.id.totalexptxt);
        reportingmanagertxt = findViewById(R.id.reportingmanagertxt);
        profilenametxt = findViewById(R.id.profilenametxt);

        addresstxt = findViewById(R.id.addresstxt);
        camimg = findViewById(R.id.camimg);

        try {
            if (SharedDB.isLoggedIn(getApplicationContext())) {
                values = SharedDB.getUserDetails(getApplicationContext());
                PRIMARYID = values.get(SharedDB.PRIMARYID);
                USERTYPE = values.get(SharedDB.USERTYPE);
                SHORTFORM = values.get(SharedDB.SHORTFORM);
                USERNAME = values.get(SharedDB.USERNAME);
                MOBILE = values.get(SharedDB.MOBILE);
                DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                String IMAGEURL = values.get(SharedDB.IMAGEURL);

                if (!IMAGEURL.equals("")) {
                    Picasso.with(context).load(IMAGEURL).memoryPolicy(MemoryPolicy.NO_STORE).into(profileimage);
                } else {
                    profileimage.setImageResource(R.drawable.noprofile);
                }
               // profilenametxt.setText(USERNAME);
                //mobilenumbertxt.setText(MOBILE);
            //    addresstxt.setText(DELIVERY_ADDRESS);
            }
        } catch (Exception e) {
        }

            companylinear.setVisibility(View.GONE);
            keypersonnamelinear.setVisibility(View.GONE);
            doblinear.setVisibility(View.VISIBLE);
            sexlinear.setVisibility(View.VISIBLE);
            fathernamelinear.setVisibility(View.VISIBLE);
            mothernamelinear.setVisibility(View.VISIBLE);
            martialllinear.setVisibility(View.VISIBLE);
            licencelinear.setVisibility(View.GONE);
            gstinlinear.setVisibility(View.GONE);
            regtypelinear.setVisibility(View.GONE);
            pflinear.setVisibility(View.VISIBLE);
            esilinear.setVisibility(View.VISIBLE);
            empidlinear.setVisibility(View.VISIBLE);
            dojlinear.setVisibility(View.VISIBLE);
            departmentlinear.setVisibility(View.VISIBLE);
            designationlinear.setVisibility(View.VISIBLE);
            companyemaillinear.setVisibility(View.VISIBLE);
            companycontactlinear.setVisibility(View.VISIBLE);
            totalexplinear.setVisibility(View.VISIBLE);
            reportingmanagerlinear.setVisibility(View.VISIBLE);

            dealeridlinear.setVisibility(View.GONE);
            ofcemaillinear.setVisibility(View.GONE);
            officialcontactlinear.setVisibility(View.GONE);
            officialwhatsapplinear.setVisibility(View.GONE);
            areasalesmanagerlinear.setVisibility(View.GONE);
            areamanagercontactlinear.setVisibility(View.GONE);


        camimg.setOnClickListener(EmployeeFullProfileActivity.this);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileFullScreenActivity.class);
                startActivity(intent);
            }
        });

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if(isConnectedToInternet)
        {
            getEMPListDetails();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        editlinear.setOnClickListener(EmployeeFullProfileActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camimg:

                break;
            case R.id.backimg:
                recallMethod();
                break;
            case R.id.editlinear:
                if(SHORTFORM.equals("DEALER")) {
                    Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), UpdateEmployeeProfileActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void getEMPListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(EmployeeFullProfileActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;
       mService = mApiService.getEMPDetails(IDVAL);


        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Login mOrderObject = response.body();
                    String status = mOrderObject.getStatus();

                    Log.e("status", status);

                    if (Integer.parseInt(status) == 1) {
                        List<EmpDetailsDTO> empDetailsDTOS = mOrderObject.getEmpDetailsDTOS();

                        if (empDetailsDTOS != null) {
                            if (empDetailsDTOS.size() > 0) {

                                for (int i = 0; i < empDetailsDTOS.size(); i++) {
                                    String firstname = empDetailsDTOS.get(i).getFirst_name();
                                    String last_name = empDetailsDTOS.get(i).getLast_name();
                                    String uniq_id = empDetailsDTOS.get(i).getUniq_id();
                                    String branch = empDetailsDTOS.get(i).getBranch();
                                    String dept = empDetailsDTOS.get(i).getDept();
                                    String mobile = empDetailsDTOS.get(i).getMobile();
                                    String email = empDetailsDTOS.get(i).getEmail();
                                    String dob = empDetailsDTOS.get(i).getDob();
                                    String gender = empDetailsDTOS.get(i).getGender();
                                    String location = empDetailsDTOS.get(i).getLocation();
                                    String blood_group = empDetailsDTOS.get(i).getBlood_group();
                                    String father = empDetailsDTOS.get(i).getFather();
                                    String mother = empDetailsDTOS.get(i).getMother();
                                    String marital = empDetailsDTOS.get(i).getMarital();
                                    String expeience = empDetailsDTOS.get(i).getExpeience();
                                    String report_to = empDetailsDTOS.get(i).getReport_to();
                                    String doj = empDetailsDTOS.get(i).getDoj();
                                    String pan = empDetailsDTOS.get(i).getPan();
                                    String pf = empDetailsDTOS.get(i).getPf();
                                    String esi = empDetailsDTOS.get(i).getEsi();
                                    String bank_name = empDetailsDTOS.get(i).getBank_name();
                                    String bank_account = empDetailsDTOS.get(i).getBank_account();
                                    String ifsc = empDetailsDTOS.get(i).getIfsc();
                                    String address = empDetailsDTOS.get(i).getAddress();
                                    String addressproof1 = empDetailsDTOS.get(i).getAddressproof1();
                                    String addressproof2 = empDetailsDTOS.get(i).getAddressproof2();
                                    String role_id = empDetailsDTOS.get(i).getRole_id();
                                    String ofc_email = empDetailsDTOS.get(i).getOfc_email();
                                    String ofc_contact = empDetailsDTOS.get(i).getOfc_contact();
                                    String createdon = empDetailsDTOS.get(i).getCreatedon();
                                    String createdby = empDetailsDTOS.get(i).getCreatedby();
                                    String modifiedon = empDetailsDTOS.get(i).getModifiedon();
                                    String modifiedby = empDetailsDTOS.get(i).getModifiedby();
                                    String country = empDetailsDTOS.get(i).getCountry();
                                    String state = empDetailsDTOS.get(i).getState();
                                    String city = empDetailsDTOS.get(i).getCity();
                                    String pincode = empDetailsDTOS.get(i).getPincode();
                                    String gstin = empDetailsDTOS.get(i).getGstin();
                                    String company_name = empDetailsDTOS.get(i).getCompany_name();
                                    //  String key_person = empDetailsDTOS.get(i).getKey_person();
                                    String reg_type = empDetailsDTOS.get(i).getReg_type();
                                    //   String ofc_whatsapp = empDetailsDTOS.get(i).getOfc_whatsapp();
                                    String sales_manager = empDetailsDTOS.get(i).getSm();
                                    String am_contact = empDetailsDTOS.get(i).getAm_contact();
                                    String dealer_code = empDetailsDTOS.get(i).getDealer_code();
                                    String country_id = empDetailsDTOS.get(i).getCountry_id();
                                    String state_id = empDetailsDTOS.get(i).getState_id();
                                    String licence = empDetailsDTOS.get(i).getLicence();
                                    String area_sm = empDetailsDTOS.get(i).getSm();
                                    String designation = empDetailsDTOS.get(i).getDesignation();

                                        nametxt.setText(firstname);
                                        dobtxt.setText(dob);
                                        sextxt.setText(gender);
                                        fathernametxt.setText(father);
                                        mothernametxt.setText(mother);
                                        martialtxt.setText(marital);
                                        pfnumbertxt.setText(pf);
                                        esinumbertxt.setText(esi);

                                        employeeidtxt.setText(uniq_id);
                                        dobjoiningtxt.setText(doj);
                                        departmenttxt.setText(dept);
                                        designationtxt.setText(designation);
                                        companyemailtxt.setText(ofc_email);
                                        companycontacttxt.setText(ofc_contact);
                                        totalexptxt.setText(expeience);
                                        reportingmanagertxt.setText(report_to);

                                    addresstxt.setText(address);
                                    countrytxt.setText(country);
                                    statetxt.setText(state);
                                    towncitytxt.setText(city);
                                    pincodetxt.setText(pincode);
                                    pannumbertxt.setText(pan);
                                    banknametxt.setText(bank_name);
                                    accountnumbertxt.setText(bank_account);
                                    ifsccodetxt.setText(ifsc);
                                    personalmobile_txt.setText(mobile);
                                    personalemail_txt.setText(email);

                                }
                            }
                        }

                    } else {
                        String messge = mOrderObject.getMessage();
                        Toast.makeText(getApplicationContext(), messge, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        recallMethod();
    }

    public void recallMethod() {
        finish();
    }
}
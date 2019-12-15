package com.mecodroid.blood_bank.helper;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.api.ApiServer;
import com.mecodroid.blood_bank.data.model.bloodtypes.BloodTypes;
import com.mecodroid.blood_bank.data.model.cities.CityDataModel;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import com.mecodroid.blood_bank.data.model.governorates.Governorates;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mecodroid.blood_bank.helper.HelperMethod.customMassageError;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissLovelyDailog;
import static com.mecodroid.blood_bank.helper.HelperMethod.dismissProgressDialog;
import static com.mecodroid.blood_bank.helper.HelperMethod.setLovelyProgressDailog;
import static com.mecodroid.blood_bank.helper.HelperMethod.showProgressDialog;

public class CommonModelMethod {
    public static List<GeneralModel> generalList = new ArrayList<>();
    // store blood type name
    public static ArrayList<String> typeBlood = new ArrayList<>();
    // store blood type id
    public static ArrayList<Integer> idBlood = new ArrayList<Integer>();
    public static ArrayList<String> citiesName = new ArrayList<>();
    public static ArrayList<Integer> citiesId = new ArrayList<>();
    public static ArrayList<String> governorat = new ArrayList<>();
    public static ArrayList<Integer> idGovern = new ArrayList<>();
    public static String blood_type_id;
    public static String startCityId;

    // get all blood Types
    public static void getAllBloodTypes(final Activity activity, ApiServer apiServer,
                                        final Spinner spinner) {
        setLovelyProgressDailog(activity, 0, null,
                Color.WHITE, R.color.thick_blue);
        apiServer.getBloodTypes().enqueue(new Callback<BloodTypes>() {
            @Override
            public void onResponse(Call<BloodTypes> call, Response<BloodTypes> response) {
                dismissLovelyDailog();
                generalList = new ArrayList<>();
                generalList = response.body().getData();
                // title blood type
                typeBlood.add(activity.getResources().getString(R.string.blood_type).trim());
                idBlood.add(0);
                // loop all blood types and pass name types to blood name list, pass the id of blood types to blood id list
                for (int i = 0; i < generalList.size(); i++) {
                    String bloodTypNAme = generalList.get(i).getName();
                    Integer bloodTypId = generalList.get(i).getId();
                    typeBlood.add(bloodTypNAme);
                    idBlood.add(bloodTypId);
                }

                // create array adapter to view list
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                        R.layout.spinner_layout, typeBlood) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                // to specify form of spinner
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                // bind spinner with adapter
                spinner.setAdapter(adapter);
                // interaction with spinner
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // return the item selected from spinner else postion equal zero (title)
                        blood_type_id = String.valueOf(idBlood.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<BloodTypes> call, Throwable t) {
                dismissProgressDialog();
                customMassageError(activity, t.getMessage());
            }
        });

    }

    // get all Governratrate
    public static void getAllGovern(final Activity activity, final ApiServer apiServer,
                                    final Spinner spinner, final Spinner spinner1) {
        apiServer.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                dismissProgressDialog();
                generalList = new ArrayList<>();
                generalList = response.body().getData();
                governorat.add(activity.getResources().getString(R.string.choosegovernorate));
                idGovern.add(0);

                for (int i = 0; i < generalList.size(); i++) {
                    String governorateName = generalList.get(i).getName();
                    Integer governoratId = generalList.get(i).getId();
                    governorat.add(governorateName);
                    idGovern.add(governoratId);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                        R.layout.spinner_layout, governorat) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            getAllCity((idGovern.get(position)), activity, apiServer, spinner1);
                            spinner1.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {
                dismissProgressDialog();
                customMassageError(activity, t.getMessage());
            }

        });
    }

    // get all city
    private static void getAllCity(Integer gavernoratesId, final Activity activity, ApiServer apiServer, final Spinner spinner) {
        showProgressDialog(activity, activity.getResources().getString(R.string.wait));
        apiServer.getCities(gavernoratesId)
                .enqueue(new Callback<CityDataModel>() {
                    @Override
                    public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                        dismissProgressDialog();
                        generalList = new ArrayList<>();
                        generalList = response.body().getData();


                        citiesName.add(activity.getResources().getString(R.string.choosecity));
                        citiesId.add(0);

                        for (int i = 0; i < generalList.size(); i++) {
                            String cityName = generalList.get(i).getName();
                            Integer cityId = generalList.get(i).getId();

                            citiesName.add(cityName);
                            citiesId.add(cityId);
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                                R.layout.spinner_layout, citiesName) {
                            @Override
                            public boolean isEnabled(int position) {
                                return position != 0;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };

                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    startCityId = String.valueOf(citiesId.get(position));
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {
                        dismissProgressDialog();
                        customMassageError(activity, t.getMessage());


                    }
                });
    }


}

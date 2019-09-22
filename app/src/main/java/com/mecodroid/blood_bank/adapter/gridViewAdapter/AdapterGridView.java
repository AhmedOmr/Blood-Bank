package com.mecodroid.blood_bank.adapter.gridViewAdapter;

 import android.app.Activity;
 import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.data.model.cities.GeneralModel;
import java.util.ArrayList;
import java.util.List;


public class AdapterGridView extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<GeneralModel> generatedModelArrayList;

    private List<Integer> idGovernorates_Blood;


    public List<Integer> numCheck = new ArrayList<>();


    public AdapterGridView(Activity context, List<GeneralModel> spinnerArrayList, List<Integer> idGovernorates_Blood) {
        this.context = context;
        this.generatedModelArrayList = spinnerArrayList;
        this.idGovernorates_Blood = idGovernorates_Blood;
    }

    @Override
    public int getCount() {
        return generatedModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return generatedModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_grid_view, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.setAdapterGridChk = convertView.findViewById(R.id.setAdapterGridChk);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setAdapterGridChk.setText(generatedModelArrayList.get(position).getName());

        // for loop in list id notify
        for (int i = 0; i < idGovernorates_Blood.size(); i++) {

            viewHolder.setAdapterGridChk.setChecked(false);

            if (idGovernorates_Blood.get(i).equals(generatedModelArrayList.get(position).getId())) {
                viewHolder.setAdapterGridChk.setChecked(true);
                numCheck.add(generatedModelArrayList.get(position).getId());
                break;
            }
        }

        viewHolder.setAdapterGridChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    numCheck.add(generatedModelArrayList.get(position).getId());
                } else {
                    numCheck.remove(generatedModelArrayList.get(position).getId());
                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        CheckBox setAdapterGridChk;
    }

}

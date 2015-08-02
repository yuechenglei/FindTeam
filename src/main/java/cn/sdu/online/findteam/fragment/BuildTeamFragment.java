package cn.sdu.online.findteam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.resource.SwitchButton;


public class BuildTeamFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    public TextView bt_confirm;
    public EditText text_teamname, text_introduction;
    public Button bt_changehead;
    private Spinner spinner_number, spinner_year, spinner_month, spinner_day;
    private static final String[] number = {"请选择", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final String[] year = {"年", "2015", "2016", "2017", "2018"};
    private static final String[] month = {"月", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private static final String[] day = {"日", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    private ArrayAdapter<String> adapter_number, adapter_year, adapter_month, adapter_day;

    private View view;

    private SwitchButton switchButton1, switchButton2, switchButton3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.buildteam_layout,null);

        initView();

        return view;
    }

    private void initView(){
        text_teamname = (EditText) view.findViewById(R.id.text_teamname);
        text_teamname.setOnClickListener(this);
        text_introduction = (EditText) view.findViewById(R.id.text_introduction);
        text_introduction.setOnClickListener(this);
        bt_changehead = (Button) view.findViewById(R.id.bt_changehead);
        bt_changehead.setOnClickListener(this);
        bt_confirm = (TextView) view.findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(this);

        spinner_number = (Spinner) view.findViewById(R.id.spinnernumber);
        adapter_number = new ArrayAdapter<String>(BuildTeamFragment.this.getActivity(), android.R.layout.simple_spinner_item, number);
        adapter_number.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_number.setAdapter(adapter_number);
        spinner_number.setOnItemSelectedListener(new SpinnerSelectedListener_number());
        spinner_number.setVisibility(View.VISIBLE);

        spinner_year = (Spinner) view.findViewById(R.id.spinneryear);
        adapter_year = new ArrayAdapter<String>(BuildTeamFragment.this.getActivity(), android.R.layout.simple_spinner_item, year);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);
        spinner_year.setOnItemSelectedListener(new SpinnerSelectedListener_year());
        spinner_year.setVisibility(View.VISIBLE);

        spinner_month = (Spinner) view.findViewById(R.id.spinnermonth);
        adapter_month = new ArrayAdapter<String>(BuildTeamFragment.this.getActivity(), android.R.layout.simple_spinner_item, month);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_month);
        spinner_month.setOnItemSelectedListener(new SpinnerSelectedListener_month());
        spinner_month.setVisibility(View.VISIBLE);

        spinner_day = (Spinner) view.findViewById(R.id.spinnerday);
        adapter_day = new ArrayAdapter<String>(BuildTeamFragment.this.getActivity(), android.R.layout.simple_spinner_item, day);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapter_day);
        spinner_day.setOnItemSelectedListener(new SpinnerSelectedListener_day());
        spinner_day.setVisibility(View.VISIBLE);

        switchButton1 = (SwitchButton) view.findViewById(R.id.switch_test);
        switchButton2 = (SwitchButton) view.findViewById(R.id.switch_see);
        switchButton3 = (SwitchButton) view.findViewById(R.id.switch_comment);
        switchButton1.setChecked(false);
        switchButton2.setChecked(false);
        switchButton3.setChecked(false);
        switchButton1.setOnCheckedChangeListener(this);
        switchButton2.setOnCheckedChangeListener(this);
        switchButton3.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == false) {
            Toast.makeText(BuildTeamFragment.this.getActivity(),
                    "您关闭了我",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(BuildTeamFragment.this.getActivity(),
                    "您打开了我",Toast.LENGTH_SHORT).show();
        }
    }

    class SpinnerSelectedListener_number implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            spinner_number.setTag(number[arg2]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class SpinnerSelectedListener_year implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            spinner_year.setTag(year[arg2]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class SpinnerSelectedListener_month implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            spinner_month.setTag(month[arg2]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class SpinnerSelectedListener_day implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            spinner_day.setTag(day[arg2]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_teamname: {
                text_teamname.setHint(null);
            }
            case R.id.text_introduction: {
                text_introduction.setHint(null);
            }
        }
    }
}


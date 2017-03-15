package go.fish.goapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import go.fish.goapp.R;

/**
 * Created by fish on 17-2-28.
 */

public class AddEquipmentsActivity extends Activity {

    private EditText mEtName;
    private EditText mEtLv;
    private EditText mEtHoly;
    private EditText mEtCri;
    private EditText mEtMiss;
    private EditText mEtDesc;
    private Spinner mSpiClr;
    private Spinner mSpiT1;
    private Spinner mSpiT2;
    private Spinner mSpiT3;

    private int mClrFlag = -1;
    private int mT1Flag = -1;
    private int mT2Flag = -1;
    private int mT3Flag = -1;


    String[] E_COLOR        = {"请选择品质", "普通", "高级", "稀有", "神器", "传说", "史诗", "？？？", };
    String[] A_TYPE         = {"请选择武器类别", "武器", "防具", };
    String[] WEAPON_USER    = {"请选择使用职业", "所有", "战士", "魔法师", "流浪者", };
    String[][] WEAPON_TYPE  = {
            {"利器", "钝器"},
            {"短剑", "长矛", "刀", "斧子"},
            {"张", "", "", ""},
            {"", "", "", ""},};
    String[] EQUIP_TYPE     ={"布甲", "皮甲", "轻甲", "重甲", };
    String[] EQUIP_POI      ={"头肩", "衣服", "鞋子", };


    private ArrayAdapter<String> mArrAdapterClr;
    private ArrayAdapter<String> mArrAdapter1;
    private ArrayAdapter<String> mArrAdapter2;
    private ArrayAdapter<String> mArrAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_tmp_adde);
        mEtName = (EditText) findViewById(R.id.et_adde_name);
        mEtLv = (EditText) findViewById(R.id.et_adde_lv);
        mEtHoly = (EditText) findViewById(R.id.et_adde_holy);
        mEtCri = (EditText) findViewById(R.id.et_adde_cri);
        mEtMiss = (EditText) findViewById(R.id.et_adde_miss);
        mEtDesc = (EditText) findViewById(R.id.et_adde_desc);
        mSpiClr = (Spinner) findViewById(R.id.spn_adde_color);
        mSpiT1 = (Spinner) findViewById(R.id.spn_adde_type);
        mSpiT2 = (Spinner) findViewById(R.id.spn_adde_type2);
        mSpiT3 = (Spinner) findViewById(R.id.spn_adde_type3);
        findViewById(R.id.btn_cmt).setOnClickListener(v -> commit());
        initAdapters();
    }

    private void initAdapters() {
        mArrAdapterClr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mArrAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mArrAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mArrAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mArrAdapterClr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mArrAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mArrAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mArrAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpiClr.setAdapter(mArrAdapterClr);
        mSpiT1.setAdapter(mArrAdapter1);
        mSpiT2.setAdapter(mArrAdapter2);
        mSpiT3.setAdapter(mArrAdapter3);
        mArrAdapterClr.clear();
        mArrAdapter1.clear();
        mArrAdapter2.clear();
        mArrAdapter3.clear();
        mArrAdapterClr.addAll(E_COLOR);
        mArrAdapter1.addAll(A_TYPE);

        mSpiClr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClrFlag = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSpiT1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mT1Flag = position;
                if (position == 1) {
                    flushWpAdapter();
                } else if (position == 2) {
                    flushEqAdapter();
                } else if (position == 3) {
                    flushRgAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void flushWpAdapter() {
        mArrAdapter2.clear();
        mArrAdapter2.addAll(WEAPON_USER);
        mArrAdapter3.clear();
        mT2Flag = 0;
        mT3Flag = 0;
    }

    private void flushEqAdapter() {
        mArrAdapter2.clear();
        mArrAdapter2.addAll(EQUIP_POI);
        mArrAdapter3.clear();
        mArrAdapter2.addAll(EQUIP_TYPE);
        mT2Flag = 0;
        mT3Flag = 0;
    }


    private void flushRgAdapter() {
        mArrAdapter2.clear();
        mArrAdapter2.addAll(EQUIP_POI);
        mArrAdapter3.clear();
        mT2Flag = 0;
        mT3Flag = 0;
    }

    private void commit() {

    }

    private int getEditInt(EditText et) {
        if (et.getText() == null) {
            return 0;
        }
        String intStr = et.getText().toString();
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

}

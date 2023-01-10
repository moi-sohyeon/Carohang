package com.example.cafetotravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EventDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG_EVENT_DIALOG = "dialog_event";

    RadioGroup radioGroup;

    public EventDialogFragment(){}
    public static EventDialogFragment getInstance(){
        EventDialogFragment e = new EventDialogFragment();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_location, container);

        Intent intent = new Intent(v.getContext(), MainActivity.class);
        radioGroup = v.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getId() == R.id.radio_group) {
                    switch (checkedId) {
                        case R.id.radio_button_bookchon:
                            intent.putExtra("location", "북촌한옥마을");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_seochon:
                            intent.putExtra("location", "서촌");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_sangsu:
                            intent.putExtra("location", "상수");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_seoulforest:
                            intent.putExtra("location", "서울숲성수");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_sinsa:
                            intent.putExtra("location", "신사");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_daehakro:
                            intent.putExtra("location", "대학로");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_hannam:
                            intent.putExtra("location", "한남");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_hongdae:
                            intent.putExtra("location", "홍대");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_eulgiro:
                            intent.putExtra("location", "을지로");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_ikseondong:
                            intent.putExtra("location", "익선동");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_itaewon:
                            intent.putExtra("location", "이태원");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_gangnam:
                            intent.putExtra("location", "강남");
                            startActivity(intent);
                            break;
                        case R.id.radio_button_gundae:
                            intent.putExtra("location", "건대뚝섬");
                            startActivity(intent);
                            break;
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View v){
        //dismiss();
    }
}

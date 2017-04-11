/*
 *
 *   Copyright (C) 2017 author : 梁桂栋
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Email me : stonelavender@hotmail.com
 *
 */

package dong.lan.smarttrip.ui.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import java.util.Calendar;

import dong.lan.smarttrip.R;


/**
 * Created by 梁桂栋 on 2016年09月02日 21:24.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class DateTimePicker extends AlertDialog {

    private ViewFlipper switcher;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar calendar;
    private RadioButton dateRb;
    private RadioButton timeRb;
    private CallBack callBack;
    private boolean isInit = false;
    private int index = 0;
    private TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int i, int i1) {
            calendar.set(Calendar.HOUR, i);
            calendar.set(Calendar.MINUTE, i1);
        }
    };
    private DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DATE, i2);
        }
    };


    public DateTimePicker(Context context, CallBack callBackListener) {
        super(context);
        this.callBack = callBackListener;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        View view = LayoutInflater.from(context).inflate(R.layout.date_time_picker, null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        TextView back = (TextView) view.findViewById(R.id.left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePicker.this.dismiss();
            }
        });
        TextView ok = (TextView) view.findViewById(R.id.right);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(callBack);
                dismiss();
            }
        });
        switcher = (ViewFlipper) view.findViewById(R.id.switcher);
        RadioGroup dateCheck = (RadioGroup) view.findViewById(R.id.dateTimeCheck);
        dateRb = (RadioButton) view.findViewById(R.id.dateCheck);
        timeRb = (RadioButton) view.findViewById(R.id.timeCheck);
        dateCheck.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.dateCheck && index == 1) {
                            switcher.showPrevious();
                            index = 0;
                        } else if (i == R.id.timeCheck && index == 0) {
                            switcher.showNext();
                            index = 1;
                        }
                    }
                }
        );
        setView(view);
        reset(System.currentTimeMillis());
        isInit = true;
    }

    public long timeOfHourAndMinute(){
        return calendar.get(Calendar.HOUR) * 3600000  + calendar.get(Calendar.MINUTE) * 6000;
    }

    public long timeOfYearDay(){
        return calendar.getTimeInMillis() - timeOfHourAndMinute();
    }

    @Override
    public void show() {
        if (isShowing())
            return;
        super.show();
    }

    public void show(boolean isTimePager) {
        if (isTimePager)
            timeRb.setChecked(true);
        else
            dateRb.setChecked(true);
        show();
    }

    public void reset(long minTime) {
        datePicker.setMinDate(minTime);
        calendar.setTimeInMillis(minTime);
        if (isInit) {
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE));
            if (Build.VERSION.SDK_INT < 23) {
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
            } else {
                timePicker.setHour(calendar.get(Calendar.HOUR));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
            }
        } else {
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE), dateChangedListener);
            timePicker.setOnTimeChangedListener(timeChangedListener);
        }
    }

    public DateTimePicker getTime(CallBack callBack) {
        callBack.onClose(calendar.getTimeInMillis());
        return this;
    }


    public interface CallBack {
        void onClose(long time);
    }
}

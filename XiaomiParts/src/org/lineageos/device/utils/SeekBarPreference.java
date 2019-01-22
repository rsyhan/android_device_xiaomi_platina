/*
 * Copyright (c) 2015 The CyanogenMod Project
 *               2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lineageos.device.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import android.os.Handler;
import android.os.Message;

import org.lineageos.device.R;
import org.lineageos.device.UtilsKCAL;

public class SeekBarPreference extends Preference {

    public int minimum = 1;
    public int maximum = 256;
    public int def = 256;
    public int interval = 1;

    final int UPDATE = 0;

    int currentValue = def;

    private OnPreferenceChangeListener changer;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, 0, 0);

        minimum = typedArray.getInt(R.styleable.SeekBarPreference_min_value, minimum);
        maximum = typedArray.getInt(R.styleable.SeekBarPreference_max_value, maximum);
        def = typedArray.getInt(R.styleable.SeekBarPreference_default_value, def);

        typedArray.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    private void bind(final View layout) {
        final EditText monitorBox = (EditText) layout.findViewById(R.id.monitor_box);
        final SeekBar bar = (SeekBar) layout.findViewById(R.id.seek_bar);

        monitorBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        bar.setMax(maximum - minimum);
        bar.setProgress(currentValue - minimum);        

        monitorBox.setText(String.valueOf(currentValue));
        monitorBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    monitorBox.setSelection(monitorBox.getText().length());
            }
        });

        monitorBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    currentValue = (int) UtilsKCAL.clamp(Integer.parseInt(v.getText().toString()),minimum,maximum);
                    monitorBox.setText(String.valueOf(currentValue));
                    bar.setProgress(currentValue - minimum, true);
                    changer.onPreferenceChange(SeekBarPreference.this, Integer.toString(currentValue));
                    return true;
                }
                return false;
            }
        });

        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = Math.round(((float) progress) / interval) * interval;
                currentValue = progress + minimum;
                monitorBox.setText(String.valueOf(currentValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changer.onPreferenceChange(SeekBarPreference.this, Integer.toString(currentValue));
            }
        });
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        bind(view);
    }

    public void setInitValue(int progress) {
        currentValue = progress;
    }

    @Override
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        changer = onPreferenceChangeListener;
        super.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }

    public int reset() {
        currentValue = (int) UtilsKCAL.clamp(def, minimum, maximum);
        notifyChanged();
        return currentValue;
    }

    public void setValue(int progress) {
        currentValue = (int) UtilsKCAL.clamp(progress, minimum, maximum);
        notifyChanged();
    }
}

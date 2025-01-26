package com.ibntaymiyya.gym.model;

import com.sun.jdi.Value;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;

public class SpinnerModel {


    public static IntegerSpinnerModel integerSpinnerModel(int min, int max , int increment, int value){
        IntegerSpinnerModel spinnerModel = new IntegerSpinnerModel();
        spinnerModel.setMin(min);
        spinnerModel.setMax(max);
        spinnerModel.setIncrement(increment);
        spinnerModel.setValue(value);

        return spinnerModel;
    }
}

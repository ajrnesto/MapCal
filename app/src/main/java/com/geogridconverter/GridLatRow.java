package com.geogridconverter;

import com.google.gson.annotations.SerializedName;

public class GridLatRow {
    @SerializedName("LAT_DEG")
    int LAT_DEG;
    @SerializedName("LAT_MIN")
    int LAT_MIN;
    @SerializedName("I")
    double I;
    @SerializedName("I_DIFF")
    double I_DIFF;
    @SerializedName("VII")
    double VII;
    @SerializedName("VII_DIFF")
    double VII_DIFF;
    @SerializedName("VIII")
    double VIII;
    @SerializedName("IX")
    double IX;
    @SerializedName("IX_DIFF")
    double IX_DIFF;
    @SerializedName("X")
    double X;
    @SerializedName("X_DIFF")
    double X_DIFF;
    @SerializedName("XI")
    double XI;

    public GridLatRow(int LAT_DEG, int LAT_MIN, double i, double i_DIFF, double VII, double VII_DIFF, double VIII, double IX, double IX_DIFF, double x, double x_DIFF, double XI) {
        this.LAT_DEG = LAT_DEG;
        this.LAT_MIN = LAT_MIN;
        I = i;
        I_DIFF = i_DIFF;
        this.VII = VII;
        this.VII_DIFF = VII_DIFF;
        this.VIII = VIII;
        this.IX = IX;
        this.IX_DIFF = IX_DIFF;
        X = x;
        X_DIFF = x_DIFF;
        this.XI = XI;
    }

    public int getLAT_DEG() {
        return LAT_DEG;
    }

    public void setLAT_DEG(int LAT_DEG) {
        this.LAT_DEG = LAT_DEG;
    }

    public int getLAT_MIN() {
        return LAT_MIN;
    }

    public void setLAT_MIN(int LAT_MIN) {
        this.LAT_MIN = LAT_MIN;
    }

    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

    public double getI_DIFF() {
        return I_DIFF;
    }

    public void setI_DIFF(double i_DIFF) {
        I_DIFF = i_DIFF;
    }

    public double getVII() {
        return VII;
    }

    public void setVII(double VII) {
        this.VII = VII;
    }

    public double getVII_DIFF() {
        return VII_DIFF;
    }

    public void setVII_DIFF(double VII_DIFF) {
        this.VII_DIFF = VII_DIFF;
    }

    public double getVIII() {
        return VIII;
    }

    public void setVIII(double VIII) {
        this.VIII = VIII;
    }

    public double getIX() {
        return IX;
    }

    public void setIX(double IX) {
        this.IX = IX;
    }

    public double getIX_DIFF() {
        return IX_DIFF;
    }

    public void setIX_DIFF(double IX_DIFF) {
        this.IX_DIFF = IX_DIFF;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getX_DIFF() {
        return X_DIFF;
    }

    public void setX_DIFF(double x_DIFF) {
        X_DIFF = x_DIFF;
    }

    public double getXI() {
        return XI;
    }

    public void setXI(double XI) {
        this.XI = XI;
    }
}

package com.geogridconverter;

import com.google.gson.annotations.SerializedName;

public class GeoLatRow {
    @SerializedName("LAT_DEG")
    int LAT_DEG;
    @SerializedName("LAT_MIN")
    int LAT_MIN;
    @SerializedName("I")
    double I;
    @SerializedName("I_DIFF")
    double I_DIFF;
    @SerializedName("II")
    double II;
    @SerializedName("II_DIFF")
    double II_DIFF;
    @SerializedName("III")
    double III;
    @SerializedName("IV")
    double IV;
    @SerializedName("IV_DIFF")
    double IV_DIFF;
    @SerializedName("V")
    double V;
    @SerializedName("V_DIFF")
    double V_DIFF;
    @SerializedName("VI")
    double VI;

    public GeoLatRow(int LAT_DEG, int LAT_MIN, double i, double i_DIFF, double II, double II_DIFF, double III, double IV, double IV_DIFF, double v, double v_DIFF, double VI) {
        this.LAT_DEG = LAT_DEG;
        this.LAT_MIN = LAT_MIN;
        I = i;
        I_DIFF = i_DIFF;
        this.II = II;
        this.II_DIFF = II_DIFF;
        this.III = III;
        this.IV = IV;
        this.IV_DIFF = IV_DIFF;
        V = v;
        V_DIFF = v_DIFF;
        this.VI = VI;
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

    public double getII() {
        return II;
    }

    public void setII(double II) {
        this.II = II;
    }

    public double getII_DIFF() {
        return II_DIFF;
    }

    public void setII_DIFF(double II_DIFF) {
        this.II_DIFF = II_DIFF;
    }

    public double getIII() {
        return III;
    }

    public void setIII(double III) {
        this.III = III;
    }

    public double getIV() {
        return IV;
    }

    public void setIV(double IV) {
        this.IV = IV;
    }

    public double getIV_DIFF() {
        return IV_DIFF;
    }

    public void setIV_DIFF(double IV_DIFF) {
        this.IV_DIFF = IV_DIFF;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }

    public double getV_DIFF() {
        return V_DIFF;
    }

    public void setV_DIFF(double v_DIFF) {
        V_DIFF = v_DIFF;
    }

    public double getVI() {
        return VI;
    }

    public void setVI(double VI) {
        this.VI = VI;
    }
}

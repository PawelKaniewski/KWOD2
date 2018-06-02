package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

import trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine.Medicine;

public class CustomizedMedicine {
    private int id;
    private Medicine medicine;
    private int frequency;
    private int portion;
    private String unit;

    public CustomizedMedicine(int id, Medicine medicine, int frequency, int portion, String unit) {
        this.frequency = frequency;
        this.medicine = medicine;
        this.portion = portion;
        this.unit = unit;
        this.id = id;
    }


    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


}

package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

public class MedicineNotification {

    private boolean isNotificationOn;
    private int hours;
    private int mins;
    private int customizedMedicineID;

    public boolean isNotificationOn() {
        return isNotificationOn;
    }

    public void setNotificationOn(boolean notificationOn) {
        isNotificationOn = notificationOn;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getCustomizedMedicineID() {
        return customizedMedicineID;
    }

    public void setCustomizedMedicineID(int customizedMedicineID) {
        this.customizedMedicineID = customizedMedicineID;
    }



}

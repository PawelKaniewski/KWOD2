package trochimiuk.kaniewski.czaplicka.kwod.pl.healthcareapp.Medicine;

public class Medicine {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Medicine(String name, String description){
        this.name = name;
        this.description = description;
    }

}

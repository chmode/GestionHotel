package model;

public class RoomTypes {
    private int id;
    private String label;
    private String description;
    private int nbrPersone;

    public RoomTypes(){
        
    }

    public RoomTypes(int id, String label, String description, int nbrPersone){
        this.id = id;
        this.label = label;
        this.description = description;
        this.nbrPersone = nbrPersone;
    }
    
        public RoomTypes(String label, String description, int nbrPersone){
        this.label = label;
        this.description = description;
        this.nbrPersone = nbrPersone;
    }
        
        
    
    public int getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }    
    public String getDescription() {
        return description;
    }    
    public int getNbrPersone() {
        return nbrPersone;
    }

    
    public void setId(int id) {
        this.id = id;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setNbrPersone(int nbrPersone) {
        this.nbrPersone = nbrPersone;
    }
    
    @Override
    public String toString(){
        return "RoomTypes [id=" + id + ", label=" + label + ", description=" + description + ", nbrPersone=" + nbrPersone + "]";
    }
}


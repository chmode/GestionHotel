package model;

public class Room {
    private int id;
    private int typeId;
    private int matricule;
    private boolean status;
    
    public Room(){
        
    }
    
    public Room(int id, int typeId, int matricule, boolean status){
        this.id = id;
        this.typeId = typeId;
        this.matricule = matricule;
        this.status = status;
    }
    
    public Room(int typeId, int matricule, boolean status){
        this.typeId = typeId;
        this.matricule = matricule;
        this.status = status;
    }   
    
    
    public int getId() {
        return id;
    }
    public int getTypeId() {
        return typeId;
    }
    public int getMatricule() {
        return matricule;
    }
    public boolean getStatus() {
        return status;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    @Override
    public String toString(){
        return "Room [id=" + id + ",typeId=" + typeId + ",matricule=" + matricule + ",status=" + status + "]";
    }
}

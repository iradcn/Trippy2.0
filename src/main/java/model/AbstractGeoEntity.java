package model;

/**
 * Created by nimrod on 5/24/15.
 */
public abstract class AbstractGeoEntity {
    private int id;
    private String name;
    private String yagoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGeoEntity that = (AbstractGeoEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (yagoId != null ? !yagoId.equals(that.yagoId) : that.yagoId != null) return false;
        return myType == that.myType;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (yagoId != null ? yagoId.hashCode() : 0);
        result = 31 * result + (myType != null ? myType.hashCode() : 0);
        return result;
    }

    public enum typeOf{
        Category,
        City,
        Country,
        Place
    };
    protected typeOf myType;

    public typeOf getType(){
        return this.myType;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYagoId() {
        return yagoId;
    }

    public void setYagoId(String yagoId) {
        this.yagoId = yagoId;
    }
}

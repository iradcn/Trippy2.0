package model;

/**
 * Created by nimrod on 5/24/15.
 */
public class Category extends AbstractGeoEntity {

    public Category(String yagoId) {
        super.setYagoId(yagoId);
        super.myType = typeOf.Category;

    }
}

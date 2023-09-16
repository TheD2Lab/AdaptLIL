package data_classes;

import geometry.Shape;

public class DomElement {
    private String id;
    private String className;
    private Shape shape;

    public DomElement(String id, String className, Shape coordsAndDimens) {
        this.id = id;
        this.className = className;
        this.shape = coordsAndDimens;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Shape getShape() {
        return shape;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}

package adaptlil.data_classes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
/**
 * 5.13 Cursor position
 * Description: The position of the mouse cursor.
 * Parameter ID: CX, CY
 * Parameter type: float
 * Parameter description: The X- and Y-coordinates of the mouse cursor, as percentage of the screen size.
 * Parameter ID: CS (0 for idle, 1 for left mouse button down, 2 for right button down, 3 for left
 * button up, 4 for right button up.
 * Parameter type: integer
 * Parameter description: Mouse cursor state, 0 for steady state, 1 for left button down, 2 for right button
 * down, 3 for left button up, 4 for right button up
 */
public class Cursor {

    @JacksonXmlProperty(isAttribute = true, localName = "CX")
    public Float x;
    @JacksonXmlProperty(isAttribute = true, localName = "CY")
    public Float y;
    @JacksonXmlProperty(isAttribute = true,localName = "CS")
    public Integer cursorState;

    /**
     * Default constructor for Jackson Serialization
     */
    public Cursor() {}

    public Cursor(Float x, Float y, Integer cursorState) {
        this.x = x;
        this.y = y;
        this.cursorState = cursorState;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public Integer getCursorState() {
        return cursorState;
    }
}

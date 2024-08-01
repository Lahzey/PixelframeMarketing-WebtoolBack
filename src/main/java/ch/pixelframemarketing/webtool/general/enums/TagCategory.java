package ch.pixelframemarketing.webtool.general.enums;

public enum TagCategory {
    TIME("Time", "#a25e52"),
    PLACE("Place", "#7fb0d5"),
    STYLE("Style", "#d2c065"),
    MISC("Miscellaneous", "#777777");
    
    public final String name;
    public final String color;
    
    TagCategory(String name, String color) {
        this.name = name;
        this.color = color;
    }
    
}

package view.styles;

import java.awt.*;
import java.io.InputStream;

public class FontUtil {
    public static Font loadFont(float tamaño, String fuente) {
        try {
            InputStream is = FontUtil.class.getClassLoader().getResourceAsStream("fonts/" + fuente + ".ttf");
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar la fuente.");
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(tamaño);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) tamaño);
        }
    }
}

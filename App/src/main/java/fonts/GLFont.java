package fonts;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GLFont {

    private static final GLFont INSTANCE = new GLFont();

    /*
     * A list of "logical fonts", based on an article from the oracle website.
     */
    private final TrueTypeFont logicalFonts[] = new TrueTypeFont[5];
    private final String fontNames[] = { "Dialog", "DialogInput", "Monospaced", "Serif", "SansSerif" };

    /*
     * A list of custom fonts which the Two-D developer can implement.
     */
    private Map<String, TrueTypeFont> customFonts = new HashMap<String, TrueTypeFont>();

    // TODO store a texture instance of the custom fonts, instead of using a seperate system.
    // If you are using slick2D, just ignore this. I use my own texture/texturemanager classes
    // as part of the learning process for me is to not use slick2d
    //private List<Texture> fontTextures = new CopyOnWriteArrayList<Texture>();

    public enum FontType {
        Dialog, DialogInput, Monospaced, Serif, SansSerif
    }

    public GLFont() {
        for (int i = 0; i < logicalFonts.length; i++) {
            logicalFonts[i] = new TrueTypeFont(new Font(fontNames[i], Font.PLAIN, 12), true);
        }

        // just testing custom fonts
        addCustomFont("Arial", false, 12, true);
        addCustomFont("Helvetica", false, 12, true);
    }


    public void drawString(String string, int x, int y, int scaleX, int scaleY, int format, FontType fontType) {
        switch (fontType) {
            case Dialog:
                logicalFonts[0].drawString(x, y, string, scaleX, scaleY, format);
                break;
            case DialogInput:
                logicalFonts[1].drawString(x, y, string, scaleX, scaleY, format);
                break;
            case Monospaced:
                logicalFonts[2].drawString(x, y, string, scaleX, scaleY, format);
                break;
            case Serif:
                logicalFonts[3].drawString(x, y, string, scaleX, scaleY, format);
                break;
            case SansSerif:
                logicalFonts[4].drawString(x, y, string, scaleX, scaleY, format);
                break;
            default:
                System.err.println("Unknown font type selected!");
                System.exit(1);
                break;
        }
    }

    /**
     * Adds a custom font to memory using the given variables. This essentially just creates a java.awt.Font
     * and converts it to a TrueFontType instance for lwjgl display.
     *
     * @param fontName The name of the given font. This will use a java.awt.Font instance to create
     * an lwjgl suitable TrueTypeFont.
     * @param bold Whether or not the font will be bold.
     * @param fontSize The size of the font.
     * @param antiAlias Whether or not you want to use anti aliasing for the custom font.
     */
    public void addCustomFont(String fontName, boolean bold, int fontSize, boolean antiAlias) {
        try {
            TrueTypeFont ttf = new TrueTypeFont(new Font(fontName, bold ? Font.BOLD : Font.PLAIN, fontSize), antiAlias);
            customFonts.put(fontName.toLowerCase(), ttf);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error adding custom font: " + fontName);
        }
    }

    /**
     * Draws a String at the given x, y coordinates, using the specified customFont type.
     *
     * @param string The string to be rendered.
     * @param x The x coordinate in 2D space.
     * @param y The y coordinate in 2D space.
     * @param format The string format. 0 = align left, 1 = align right, 2 = align center
     * @param customFont The name of the custom font. This would have been set when adding a
     * new font using {@link #addCustomFont(String, boolean, int, boolean)}
     */
    public void drawString(String string, int x, int y, int format, String customFont) {
        try {
            customFonts.get(customFont.toLowerCase()).drawString(x, y, string, 1, 1, format);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error drawing string using custom font: " + customFont);
        }
    }

    /**
     *
     * Draws a String at the given x, y coordinates, using the specified customFont type.
     *
     * @param string The string to be rendered.
     * @param x The x coordinate in 2D space.
     * @param y The y coordinate in 2D space.
     * @param scaleX The scale (size) you want to use while drawing the string.
     * @param scaleY The scale (size) you want to use while drawing the string.
     * @param format The string format. 0 = align left, 1 = align right, 2 = align center
     * @param customFont The name of the custom font. This would have been set when adding a
     * new font using {@link #addCustomFont(String, boolean, int, boolean)}
     */
    public void drawString(String string, int x, int y, int scaleX, int scaleY, int format, String customFont) {
        try {
            customFonts.get(customFont.toLowerCase()).drawString(x, y, string, scaleX, scaleY, format);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error drawing string using custom font: " + customFont);
        }
    }

    public static GLFont getInstance() {
        return INSTANCE;
    }

}

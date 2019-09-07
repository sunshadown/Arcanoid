package fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import app.Model;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * A TrueType font implementation originally for Slick, edited for Bobjob's
 * Engine by David Aaron Muhar (bobjob), then edited by Yousef Amar
 * (Paraknight).
 *
 * Further modified on 2/11/2015 for use with the Two-D lwjgl 2D wrapper, by Minigame.
 *
 * @author James Chambers (Jimmy)
 * @author Jeremy Adams (elias4444)
 * @author Kevin Glass (kevglass)
 * @author Peter Korzuszek (genail)
 * @author David Aaron Muhar (bobjob)
 * @author Yousef Amar (Paraknight)
 * @author Corey K****** (Minigame)
 */
public class TrueTypeFont extends Model {

    public final static int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;
    /** Array that holds necessary information about the font characters */
    private IntObject[] charArray = new IntObject[256];
    /** Map of user defined font characters (Character <-> IntObject) */
    private Map<java.lang.Character, IntObject> customChars = new HashMap<java.lang.Character, IntObject>();

    /** Boolean flag on whether AntiAliasing is enabled or not */
    private boolean antiAlias;

    /** Font's size */
    private int fontSize = 0;

    /** Font's height */
    private int fontHeight = 0;

    /** Texture used to cache the font 0-255 characters */
    private int fontTextureID;

    /** Default font texture width */
    private int textureWidth = 512;

    /** Default font texture height */
    private int textureHeight = 512;

    /** A reference to Java's AWT Font that we create our font texture from */
    private Font font;

    /** The font metrics for our Java AWT font */
    private FontMetrics fontMetrics;

    private int correctL = 9, correctR = 8;

    // TODO : create a texture cache in GLFont specifically for font textures!
    // we will need to enable alpha for the texture instance, so that we can draw Strings
    // without having to call gl blend/alpha settings locally in the TTF class.
    // until that is done, there could be compatibility issues with games powered by
    // Two-D, or possible unnecessary overhead due to settings not being disabled
    private static AtomicInteger fontId = new AtomicInteger(0);

    @Override
    public void Init() {

    }

    @Override
    public void Update(float dt) {

    }

    @Override
    public void Render(Matrix4f view, Matrix4f proj, Matrix4f ortho) {

    }

    private class IntObject {
        /** Character's width */
        public int width;

        /** Character's height */
        public int height;

        /** Character's stored x position */
        public int storedX;

        /** Character's stored y position */
        public int storedY;
    }

    public TrueTypeFont(Font font, boolean antiAlias, char[] additionalChars) {
        this.font = font;
        this.fontSize = font.getSize() + 3;
        this.antiAlias = antiAlias;

        createSet(additionalChars);

        fontHeight -= 1;
        if (fontHeight <= 0)
            fontHeight = 1;
    }

    public TrueTypeFont(Font font, boolean antiAlias) {
        this(font, antiAlias, null);
    }

    public void setCorrection(boolean on) {
        if (on) {
            correctL = 2;
            correctR = 1;
        } else {
            correctL = 0;
            correctR = 0;
        }
    }

    private BufferedImage getFontImage(char ch) {
        // Create a temporary image to extract the character's size
        BufferedImage tempfontImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
        if (antiAlias == true) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        fontMetrics = g.getFontMetrics();
        int charwidth = fontMetrics.charWidth(ch) + 8;

        if (charwidth <= 0) {
            charwidth = 7;
        }
        int charheight = fontMetrics.getHeight() + 3;
        if (charheight <= 0) {
            charheight = fontSize;
        }

        // Create another image holding the character we are creating
        BufferedImage fontImage;
        fontImage = new BufferedImage(charwidth, charheight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D gt = (Graphics2D) fontImage.getGraphics();
        if (antiAlias == true) {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        gt.setFont(font);
        gt.setColor(Color.WHITE);
        int charx = 3;
        int chary = 1;
        gt.drawString(String.valueOf(ch), (charx), (chary) + fontMetrics.getAscent());
        return fontImage;

    }

    private void createSet(char[] customCharsArray) {
        // If there are custom chars then I expand the font texture twice
        if (customCharsArray != null && customCharsArray.length > 0) {
            textureWidth *= 2;
        }

        // In any case this should be done in other way. Texture with size
        // 512x512
        // can maintain only 256 characters with resolution of 32x32. The
        // texture
        // size should be calculated dynamicaly by looking at character sizes.

        try {

            BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) imgTemp.getGraphics();

            g.setColor(new Color(0, 0, 0, 1));
            g.fillRect(0, 0, textureWidth, textureHeight);

            int rowHeight = 0;
            int positionX = 0;
            int positionY = 0;

            int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0;

            for (int i = 0; i < 256 + customCharsLength; i++) {

                // get 0-255 characters and then custom characters
                char ch = (i < 256) ? (char) i : customCharsArray[i - 256];

                BufferedImage fontImage = getFontImage(ch);

                IntObject newIntObject = new IntObject();

                newIntObject.width = fontImage.getWidth();
                newIntObject.height = fontImage.getHeight();

                if (positionX + newIntObject.width >= textureWidth) {
                    positionX = 0;
                    positionY += rowHeight;
                    rowHeight = 0;
                }

                newIntObject.storedX = positionX;
                newIntObject.storedY = positionY;

                if (newIntObject.height > fontHeight) {
                    fontHeight = newIntObject.height;
                }

                if (newIntObject.height > rowHeight) {
                    rowHeight = newIntObject.height;
                }

                // Draw it here
                g.drawImage(fontImage, positionX, positionY, null);

                positionX += newIntObject.width;

                if (i < 256) { // standard characters
                    charArray[i] = newIntObject;
                } else { // custom characters
                    customChars.put(new java.lang.Character(ch), newIntObject);
                }

                fontImage = null;
            }

            fontTextureID = loadImage(imgTemp);

        } catch (Exception e) {
            System.err.println("Failed to create font.");
            e.printStackTrace();
        }
    }

    private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2) {
        float DrawWidth = drawX2 - drawX;
        float DrawHeight = drawY2 - drawY;
        float TextureSrcX = srcX / textureWidth;
        float TextureSrcY = srcY / textureHeight;
        float SrcWidth = srcX2 - srcX;
        float SrcHeight = srcY2 - srcY;
        float RenderWidth = (SrcWidth / textureWidth);
        float RenderHeight = (SrcHeight / textureHeight);

        glTexCoord2f(TextureSrcX, TextureSrcY);
        glVertex2f(drawX, drawY + DrawHeight);
        glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
        glVertex2f(drawX + DrawWidth, drawY + DrawHeight);
        glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight);
        glVertex2f(drawX + DrawWidth, drawY);
        glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
        glVertex2f(drawX, drawY);
    }

    public int getWidth(String whatchars) {
        int totalwidth = 0;
        IntObject intObject = null;
        int currentChar = 0;
        for (int i = 0; i < whatchars.length(); i++) {
            currentChar = whatchars.charAt(i);
            if (currentChar < 256) {
                intObject = charArray[currentChar];
            } else {
                intObject = (IntObject) customChars.get(new java.lang.Character((char) currentChar));
            }
            if (intObject != null) {
                totalwidth += intObject.width;
            }
        }
        return totalwidth;
    }

    public int getHeight() {
        return fontHeight;
    }

    public int getHeight(String HeightString) {
        return fontHeight;
    }

    public int getLineHeight() {
        return fontHeight;
    }

    public void drawString(float x, float y, String whatchars, float scaleX, float scaleY) {
        drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY, ALIGN_LEFT);
    }

    public void drawString(float x, float y, String whatchars, float scaleX, float scaleY, int format) {
        drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY, format);
    }

    public void drawString(float x, float y, String whatchars, int startIndex, int endIndex, float scaleX, float scaleY, int format) {
        IntObject intObject = null;
        int charCurrent;
        int totalwidth = 0;
        int i = startIndex, d, c;
        float startY = 0;

        switch (format) {
            case ALIGN_RIGHT: {
                d = -1;
                c = correctR;
                while (i < endIndex) {
                    if (whatchars.charAt(i) == '\n') {
                        startY -= fontHeight;
                    }
                    i++;
                }
            }
            case ALIGN_CENTER: {
                for (int l = startIndex; l <= endIndex; l++) {
                    charCurrent = whatchars.charAt(l);
                    if (charCurrent == '\n') {
                        break;
                    }
                    if (charCurrent < 256) {
                        intObject = charArray[charCurrent];
                    } else {
                        intObject = (IntObject) customChars.get(new java.lang.Character((char) charCurrent));
                    }
                    totalwidth += intObject.width - correctL;
                }
                totalwidth /= -2;
            }
            case ALIGN_LEFT:
            default:
                d = 1;
                c = correctL;
                break;
        }

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindTexture(GL_TEXTURE_2D, fontTextureID);
        //TextureManager.getInstance().getTextureById(fontTextureID).bind();
        glBegin(GL_QUADS);

        while (i >= startIndex && i <= endIndex) {
            charCurrent = whatchars.charAt(i);
            if (charCurrent < 256) {
                intObject = charArray[charCurrent];
            } else {
                intObject = (IntObject) customChars.get(new java.lang.Character((char) charCurrent));
            }
            if (intObject != null) {
                if (d < 0)
                    totalwidth += (intObject.width - c) * d;
                if (charCurrent == '\n') {
                    startY -= fontHeight * d;
                    totalwidth = 0;
                    if (format == ALIGN_CENTER) {
                        for (int l = i + 1; l <= endIndex; l++) {
                            charCurrent = whatchars.charAt(l);
                            if (charCurrent == '\n')
                                break;
                            if (charCurrent < 256) {
                                intObject = charArray[charCurrent];
                            } else {
                                intObject = (IntObject) customChars
                                        .get(new java.lang.Character((char) charCurrent));
                            }
                            totalwidth += intObject.width - correctL;
                        }
                        totalwidth /= -2;
                    }
                    // if center get next lines total width/2;
                } else {
                    drawQuad((totalwidth + intObject.width) * scaleX + x,
                            startY * scaleY + y, totalwidth * scaleX + x,
                            (startY + intObject.height) * scaleY + y,
                            intObject.storedX + intObject.width,
                            intObject.storedY + intObject.height,
                            intObject.storedX, intObject.storedY);
                    if (d > 0)
                        totalwidth += (intObject.width - c) * d;
                }
                i += d;

            }
        }
        glEnd();
        glDisable(GL_BLEND);
    }

    public static int loadImage(BufferedImage bufferedImage) {
        // TODO : store the bufferedimage as a com.javarpg.twod.Texture instance
        // in the GLFont class. we need to keep track of font textures,
        // but cannot store them locally in the TTF class..
        try {
            short width = (short) bufferedImage.getWidth();
            short height = (short) bufferedImage.getHeight();
            // textureLoader.bpp = bufferedImage.getColorModel().hasAlpha() ?
            // (byte)32 : (byte)24;
            int bpp = (byte) bufferedImage.getColorModel().getPixelSize();
            ByteBuffer byteBuffer;
            DataBuffer db = bufferedImage.getData().getDataBuffer();
            if (db instanceof DataBufferInt) {
                int intI[] = ((DataBufferInt) (bufferedImage.getData().getDataBuffer())).getData();
                byte newI[] = new byte[intI.length * 4];
                for (int i = 0; i < intI.length; i++) {
                    byte b[] = intToByteArray(intI[i]);
                    int newIndex = i * 4;
                    newI[newIndex] = b[1];
                    newI[newIndex + 1] = b[2];
                    newI[newIndex + 2] = b[3];
                    newI[newIndex + 3] = b[0];
                }
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(newI);
            } else {
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(((DataBufferByte) (bufferedImage.getData() .getDataBuffer())).getData());
            }
            byteBuffer.flip();

            int internalFormat = GL_RGBA8, format = GL_RGBA;
            IntBuffer textureId = BufferUtils.createIntBuffer(1);

            glGenTextures(textureId);
            glBindTexture(GL_TEXTURE_2D, textureId.get(0));

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,GL_MODULATE);
            glGenerateMipmap(GL_TEXTURE_2D);
            //gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, internalFormat, width, height, format, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            return textureId.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    public static boolean isSupported(String fontname) {
        Font font[] = getFonts();
        for (int i = font.length - 1; i >= 0; i--) {
            if (font[i].getName().equalsIgnoreCase(fontname)) {
                return true;
            }
        }
        return false;
    }

    public static Font[] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    public void destroy() {
        IntBuffer scratch = BufferUtils.createIntBuffer(1);
        scratch.put(0, fontTextureID);
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(scratch);
    }
}
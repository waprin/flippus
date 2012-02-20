package us.flipp.simulation;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import us.flipp.utility.NL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class World {

    public enum Color {
        BLUE, RED
    }

    static private Map<String, Color> colorMap;
    static {
        colorMap = new HashMap<String, Color>();
        colorMap.put("blue", Color.BLUE);
        colorMap.put("red", Color.RED);
    }


    private Color[][] startBoard;
    private Color[][] currentBoard;

    private static final String TAG = World.class.getName();
    private String identifier;
    private String author;

    public Color[][] getStartBoard() {
        return startBoard;
    }

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValid() {
        return isValid;
    }

    private boolean isValid;

    protected void loadFromXML(InputStream inputStream) throws IOException
    {
        try
        {
            javax.xml.parsers.DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
            db.setValidating(false);
            db.setCoalescing(false);
            db.setExpandEntityReferences(false);
            javax.xml.parsers.DocumentBuilder documentBuilder = db.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element root = document.getDocumentElement();
            if (!loadProperties(root)) {
                Log.e(TAG, "failure to load properties");
                return;
            }
            if (!loadBoard(root)) {
                Log.e(TAG, "failure to load board");
                return;
            }
            isValid = true;
        }
        catch(ParserConfigurationException e)
        {
            throw new IOException("Unable to create parser to read xml: " + e.getMessage());
        }
        catch (SAXException e)
        {
            throw new IOException("Unable to load level due to SAX exception " + e.getMessage());
        }
        catch (InvalidParameterException e)
        {
            throw new IOException("Unable to load level due to XML parameter error : "+ e.getMessage());
        }
    }

    private boolean loadProperties(Element root)
    {
        NodeList authorNodes = NL.ElementsByTag(root, "Author");
        if (authorNodes.getLength() >= 1)
        {
            String author = authorNodes.item(0).getFirstChild().getNodeValue();
            Log.d(TAG, "and sanity check as author is " + author);
            if (author != null) {
                Log.d(TAG, "found author and setting to " + author);
                this.author = author;
            } else {
                return false;
            }
        } else {
            return false;
        }
        NodeList sizeNode = NL.ElementsByTag(root, "Size");
        if (sizeNode.getLength() == 1) {
            NamedNodeMap sizeMap = sizeNode.item(0).getAttributes();
            Node widthNode = sizeMap.getNamedItem("width");
            if (widthNode == null) {
                Log.e(TAG, "loading level: no width specified.");
                return false;
            }
            Node heightNode = sizeMap.getNamedItem("height");
            if (heightNode == null) {
                Log.e(TAG, "loading level: no height specified.");
                return false;
            }
            this.height = Integer.parseInt(heightNode.getNodeValue());
            Log.d(TAG, "set height to " + height);
            this.width = Integer.parseInt(widthNode.getNodeValue());
            Log.d(TAG, "set width to " + width);
        }
        else {
            return false;
        }
        return true;
    }

    private boolean loadBoard(Element root)
    {
        this.startBoard = new Color[width][height];
        NodeList vertexNodes = NL.ElementsByTag(root, "V");
        if (vertexNodes.getLength() == width * height) {
            for (int i = 0; i < vertexNodes.getLength(); i++) {
                Node vertex = vertexNodes.item(i);
                NamedNodeMap vertexMap = vertex.getAttributes();
                Node xNode = vertexMap.getNamedItem("x");
                if (xNode == null) {
                    Log.e(TAG, "loading vertices: did not contain x coord");
                    return false;
                }
                int x = Integer.parseInt(xNode.getNodeValue());
                if (x >= width) {
                    Log.e(TAG, "x value larger than width");
                    return false;
                }
                Node yNode = vertexMap.getNamedItem("y");
                if (yNode == null) {
                    Log.e(TAG, "loading vertices: did not contain y coord");
                    return false;
                }
                int y = Integer.parseInt(yNode.getNodeValue());
                if (y >= height) {
                    Log.e(TAG, "y value larger than heihht");
                    return false;
                }

                Node colorNode = vertexMap.getNamedItem("color");
                if (colorNode == null) {
                    Log.e(TAG, "loading vertices: did not contain color value");
                    return false;
                }

                String colorStr = colorNode.getNodeValue();

                Color color = colorMap.get(colorStr);
                if (color == null) {
                    Log.e(TAG, "unrecognized color value");
                    return false;
                }
                if (startBoard[x][y] != null) {
                    Log.e(TAG, "board location x, y already taken");
                    return false;
                }
                startBoard[x][y] = color;
            }
        } else {
            Log.d(TAG, "incorrectly specified number of vertices");
            return false;
        }
        return true;
    }

    public World(InputStream inputStream) throws IOException {
        isValid = false;
        loadFromXML(inputStream);
    }

    public String getAuthor() {
        return author;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

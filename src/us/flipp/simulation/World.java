package us.flipp.simulation;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import us.flipp.utility.NL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import android.util.Log;

public class World {

    private static final String TAG = World.class.getName();
    private String identifier;
    private String author;

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
            loadProperties(root);
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

    private void loadProperties(Element root)
    {
        NodeList authorNodes = NL.ElementsByTag(root, "Author");
        if (authorNodes.getLength() >= 1)
        {
            Log.d(TAG, "UGG got here " + authorNodes.item(0).getFirstChild().getNodeValue());
            String author = authorNodes.item(0).getFirstChild().getNodeValue();
            Log.d(TAG, "and sanity check as author is " + author);
            if (author != null) {
                Log.d(TAG, "found author and setting to " + author);
                this.author = author;
            }
        }

    }

    public World(InputStream inputStream) throws IOException {
        loadFromXML(inputStream);
    }

    public String getAuthor() {
        return author;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

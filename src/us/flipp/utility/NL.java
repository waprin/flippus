package us.flipp.utility;

import android.nfc.Tag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import android.util.Log;

public class NL implements NodeList {

    private static final String TAG = NL.class.getName();

    private NL()
    {
        nodes = new ArrayList<Node>();
    }

    private ArrayList<Node> nodes;

    public static NodeList ElementsByTag(Element element, String tag)
    {
        Log.d(TAG, "Search for tag " + tag);
        NodeList allNodes = element.getChildNodes();
        Log.d(TAG, "All nodes is size " + allNodes.getLength());
        NL tagNodes = new NL();

        for (int i = 0; i < allNodes.getLength(); i++)
        {
            short nodeType = allNodes.item(i).getNodeType();
            String nodeTag = allNodes.item(i).getNodeName();

            if (nodeType == Node.ELEMENT_NODE && nodeTag.equals(tag))
            {
                Log.d(TAG, "adding a node that matched: " + nodeTag + " node type " + nodeType);
                tagNodes.nodes.add(allNodes.item(i));
            }
        }
        return tagNodes;
    }

    @Override
    public int getLength() {
        return nodes.size();
    }

    @Override
    public Node item(int arg0) {
        return nodes.get(arg0);
    }
}

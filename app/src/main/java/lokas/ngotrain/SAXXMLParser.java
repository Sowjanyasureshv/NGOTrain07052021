package lokas.ngotrain;

/**
 * Created by Bala on 12-01-2016.
 */

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Logon on 27/10/2015.
 */
public class SAXXMLParser {
    public static List<Employee> parse(InputStream is) {
        List<Employee> employees = null;
        try {
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SAXXMLHandler
            SAXXMLHandler saxHandler = new SAXXMLHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(is));
            // get the `Employee list`
            employees = saxHandler.getEmployees();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employees;
    }
}


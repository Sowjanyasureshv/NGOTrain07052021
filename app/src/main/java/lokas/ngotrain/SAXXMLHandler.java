package lokas.ngotrain;

/**
 * Created by Bala on 12-01-2016.
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Logon on 27/10/2015.
 */
public class SAXXMLHandler extends DefaultHandler {

    private List<Employee> employees;
    private Employee tempEmp;
    private StringBuilder tempVal = new StringBuilder();
    public SAXXMLHandler() {
        employees = new ArrayList<Employee>();
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset
        // tempVal = "";
        tempVal.setLength(0);
        if (qName.equalsIgnoreCase("item")) {
            // create a new instance of employee
            tempEmp = new Employee();
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //tempVal = new String(ch, start, length);
        tempVal.append(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("item")) {
            // add it to the list
            employees.add(tempEmp);
        } else if (qName.equalsIgnoreCase("id")) {
            tempEmp.setId(Integer.parseInt(tempVal.toString()));
        } else if (qName.equalsIgnoreCase("title")) {
            tempEmp.setName(tempVal.toString());
        } else if (qName.equalsIgnoreCase("desc")) {
            tempEmp.setDepartment(tempVal.toString());
        } else if (qName.equalsIgnoreCase("image")) {
            tempEmp.setType(tempVal.toString());
        }
    }
}

package org.mindfulcode.multi_media_text_field;

import android.content.Context;
import android.net.Uri;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Helper {
    public static void createCursorDrawable(Context context){
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("CONFIGURATION");
            doc.appendChild(rootElement);
            Element browser = doc.createElement("BROWSER");
            browser.appendChild(doc.createTextNode("chrome"));
            rootElement.appendChild(browser);
            Element base = doc.createElement("BASE");
            base.appendChild(doc.createTextNode("http:fut"));
            rootElement.appendChild(base);
            Element employee = doc.createElement("EMPLOYEE");
            rootElement.appendChild(employee);
            Element empName = doc.createElement("EMP_NAME");
            empName.appendChild(doc.createTextNode("Anhorn, Irene"));
            employee.appendChild(empName);
            Element actDate = doc.createElement("ACT_DATE");
            actDate.appendChild(doc.createTextNode("20131201"));
            employee.appendChild(actDate);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(getURLForResource(R.drawable.cursor)));
            transformer.transform(source, result);
            System.out.println("File saved!");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();}
    }


    public static String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
}

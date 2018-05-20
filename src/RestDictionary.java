//import generate.*;
//import generate.EntryList.Entry.Def;
///import generate.EntryList.Entry.Def.Dt;
//import generate.EntryList.Entry.Def;
//import generate.EntryList.Entry.Def.Dt;
//import generate.EntryList.Entry.Def;
//import generate.EntryList.Entry.Def.Dt;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.w3._2001.xmlschema.Dt;
import org.w3._2001.xmlschema.DxDef;
import org.w3._2001.xmlschema.Entry;
import org.w3._2001.xmlschema.EntryList;
public class RestDictionary {

	EntryList el;

	public RestDictionary() {
		// TODO Auto-generated constructor stub
	}

	// TODO Auto-generated method stub
	public static void main(String[] args) throws JAXBException {
		URL url = null;
		String strURL = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/Rongji?key=55999015-2e6f-4dd3-8463-f595ffa25cfb";
		try {
			url = new URL(strURL);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ClientResource helloClientresource = new ClientResource(
				strURL);
		Representation out = helloClientresource.get();
		try {
			out.write(System.out);
		} catch (IOException e1) {
			//	// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RestDictionary rd = new RestDictionary();
		rd.unmarshal(url);

		rd.getDef();
		//System.out.println(s.getWav());
	}

	public void getDef() {
		String s = el.toString();
		System.out.println("el: " + el);
		
		List<Object> l = el.getEntryOrComment();
		//List<Object> l = ((EntryList) el).getEntryOrComment();
		for( Object e: l) {
			System.out.println(e);
			//DxDef d = e.get();
			//List<JAXBElement<?>> dateSnDt = d.getDateOrSnOrDt();
			//System.out.println(d);
			//processDefinition(dateSnDt);
		}
	}

	/**
	 * @param dateSnDt
	 * @param str
	 * @return
	 */
	private String processDefinition(List<JAXBElement<?>> dateSnDt) {
		String str = "";

		for(JAXBElement item: dateSnDt) {
			if (item == null) 
			{
				System.out.println("Null item found");;
				continue;
			}
			Class<? extends Object> c = item.getDeclaredType();
			//Class c = item.getDeclaredType();
			//System.out.println(item.getValue());
			
			String name = c.getName();
			//c.getDeclaredAnnotationsByType(annotationClass)
			//System.out.println(">>> name: " + name);
			if (name.equals("java.lang.Byte")) {  // sn
				str = item.toString() + " ";
				System.out.print("\t" + item.toString() + " ");				
			}
			else if (name.equals("generate.EntryList$Entry$Def$Dt")) { //DT
				System.out.println(c.toString());
				definingText(item);
			}	
			else if (name.equals("java.lang.String")){ // Date
				str   = (String) item.getValue(); // toString() + " ";
				System.out.println(str + " ");
			}
			else {
				System.out.println("Unknown Name: " + name);
			}
		}
		System.out.println();
		return str;
	}

	/**
	 * @param item
	 */
	private void definingText(JAXBElement item) {
		//String Sx;
		//Sx = ((Dt) item).getSx();
		//if (Sx != null) {
		//	System.out.println(Sx);
		//}
		
		try {
			 Dt dt = (Dt) item.getValue();
			 Class<? extends Object> s = dt.getClass();
			 Method s1 = s.getMethod("getContent");
			
			if (s1 != null) {
				ArrayList<Serializable> al = (ArrayList<Serializable>) s1.invoke(dt);
				for(Serializable it:al) {
					if (!it.getClass().equals("javax.xml.bind.JAXBElement<String>".getClass())) {
						System.out.print(((javax.xml.bind.JAXBElement<String>) it).getValue());
					}
					else {
						System.out.print(it);
					}
				}
			}
		} catch (SecurityException | ReflectiveOperationException | IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println();
	}

	public  void marshal( Object jaxObject) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(jaxObject.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(jaxObject, System.out);
	}

	public  void unmarshal( URL url) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(EntryList.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		el = (EntryList) unmarshaller.unmarshal(url);
		System.out.println(el);
	}
}
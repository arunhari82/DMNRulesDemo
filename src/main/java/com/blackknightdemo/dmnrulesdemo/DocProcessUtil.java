package com.blackknightdemo.dmnrulesdemo;

public class DocProcessUtil {

	private static XPath configXPath() {
		XPath xPath = null;
		try {

			xPath = XPathFactory.newInstance().newXPath();
			HashMap<String, String> prefMap = new HashMap<String, String>() {
				{
					put("mismo",
							"http://www.mismo.org/residential/2009/schemas");
					put("xlink", "http://www.w3.org/1999/xlink");
					put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
					put("dsl", "http://www.w3.org/1999/xlink");
					put("bkfs", "http://bkfs.com/2017/integrations");
				}
			};

			SimpleNamespaceContext nsContext = new SimpleNamespaceContext(
					prefMap);
			xPath.setNamespaceContext(nsContext);
		} catch (Exception e) {

		}
		return xPath;
	}

	public static String getFNMAData(String xmlData) {
		String FNMAData = null;
		try {
			InputStream targetXMLStream = new ByteArrayInputStream(
					xmlData.getBytes());
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(targetXMLStream);
			XPath xPath = configXPath();
			String expression = "//bkfs:DOCUMENT_CONTENT[1]"; // get FNMA record
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					xmlDocument, XPathConstants.NODESET);
			System.out.println("test " + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (i == 0)
					FNMAData = node.getTextContent().trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FNMAData;
	}

	private static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}

	public static Borrowers getBorrowers(String xmlData) {
		Borrowers borrowers = null;
		try {
			InputStream targetXMLStream = new ByteArrayInputStream(
					xmlData.getBytes());
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(targetXMLStream);
			XPath xPath = configXPath();
			String expression = "//Borrowers";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					xmlDocument, XPathConstants.NODESET);
			System.out.println("test " + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (i == 0) {
					String data = nodeToString(node);
					System.out.println("Data : " + data);
					JAXBContext jaxbContext = JAXBContext
							.newInstance(Borrowers.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext
							.createUnmarshaller();
					borrowers = (Borrowers) jaxbUnmarshaller
							.unmarshal(new StringReader(data));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return borrowers;
	}

	public DocProcessUtil() {
	}

}

package com.example.androidcoap.test;

import junit.framework.TestCase;

import com.example.andoidcoap.DiscoveryResponseParser;
import com.example.andoidcoap.DiscoveryResponseParser.EndpointDescription;

public class ServerDiscoveryResponseParserTest extends TestCase {
	
	DiscoveryResponseParser parser;
	String response = "</helloWorld>;rt=\"HelloWorldDisplayer\";title=\"GET a friendly greeting!\",</large>;rt=\"BlockWiseTransferTester\";title=\"This is a large resource for testing block-wise transfer\",</toUpper>;rt=\"UppercaseConverter\";title=\"POST text here to convert it to uppercase\"";
	String[] lines;
	
	public ServerDiscoveryResponseParserTest(String name) {
		super(name);
		parser = new DiscoveryResponseParser(response);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetLines_returnsCorrectSize(){
		lines = parser.getLines();
		assertEquals(3, lines.length);
	}
	
	public void testGetDescriptions_returnsCorrectSize(){
		EndpointDescription[] descriptions = parser.getDescriptions();
		assertEquals(3, descriptions.length);
	}
	
	public void testGetDescriptions_returnsCorrectDescription(){
		EndpointDescription description = parser.getDescriptions()[0];
		
		assertEquals("endpoint incorrect", "/helloWorld", description.endpoint);
		assertEquals("rt incorrect", "HelloWorldDisplayer", description.rt);
		assertEquals("title incorrect", "GET a friendly greeting!", description.title);
	}
	
	

}

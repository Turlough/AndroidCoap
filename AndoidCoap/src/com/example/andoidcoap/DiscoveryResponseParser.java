package com.example.andoidcoap;


public class DiscoveryResponseParser {
	
	String response;
	String[] lines;
	
	public DiscoveryResponseParser (String response){
		this.response = response;
		lines = response.split(",");
	}
	
	public String[] getLines(){

		return lines;
	}
	
	public EndpointDescription[] getDescriptions(){
		
		EndpointDescription[] descriptions = new EndpointDescription[lines.length];
		
		for (int i =0; i<lines.length; i++){
			String line = lines[i];
			final String[] items = line.split(";");
			
			EndpointDescription description = new EndpointDescription(){{
				endpoint = items[0].replace("<","").replace(">", "");
				rt = items[1].replace("rt=", "").replace("\"", "");
				title = items[2].replace("title=", "").replace("\"", "");
			}};
			
			descriptions[i] = description;
			
		}
		
		return descriptions;
	}
	
	public class EndpointDescription{
		public String rt;
		public String title;
		public String endpoint;
	}

}

package com.enquero.automationFramework.apiUtility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHandler {

	ObjectMapper mapper = new ObjectMapper();
	
	public File updateJson(String location, String updateSequence, List<String> updates) throws IOException {
		
		String[] sequences = updateSequence.split(",");
		List<String> updateSequences = new ArrayList<String>(Arrays.asList(sequences));
				
		File file = new File(location);

		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile = File.createTempFile("updatedJson", ".txt", tempDir);
		FileWriter updatedFile = new FileWriter(tempFile);
		
		JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(location)))); 
		
		
		// When no updation
		if(updates.size()==0||updateSequences.size()==0) {
			return file;
		}
		
		//when there is no such key
		if(!json.has(updateSequences.get(0))) {
			System.out.println("No Such key!");
			return file;
		}
		
		// When key is only one
		if(updateSequences.size()==1) {
			if(json.get(updateSequences.get(0)) instanceof JSONArray) {
				jsonFinalKeyUpdater(json,updateSequences,updates);
			}else if(json.get(updateSequences.get(0)) instanceof JSONArray){
				JSONArray jsonArray = json.getJSONArray(updateSequences.get(0));
				if(jsonArray.length()!=updates.size()) {
					throw new IOException("There are different number of objects to be replaced and given to replace.");
				}
				else {
					for(int i=0;i<jsonArray.length();i++) {
						jsonFinalKeyUpdater(jsonArray.getJSONObject(i),updateSequences,updates);
					}
				}
			}

			
		}
		
		//when value of first key is a JSONObject 
		if(json.get(updateSequences.get(0)) instanceof JSONObject) {
			jsonObjectUpdater((JSONObject) json.get(updateSequences.get(0)), updateSequences.subList(1, updateSequences.size()), updates);
		}
		
		//when value of first key is a JSONArray
		else if(json.get(updateSequences.get(0)) instanceof JSONArray) {
			JSONArray jsonArray = json.getJSONArray(updateSequences.get(0));
			jsonArrayUpdater(jsonArray, updateSequences.subList(1,updateSequences.size()), updates);
		}
		
		System.out.println(json.toString(1));
		
		System.out.println(json.keySet());
		
		updatedFile.write(json.toString(1));
		updatedFile.flush();
		updatedFile.close();
		
		return tempFile;
	}
	
	
	
	public JSONArray jsonArrayUpdater(JSONArray jsonObjects, List<String> updateSequences , List<String> updates) throws JSONException, IOException {
		
		for(int i=0;i<jsonObjects.length();i++) {
			jsonObjectUpdater(jsonObjects.getJSONObject(i), updateSequences, updates);
		}
		
		return jsonObjects;
	}

	
	public JSONObject jsonObjectUpdater(JSONObject json, List<String> updateSequences, List<String> updates) throws IOException {
		if(updates.size()<1) {
			throw new IOException("There are different number of objects to be replaced and given to replace.");
		}
		if(!json.has(updateSequences.get(0))) {
			System.out.println("No Such Key");
			return json;
		}else if(updateSequences.size()==1) {
			jsonFinalKeyUpdater(json, updateSequences, updates);
		}
		else {
			//when value of first key is a JSONObject 
			if(json.get(updateSequences.get(0)) instanceof JSONObject) {
				jsonObjectUpdater((JSONObject) json.get(updateSequences.get(0)), updateSequences.subList(1, updateSequences.size()), updates);
			}
			
			//when value of first key is a JSONArray
			else if(json.get(updateSequences.get(0)) instanceof JSONArray) {
				JSONArray jsonArray = json.getJSONArray(updateSequences.get(0));
				jsonArrayUpdater(jsonArray, updateSequences.subList(1,updateSequences.size()), updates);
			}
		}
		
		return json;
	}
	
	public JSONObject jsonFinalKeyUpdater(JSONObject json, List<String> updateSequences, List<String> updates) {
		json.put(updateSequences.get(0), updates.get(0));
		updates.remove(0);
		return json;
	}
	
	
	
}

### `Different classes and their usage`

#### `JsonHandler`
This class helps to change the data of json file sent.

`Usage`
There is one overloaded method : updateJson
It accepts 3 parameters.
1. location : can be File or String - JSON can be shared as a file directly or as a String containing location of the file
2. updateSequence : String - the comma separated sequence of the parent keys till the key to be updated
3. updates : String - a list of all the values that are to be updated with

Eg: For a Json like below

`{"json":[

	{
		color: "red",
		value: "#f00"
	},
	{
		color: "green",
		value: "#0f0"
	},
	{
		color: "blue",
		value: "#00f"
	},
	{
		color: "cyan",
		value: "#0ff"
	},
	{
		color: "magenta",
		value: "#f0f"
	},
	{
		color: "yellow",
		value: "#ff0"
	},
	{
		color: "black",
		value: "#000"
	}
]}`

To update the value, method has to be called a below.

updateJson(location,updateSequence,updates)

1. location : location of the jsonFile or jsonFile
2. updateSequence : "json,value"
3. updates : has to be a list with 7 values that are to be replaced with the existing values.


#### `XmlHandler`
This class helps to change the data of xml file sent.

`Usage`
There is one overloaded method : updateXml
It accepts 3 parameters.
1. location : can be File or String - xml can be shared as a file directly or as a String containing location of the file
2. updateSequence : String - the nodeName of the xml node value to be changed
3. updates : String - a list of all the values that are to be updated with

Eg: For a xml like below

`<?xml version="1.0" encoding="UTF-8"?>
 <root>
 
    <element id="1">
       <age>29</age>
       <gender>Male</gender>
       <name>Pankaj</name>
       <role>Java Developer</role>
    </element>
    <element id="2">
       <age>35</age>
       <gender>Female</gender>
       <name>Lisa</name>
       <role>CSS Developer</role>
    </element>
 </root>`

To update the role, method has to be called a below.

updateXml(location,updateSequence,updates)

1. location : location of the xmlFile or xmlFile
2. updateSequence : "role"
3. updates : has to be a list with 2 values that are to be replaced with the existing values.

#### `XmlValidate`
This class helps to validate an xml against an xmlSchema.

`Usage`
There is one overloaded method : validateXmlWithSchema.
It accepts 2 parameters in 2 ways.
1. XmlFile & SchemaFile
2. XmlFileLocation & SchemaFileLocation

# VMAX Core

This repository contains the core library of the VMAX interface.

# The VMAX interface

## General idea

The idea of the VMAX interface is to provide a reausable interface for software featuring an API, that allows RDF-based access to the information available from the API.

Two options to access the interface are available:

- A triple pattern based interface.
The central idea of the VMAX interface is to abstract object oriented data from APIs as triples of an RDF knowledge graph.
Within the VMAX core library, the mapping from object oriented data to RDF triples is defined. 
The information available from the API can therefore be abstracted as a set of triples, i.e. a knowledge graph.
To query this graph, a logic to execute triple pattern queries is defined by the VMAX core library.

- The second option is to query the information using SPARQL.
This is realized by using the Apache Jena framework, specifically its query processor ARQ.
The query processor can execute SPARQL queries and uses the triple pattern interface to access the abstracted data of the API. 

## Implementation

The general idea explained above was developed during my research on interoperability in engineering using Semantic Web technologies. 
Please refer to the section [References](#references) if you are interested in the scientific background of this.

Within this and other repositories (see [Links](#links)), implementations of the approaches of my research are provided.
The implementations are no production-ready libraries and are only meant to demonstrate and evaluate my research. 
This also means, that while the scientific approach describes a general interface for APIs of object oriented software, the VMAX interface was only implemented for Java-based APIs.  

# How to use

To implement the VMAX interface for a specific API. 

Step **(1)** -
Copy the source code of this repo to your project or build a JAR file by running `mvn clean package` and include it in your project. 

Step **(2)** - 
Create an extension of the abstract class [`ApiClassAndAttributeList.java`](./src/main/java/com/vmax/vmax_core/api_helper/ApiClassAndAttributeList.java)

- Option **(A)** - The [VMAX API Documentation Browser](https://github.com/mxweigand/vmax_api_doc_browser) is a web-based tool to create this class in an automated and supported manner from the documentation of the API. Refer to the inked repository to find out how. 

- Option **(B)** - Create the extension of the abstract class [`ApiClassAndAttributeList.java`](./src/main/java/com/vmax/vmax_core/api_helper/ApiClassAndAttributeList.java) manually. It must specify the fields `classList` and `attributeList` correctly.  

Step **(3)** -
Create an extension of the abstract class [`ApiHelper.java`](./src/main/java/com/vmax/vmax_core/api_helper/ApiHelper.java). 
The extension must override the abstract method `findApiInstanceAsObjectByUri(String uri)`.
Within the constructor of the extension, create an instance of the extension of the abstract class [`ApiClassAndAttributeList.java`](./src/main/java/com/vmax/vmax_core/api_helper/ApiClassAndAttributeList.java) created in Step **(2)** and pass it to the `super()` constructor. 

Step **(4)** - Create an instance of the server class.

- Option **(A)** - Create an instance of the class [`TriplePatternServer.java`](./src/main/java/com/vmax/vmax_core/server/TriplePatternServer.java). Within the constructor, create an instance of the extension of the abstract class [`ApiHelper.java`](./src/main/java/com/vmax/vmax_core/api_helper/ApiHelper.java) created in Step **(3)** and pass it to the `super()` constructor. 

- Option **(B)** - Create an instance of the class [`SparqlServer.java`](./src/main/java/com/vmax/vmax_core/server/SparqlServer.java). Within the constructor, create an instance of the extension of the abstract class [`ApiHelper.java`](./src/main/java/com/vmax/vmax_core/api_helper/ApiHelper.java) created in Step **(3)** and pass it to the `super()` constructor. 

Step **(5)** - 
Start the sever by calling `start()` of the `TriplePatternServer` or the `SparqlServer`.

# Links

- [VMAX Plugin for MSOSA](https://github.com/mxweigand/vmax_plugin_msosa) 
An implementation of the VMAX interface for Magic Systems Of Systems Architect.

- [VMAX API Documentation Browser](https://github.com/mxweigand/vmax_api_doc_browser)
A web-based tool to automate and support the creation of implemenations of the VMAX interface for specific APIs. 
The tool enables users to explore the Javadoc API documetation and create and the interface by selecting relevant components of the API form the Javadoc.

# References

The following publications provide further insights into my research:

- Weigand, M. and Fay, A. (2022). *Creating Virtual Knowledge Graphs from Software-Internal Data.* IECON 2022 – 48th Annual Conference of the IEEE Industrial Electronics Society. [DOI: 10.1109/IECON49645.2022.9969051](https://doi.org/10.1109/IECON49645.2022.9969051).

- Weigand, M. (2023). *Triple Pattern Interfaces for Object-Oriented Software APIs.* Doctoral Consortium at ISWC 2023 co-located with 22nd International Semantic Web Conference (ISWC 2023). [Link](https://ceur-ws.org/Vol-3678/paper8.pdf).

- Weigand, M.; Gehlhoff, F. and Fay, A. (2024). *Extracting API Structures from Documentation to Create Virtual Knowledge Graphs.* 16th International Joint Conference on Knowledge Discovery, Knowledge Engineering and Knowledge Management - Volume 2: KEOD. 

# License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
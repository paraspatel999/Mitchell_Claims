
Recruiting: Coding Exercise
RESTful Web Service
======================================================

Technology used- Eclipse IDE, Apache Tomcate 7.63, MY SQL, JAX-RS with Jersey 2.x(Rest API), JAXB (Xml Binding).

Run Program
===============
--> Deploy code on the server and start the server.

Test the program
=============================

I used POSTMAN which is one of the plugin for google chrome.
Postman is used to send different request to the rest web-service which is running on the server.

1) Create the claim.( @POST request)
  
   --> Use create-claim.xml (provided)
   --> Resource Url - http://localhost:8080/MitchellClaims_WebServices/rest/mitchellclaims/create_claim

2) Read the Claim (@GET request, Xml response)

   --> Used claimNumber to retrive the data.
   --> Resurse Url - http://localhost:8080/MitchellClaims_WebServices/rest/mitchellclaims/read_claim/{claimNumber}

3) Update the Claim (@PUT request)

  --> Used update-claim.xml to update data.
  --> Resourse Url- http://localhost:8080/MitchellClaims_WebServices/rest/mitchellclaims/update_claim

4) Delete Claim (@DELETE request)

  --> Use claimNumber to delete Claim.
  --> Resourse Url - http://localhost:8080/MitchellClaims_WebServices/rest/mitchellclaims/delete_claim/{claimNumber}

Additional Task

5) Read all the Claims between two dates

  --> Pass two string date Ex. 2011-8-9/2014-12-10   
  --> http://localhost:8080/MitchellClaims_WebServices/rest/mitchellclaims/2014-12-12/2015-10-12

6) Read specific vehicle for specific Claim(return JSON)

  --> I used Query Parameter for this.
  --> for Vehicle I used LicPlate and ClaimNumber for Claim.
  --> Resourse Url - http://localhost:8080/MitchellClaims_WebServices/rest/mitchellclaims?claimNumber=22c9c23bac142856018ce14a26b6c299&licPlate=NO1PRES


Notes
==================
- I used Hashmap for temporary storage and I also provided CalimDaoImp.java if you want to use database.
-Create and update request url contain xml file.
-Read respnse will be in xml.


Files Included
==================
create-claim.xml - To create claim
update-claim.xml - To update claim
Mitchell.sql      - database script 


Source Files
================
 Packages
 1) com.mitchell.claims.Dao
    --> CalimDao.java  - Interface which provides all the methods.

    --> ClaimDaoImp.java  - Implementation  of the interface.(This is Optinal not connected to working solution. If you want to use you can change database property in Dbconnection class).
         And it is working.
    
    --> Storage.java - Temporary store the data.

 2) com.mitchell.claims.DbConnection
    --> Database connection logic.
 
 3) com.mitchell.claims.model
   --> Model classes created from Mitchell.xsd by JAXB.

 4) com.mitchell.claims.services
   --> service class which contain service methodes.
 
.





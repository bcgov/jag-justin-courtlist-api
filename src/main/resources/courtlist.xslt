<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
 <html>
 <body>
  <h1 align="center">Justin_Court_List_Data</h1>
   <table border="3" align="center" >
<tr>
<th colspan="8">Start Date : <xsl:value-of select="Justin_Court_List_Data/@StartDate"></xsl:value-of></th>
</tr>


<tr>
<th colspan="8">End Date : <xsl:value-of select="Justin_Court_List_Data/@EndDate"></xsl:value-of></th>
</tr>

<tr>
<th colspan="8">Extract : <xsl:value-of select="Justin_Court_List_Data/@Extract"></xsl:value-of></th>
</tr>


<tr>
<th colspan="8">PLMS_Court_List Type: <xsl:value-of select="Justin_Court_List_Data/PLMS_Court_List/@Type"></xsl:value-of></th>
</tr>

<tr>
<th colspan="8">Location Court: <xsl:value-of select="Justin_Court_List_Data//PLMS_Court_List/Location/@Court"></xsl:value-of></th>
</tr>


<tr>
<th colspan="8">Appearance Date: <xsl:value-of select="Justin_Court_List_Data//PLMS_Court_List/Location/Appearance/@Date"></xsl:value-of></th>
</tr>

<tr>
<th colspan="8">  </th>
</tr>



<xsl:for-each select="Justin_Court_List_Data/PLMS_Court_List/Location/Appearance/File">


<tr>
<th colspan="8" align="left"> File Number:<xsl:value-of select="@Number"></xsl:value-of>
  </th>
</tr>






   <tr>
   	   <th> Surname</th>
       <th> GivenName</th>
       <th> Initials</th>
       <th> Birthdate</th>
       <th> Ban</th>
       <th> BailCode</th>
       <th> BailDesc</th>
       <th> InCustody</th>
   </tr>



   <!-- <xsl:for-each select="File/Participant">-->

   <tr>
    <td><xsl:value-of select="Participant/Surname"/></td>
    <td><xsl:value-of select="Participant/GivenName"/></td>
    <td><xsl:value-of select="Participant/Initials"/></td>
    <td><xsl:value-of select="Participant/Birthdate"/></td>
    <td><xsl:value-of select="Participant/Ban"/></td>
    <td><xsl:value-of select="Participant/BailCode"/></td>
    <td><xsl:value-of select="Participant/BailDesc"/></td>
    <td><xsl:value-of select="Participant/InCustody"/></td></tr>
	
	<!-- <xsl:for-each select="File/Participant/Charge">-->
    <xsl:for-each select="Participant/Charge">
    
	<tr>
      <th colspan="8" align="left"> Charge Count:<xsl:value-of select="@ChgCount"></xsl:value-of>
      </th>
    </tr>
	
	<tr>
   <td colspan="4">ChargeDescription </td>
   <td>ChargeLocation</td>
   <td>AgencyFile</td>
      <td>ResultCode</td>
	     <td>ResultDesc</td>
		 </tr>
		 
		 
		 
		 <tr>
	<td colspan="4"><xsl:value-of select="ChargeDescription"/></td>
    <td><xsl:value-of select="ChargeLocation"/></td>
    <td><xsl:value-of select="AgencyFile"/></td>
    <td><xsl:value-of select="ResultCode"/></td>
    <td><xsl:value-of select="ResultDesc"/></td>
    	 </tr>
   
	
</xsl:for-each>

</xsl:for-each>


    </table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>

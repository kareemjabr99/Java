package dbii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import dbapp.exceptions.DBAppException;

@SuppressWarnings("all")
public class DBApp {
	
	Vector <Table> tables = new Vector <Table> ();
	
	
	
	public void init() {
		
	}
	
	
	public void createTable (String strTableName , String strClusteringKeyColumn , Hashtable <String,String> htblColNameType , 
			Hashtable <String,String> htblColNameMin , Hashtable <String,String> htblColNameMax) throws DBAppException {
		
		strTableName=strTableName.toLowerCase();
		strClusteringKeyColumn=strClusteringKeyColumn.toLowerCase();
		
		boolean flag = true;
		for (int i = 0 ; i < this.tables.size() ; i++) {
			if (strTableName.equals(this.tables.get(i).getTableName()))
				flag = false;
		}
		
		if (flag == false)
			throw new DBAppException("There Already Exists a Table With the Same Name, Please Refer to the MetaData File");
		
		
		// if there are no tables created already, create new metadata file and save table in metadata
		if (tables.isEmpty()) {
			Table t = new Table (strTableName , strClusteringKeyColumn , htblColNameType , htblColNameMin , htblColNameMax);
			
			
			FileWriter writer;
			try {
				writer = new FileWriter("resources/metadata.csv");
			} catch (Exception e1) {
				throw new DBAppException("Error in FileWriter");
			}
			
			Enumeration <String> e = htblColNameType.keys();
			Vector <String> v = new Vector <String> ();
			
			while (e.hasMoreElements())
				v.add(e.nextElement().toLowerCase());
			
			boolean isClusterKey = false;
			for (int i = 0 ; i < v.size() ; i++) {
				if (strClusteringKeyColumn.equals(v.get(i)))
					isClusterKey = true;
				
				try {
					writer.write( strTableName + "," + v.get(i) + "," + htblColNameType.get(v.get(i)) +
							"," + isClusterKey + "," + null + "," + null + "," + htblColNameMin.get(v.get(i)) +
							"," + htblColNameMax.get(v.get(i)) + "\n");
				} catch (Exception e1) {
					throw new DBAppException("Error in FileWriter");
				}
				try {
					writer.flush();
				} catch (Exception e1) {
					throw new DBAppException("Error in FileWriter");
				}
				isClusterKey = false;
			}
			try {
				writer.close();
			} catch (Exception e1) {
				throw new DBAppException("Error in FileWriter");
			}
			System.out.println("Metadata File Created Successfully");
			t.serializeTable(t);
			tables.add(t);
		}
		
		// if tables exist then update metadata accordingly
		else {
			Table t = new Table (strTableName , strClusteringKeyColumn , htblColNameType , htblColNameMin , htblColNameMax);
			
			FileWriter writer;
			try {
				writer = new FileWriter("resources/metadata.csv", true);
			} catch (Exception e1) {
				throw new DBAppException("Error in FileWriter");
			}
			
			Enumeration <String> e = htblColNameType.keys();
			Vector <String> v = new Vector <String> ();
			
			while (e.hasMoreElements())
				v.add(e.nextElement().toLowerCase());
			
			boolean isClusterKey = false;
			for (int i = 0 ; i < v.size() ; i++) {
				if (strClusteringKeyColumn.equals(v.get(i)))
					isClusterKey = true;
				
				try {
					writer.write( strTableName + "," + v.get(i) + "," + htblColNameType.get(v.get(i)) +
							"," + isClusterKey + "," + null + "," + null + "," + htblColNameMin.get(v.get(i)) +
							"," + htblColNameMax.get(v.get(i)) + "\n");
				} catch (Exception e1) {
					throw new DBAppException("Error in FileWriter");
				}
				try {
					writer.flush();
				} catch (Exception e1) {
					throw new DBAppException("Error in FileWriter");
				}
				isClusterKey = false;
			}
			try {
				writer.close();
			} catch (Exception e1) {
				throw new DBAppException("Error in FileWriter");
			}
			System.out.println("Metadata Updated Successfully");
			t.serializeTable(t);
			tables.add(t);
		}
	}
	
	
	public static void main(String[] args) throws DBAppException, IOException, ClassNotFoundException {
		DBApp d = new DBApp ();
		
		Hashtable <String , String> htblColNameType = new Hashtable<String, String>( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.Double");
		
		Hashtable <String , String> htblColNameMin = new Hashtable<String , String>();
		htblColNameMin.put("id", "1");
		htblColNameMin.put("name", "A");
		htblColNameMin.put("gpa", "1.3");
		
		Hashtable <String , String> htblColNameMax = new Hashtable<String , String>();
		htblColNameMax.put("id", "50");
		htblColNameMax.put("name", "ZZZ");
		htblColNameMax.put("gpa", "4.0");
		
		d.createTable( "Student", "id", htblColNameType , htblColNameMin , htblColNameMax);
		
		Hashtable <String , Object> htblColNameValue = new Hashtable <String , Object> ();
		htblColNameValue.put("name", new String ("Omar"));
		htblColNameValue.put("gpa", Double.valueOf(1.7));
		htblColNameValue.put("id", Integer.valueOf(1));
		
		d.insertIntoTable("Student", htblColNameValue);
		
		Hashtable <String , Object> htblColNameValue2 = new Hashtable <String , Object> ();
		htblColNameValue2.put("name", new String ("Gabr"));
		htblColNameValue2.put("gpa", Double.valueOf(2.7));
		htblColNameValue2.put("id", Integer.valueOf(2));
		
		d.insertIntoTable("Student", htblColNameValue2);
		
		Hashtable <String , Object> htblColNameValue3 = new Hashtable <String , Object> ();
		htblColNameValue3.put("name", new String ("Fares"));
		htblColNameValue3.put("gpa", Double.valueOf(2.1));
		htblColNameValue3.put("id", Integer.valueOf(3));
		d.insertIntoTable("Student", htblColNameValue3);
		
		Hashtable <String , Object> htblColNameValue4 = new Hashtable <String , Object> ();
		htblColNameValue4.put("name", "Sarah");
		htblColNameValue4.put("id", Integer.valueOf(4));
		htblColNameValue4.put("gpa", Double.valueOf(2.2));
		d.insertIntoTable("Student", htblColNameValue4);
		
		Hashtable <String , Object> htblColNameValue5 = new Hashtable <String , Object> ();
		htblColNameValue5.put("name", "Salma");
		htblColNameValue5.put("id", Integer.valueOf(5));
		htblColNameValue5.put("gpa", Double.valueOf(1.9));
		d.insertIntoTable("Student", htblColNameValue5);
		System.out.println(htblColNameValue5.get("name")+ "");
		
		
		
		SQLTerm [] arrSQLTerms = new SQLTerm[3];
		
		for (int i = 0 ; i < arrSQLTerms.length ; i++)
			arrSQLTerms[i] = new SQLTerm();
		
		
		arrSQLTerms[0]._strTableName = "Student";
		arrSQLTerms[0]._strColumnName = "gpa";
		arrSQLTerms[0]._strOperator = ">";
		arrSQLTerms[0]._objValue = Double.valueOf(2.1);
		
		arrSQLTerms[1]._strTableName = "Student";
		arrSQLTerms[1]._strColumnName= "gpa";
		arrSQLTerms[1]._strOperator = "=";
		arrSQLTerms[1]._objValue = Double.valueOf(1.7);
		
		arrSQLTerms[2]._strTableName = "Student";
		arrSQLTerms[2]._strColumnName= "gpa";
		arrSQLTerms[2]._strOperator = "=";
		arrSQLTerms[2]._objValue = Double.valueOf(1.9);
		
		String[]strarrOperators = new String[2];
		strarrOperators[0] = "OR";
		strarrOperators[1] = "OR";
		
//		Iterator resultSet = d.selectFromTable(arrSQLTerms, strarrOperators);
//		System.out.println();
//		System.out.println("SELECT RESULT: ");
//		int i = 1;
//		while (resultSet.hasNext()) {
//			System.out.println(i + " - " + resultSet.next());
//			i++;
//		}
	}
	
	
	public void createIndex (String strTableName , String [] strarrColName) throws DBAppException {
		if (strarrColName.length < 3) {
			throw new DBAppException("Minimum Columns is 3");
		}
		
		else {
			String indexName = "";
			
			for (String column : strarrColName) {
				String columnName = capitalize(column);
				indexName += columnName;
			}
			
			indexName += "Index";
			
			try {
				BufferedReader b = new BufferedReader( new FileReader ("resources/metadata.csv"));
				
				String l;
				String cOne = strarrColName[0];
				String cTwo = strarrColName[1];
				String cThree = strarrColName[2];
				
	            StringBuilder csvContent = new StringBuilder();
				
				while ( (l = b.readLine())  != null) {
					String [] data = l.split(",");
					
					if (data[0].equals(strTableName) && ( data[1].equals(cOne) || data[1].equals(cTwo) || data[1].equals(cThree) )) {
						data[4] = indexName;
						data[5] = "Octree";
						StringBuilder updatedRow = new StringBuilder();
						for (String value : data) {
							updatedRow.append(value);
							updatedRow.append(",");
						}
						updatedRow.deleteCharAt(updatedRow.length() - 1);
						
						l = updatedRow.toString();
					}
					
					csvContent.append(l);
					csvContent.append("\n");
					
				}
				
				b.close();
				
				FileWriter writer = new FileWriter("resources/metadata.csv");
				writer.write(csvContent.toString());
				writer.close();
				
				
			} catch (Exception w) {
				throw new DBAppException("Error");
			}
			
			Table t = null;
			
			for (int i = 0 ; i < tables.size() ; i++) 
				if (strTableName.equals(tables.get(i).getTableName())) {
					t = tables.get(i);
					break;
			}
				
			t.deserializeTable("resources/" + t.getTableName() + ".ser");
			
			String col1 = strarrColName[0];
			String col2 = strarrColName[1];
			String col3 = strarrColName[2];
			
			Object minX = getMin(col1, strTableName);
			Object maxX = getMax(col1, strTableName);
			Object minY = getMin(col2, strTableName);
			Object maxY = getMax(col2, strTableName);
			Object minZ = getMin(col3, strTableName);
			Object maxZ = getMax(col3, strTableName);
			
			Range range = new Range(minX, maxX, minY, maxY, minZ, maxZ);
			
			Node node = new Node(range);
			
			if(!(t.pages.isEmpty())) {
				
				for(int j = 0 ; j < t.pages.size() ; j++) {
					
					Page page = t.pages.get(j);
					page.deserializePage("resources/" + strTableName + " Page " + j + ".ser");
					
					for(int a = 0 ; a < page.tuplesInPage.size() ; a++) {
						
						Tuple tuple = page.tuplesInPage.get(a);
						
						Object x = tuple.data.get(col1);
						Object y = tuple.data.get(col2);
						Object z = tuple.data.get(col3);
					
						OctPoint point = new OctPoint(x,y,z,a);
						
						node.insert(point, node);
						
					}
				}
			}
			
			node.serializeIndex(node);
		}
	}
	
	
	@SuppressWarnings("resource")
	public void insertIntoTable(String strTableName , Hashtable <String,Object> htblColNameValue) 
			throws DBAppException {
		
		strTableName = strTableName.toLowerCase();
			
		boolean exists = false;
		
		try ( BufferedReader b = new BufferedReader( new FileReader("resources/metadata.csv") ) ){
			String l = "";
			
			while ( (l = b.readLine()) != null ) {
				String [] data = l.split(",");
				
				if (data[0].equals(strTableName)) {
					exists = true;
					break;
				}
				
			}
		} catch (Exception w) {
			throw new DBAppException("Error");
		}
		
		if (!exists)
			throw new DBAppException("table Does Not Exist");
		
		Table t = null;
		
		for (int i = 0 ; i < tables.size() ; i++) 
			if (strTableName.equals(tables.get(i).getTableName())) {
				t = tables.get(i);
				break;
			}
		
		t.deserializeTable("resources/" + t.getTableName() + ".ser");
		
		String csvPath = "resources/metadata.csv";
        String line = "";
        String clusterKey = "";
        String clusterKeyType = "";
        Hashtable <String , String> colType = new Hashtable <> ();
        
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvPath));
		} catch (Exception e2) {
			throw new DBAppException("File Not Found");
		}
        try {
			line = br.readLine();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        while (line != null) {
        	String [] row = line.split(",");
        	
        	if (row[3].equals("true") && row[0].equals(strTableName)) {
        		clusterKey = row[1];
        		clusterKeyType = row[2];
        		colType.put(row[1], row[2]);
        		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
        	}
        	else if (row[3].equals("false") && row[0].equals(strTableName)) {
        		colType.put(row[1], row[2]);
        		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
        	} else
				try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
        }
        try {
			br.close();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        
        Vector <Page> pages = t.getPages();
		Tuple data = new Tuple(strTableName , clusterKey, htblColNameValue);
	
		
		
		// checks conditions
		
		
		
		// checks if clusterKey is given in input
		if (htblColNameValue.get(clusterKey) == null) {
			throw new DBAppException("No Primary Key Entered");
		}
					
		else if (htblColNameValue.get(clusterKey) != null) {
			Enumeration<String> e = htblColNameValue.keys();
			Vector <String> v = new Vector <String> ();
			
			while (e.hasMoreElements())
				v.add(e.nextElement().toLowerCase());
						
			// checks if any violation in insertion happens regarding mins and maxes
			for (int k = 0 ; k < v.size() ; k++) {
							
				String type = colType.get(v.get(k));
							
				if ( type.equals("java.lang.Integer") ) {
					if ( (int) htblColNameValue.get(v.get(k)) > Integer.parseInt(t.getColNameMax().get(v.get(k)))) {
						throw new DBAppException("Integer Violates Max, Please Refer Back to MetaData File");
					}
					else if ((int) htblColNameValue.get(v.get(k)) < Integer.parseInt(t.getColNameMin().get(v.get(k)))) {
						throw new DBAppException("Integer Violates Min, Please Refer Back to MetaData File");
					}
				}
							
				else if ( type.equals("java.lang.Double") ) {
					if ( (Double) htblColNameValue.get(v.get(k)) > Double.parseDouble(t.getColNameMax().get(v.get(k)))) {
						throw new DBAppException("Double Violates Max, Please Refer Back to MetaData File");
					}
					else if ((Double) htblColNameValue.get(v.get(k)) < Double.parseDouble(t.getColNameMin().get(v.get(k)))) {
						throw new DBAppException("Double Violates Min, Please Refer Back to MetaData File");
					}
				}
							
				else if ( type.equals("java.lang.String") ) {
					if (((String) htblColNameValue.get(v.get(k))).toLowerCase().compareTo(t.getColNameMax().get(v.get(k)).toLowerCase()) > 0  ) { // left greater than right
						throw new DBAppException("String Violates Max, Please Refer Back to MetaData File");
					}
					else if ( ((String) htblColNameValue.get(v.get(k))).toLowerCase().compareTo(t.getColNameMin().get(v.get(k))) < 0 ) { // right greater than left
						throw new DBAppException("String Violates Min, Please Refer Back to MetaData File");
					}
				}
							
				else if (type.equals("java.lang.Date") ) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try {
						if ( ( (Date) htblColNameValue.get(v.get(k))).after( (Date) format.parse((t.getColNameMax().get(v.get(k))) )) ) {
							throw new DBAppException("Date Violates Max, Please Refer Back to MetaData File");
						} else
							try {
								if (  ( (Date) htblColNameValue.get(v.get(k))).before( (Date) format.parse((t.getColNameMin().get(v.get(k))) ))  ) {
									throw new DBAppException("Date Violates Min, Please Refer Back to MetaData File");
								}
							} catch (Exception e1) {
								e1.printStackTrace();
								throw new DBAppException("Error");
							}
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new DBAppException("Error");
					}
				}
							
				else {
					throw new DBAppException("Invalid Type Found");
				}
			}
						
		}
		
		
		// start inserting and updating after checking that all conditions are met
		if (pages.isEmpty()) {
			Page newPage = new Page (data , t);
			pages.add(newPage);
			t.addPageIntoTotalPages();
			newPage.serializePage(newPage , t);
			t.setPages(pages);
		}
		
		else {
			
			// traverse pages to find correct page after checking if inputed data is valid
			for (int i = 0 ; i < pages.size() ; i++) {
				
				// check with last element if inserting tuple belongs to page or not or what
				Tuple tmpTuple = pages.get(i).getTuplesInPage().lastElement();
					
					
					
					if (clusterKeyType.equals("java.lang.Integer")) {
						
						t.addRowIntoTableSize();
						int clusterKeyValueInsertTuple = (int) htblColNameValue.get(clusterKey);
						int valueInLastTupleInPage = (int) tmpTuple.get(clusterKey);
						
						if (clusterKeyValueInsertTuple < valueInLastTupleInPage) { // correct page
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = 0;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if ( (int) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValueInsertTuple ) {
									low = mid + 1;
								}
								else if ( (int) tuplesInPage.get(mid).get(clusterKey) > clusterKeyValueInsertTuple ) {
									high = mid - 1;
								}
								else {
									index = mid;
									throw new DBAppException("Record Already Exists");
								}
							}
							
							
							// if value does not exist already in tuples in page
							if ( (int) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValueInsertTuple ) 
								index = mid + 1;
							else 
								index = mid;
							
							
							tuplesInPage.add(index, data);
							pages.get(i).setTuplesInPage(tuplesInPage);
							pages.get(i).serializePage(pages.get(i) , t);
							break;
						}
						else if (clusterKeyValueInsertTuple == valueInLastTupleInPage) {
							throw new DBAppException("Record Already Exists");
						}
						
						else if (clusterKeyValueInsertTuple > valueInLastTupleInPage && (i + 1) > (pages.size() - 1)) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							pages.get(i).getTuplesInPage().add(data);
							pages.get(i).serializePage(pages.get(i) , t);
						}
					}
					
					
					
					else if (clusterKeyType.equals("java.lang.String")) {
							
						t.addRowIntoTableSize();
						String clusterKeyValueInsertTuple = (String) htblColNameValue.get(clusterKey);
						String valueInLastTupleInPage = (String) tmpTuple.get(clusterKey);
						
						if (clusterKeyValueInsertTuple.compareTo(valueInLastTupleInPage) < 0) { // correct page
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
							
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = 0;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if ( ((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValueInsertTuple) < 0 ) {
									low = mid + 1;
								}
								else if ( ((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValueInsertTuple) > 0) {
									high = mid - 1;
								}
								else {
									index = mid;
									throw new DBAppException("Record Already Exists");
								}
							}
							
							// if value does not exist already in tuples in page
							if ( ((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValueInsertTuple) < 0 ) 
								index = mid + 1;
							else 
								index = mid;
							
							
							tuplesInPage.add(index, data);
							pages.get(i).setTuplesInPage(tuplesInPage);
							pages.get(i).serializePage(pages.get(i) , t);
							break;
						}
						else if (clusterKeyValueInsertTuple.equals(valueInLastTupleInPage)) {
							throw new DBAppException("Record Already Exists");
						}
						else if (clusterKeyValueInsertTuple.compareTo(valueInLastTupleInPage) > 0 && (i + 1) > (pages.size() - 1)) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							pages.get(i).getTuplesInPage().add(data);
							pages.get(i).serializePage(pages.get(i) , t);
						}
					}
					
					
					else if (clusterKeyType.equals("java.lang.Double")) {
						
						t.addRowIntoTableSize();
						Double clusterKeyValueInsertTuple = (Double) htblColNameValue.get(clusterKey);
						Double valueInLastTupleInPage = (Double) tmpTuple.get(clusterKey);
						
						if (clusterKeyValueInsertTuple < valueInLastTupleInPage) { // correct page
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
							
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = 0;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if ( (Double) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValueInsertTuple ) {
									low = mid + 1;
								}
								else if ( (Double) tuplesInPage.get(mid).get(clusterKey) > clusterKeyValueInsertTuple ) {
									high = mid - 1;
								}
								else {
									index = mid;
									throw new DBAppException("Record Already Exists");
								}
							}
							
							// if value does not exist already in tuples in page
							if ( (Double) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValueInsertTuple ) 
								index = mid + 1;
							else 
								index = mid;
							
							
							tuplesInPage.add(index, data);
							pages.get(i).setTuplesInPage(tuplesInPage);
							pages.get(i).serializePage(pages.get(i) , t);
							break;
						}
						else if (clusterKeyValueInsertTuple == valueInLastTupleInPage) {
							throw new DBAppException("Record Already Exists");
						}
						else if (clusterKeyValueInsertTuple > valueInLastTupleInPage && (i + 1) > (pages.size() - 1)) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							pages.get(i).getTuplesInPage().add(data);
							pages.get(i).serializePage(pages.get(i) , t);
						}
						
					}
					
					else if (clusterKeyType.equals("java.lang.Date")) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						t.addRowIntoTableSize();
						Date clusterKeyValueInsertTuple = null;
						try {
							clusterKeyValueInsertTuple = (Date) format.parse( (String) htblColNameValue.get(clusterKey));
						} catch (Exception e) {
							throw new DBAppException("Error");
						}
						Date valueInLastTupleInPage = null;
						try {
							valueInLastTupleInPage = (Date) format.parse((String)tmpTuple.get(clusterKey));
						} catch (Exception e) {
							throw new DBAppException("Error");
						}
						
						if (clusterKeyValueInsertTuple.before(valueInLastTupleInPage)) { // correct page
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
							
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = 0;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if ( ((Date) tuplesInPage.get(mid).get(clusterKey)).before(clusterKeyValueInsertTuple) ) {
									low = mid + 1;
								}
								else if ( ((Date) tuplesInPage.get(mid).get(clusterKey)).after(clusterKeyValueInsertTuple) ) {
									high = mid - 1;
								}
								else {
									index = mid;
									throw new DBAppException("Record Already Exists");
								}
							}
							
							// if value does not exist already in tuples in page
							if (((Date) tuplesInPage.get(mid).get(clusterKey)).before(clusterKeyValueInsertTuple)) 
								index = mid + 1;
							else 
								index = mid;
							
							
							tuplesInPage.add(index, data);
							pages.get(i).setTuplesInPage(tuplesInPage);
							pages.get(i).serializePage(pages.get(i) , t);
							break;
						}
						
						else if (clusterKeyValueInsertTuple.equals(valueInLastTupleInPage)) {
							throw new DBAppException("Record Already Exists");
						}
						else if (clusterKeyValueInsertTuple.after(valueInLastTupleInPage) && (i + 1) > (pages.size() - 1)) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							pages.get(i).getTuplesInPage().add(data);
							pages.get(i).serializePage(pages.get(i) , t);
						}
					
						
					}
					else {
						throw new DBAppException("Not A Valid Data Type");
				}
					
					boolean indexed = false;
					int count = 0;
					ArrayList arr = indexedColumns();
					
					Enumeration<String> e = htblColNameValue.keys();
					Vector <String> v = new Vector <String> ();
					
					while (e.hasMoreElements())
						v.add(e.nextElement().toLowerCase());
					
					for (int j = 0 ; j < arr.size() ; j++) {
						if (v.contains(arr.get(j))) {
							count++;
						}
					}
					
					if (count >= 3) {
						indexed = true;
					}
					
					
					if (indexed) {
						String indexName = "";
						
						for (int j = 0 ; j < arr.size() ; j++) {
							indexName += capitalize((String) arr.get(i));
						}
						indexName += "Index";
						
						
						Node n = deserializeIndex(indexName);
						
						
						String l = "";
						ArrayList a = new ArrayList();
						
						try (BufferedReader b = new BufferedReader(new FileReader("resources/metadata.csv"))){
							
							while ( (l = br.readLine()) != null) {
								String [] d = l.split(",");
								
								if (d[0].equals(strTableName) && d[4] != null) {
									if (htblColNameValue.containsKey(d[1])) {
										a.add(htblColNameValue.get(d[1]));
										break;
									}
								}
								
								
							}
							
							
							b.close();
							
						} catch (Exception z) {
							z.printStackTrace();
							throw new DBAppException("Error");
						}
						
						Object x = a.get(0);
						Object y = a.get(1);
						Object z = a.get(2);
						
						OctPoint octPt = new OctPoint(x , y , z , i);
						
						n.insert(octPt , n);
						return;
					}
			}
			
		}
		
		
		// checks if any pages violate max rows and updates accordingly
		for (int i = 0 ; i < pages.size() ; i++) {
			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
			
			if (pages.get(i).getTuplesInPage().size() > pages.get(i).maxRowsPerPage) {
				Tuple tmp = pages.get(i).getTuplesInPage().lastElement();
				
				pages.get(i).getTuplesInPage().removeElementAt( pages.get(i).getTuplesInPage().size() - 1 );
				
				if ( (i + 1) < (pages.size() - 1) )
					pages.get(i + 1).getTuplesInPage().add(0, tmp);
				
				else {
					pages.add(new Page (tmp , t));
					t.addPageIntoTotalPages();
				}
				
			}
			pages.get(i).serializePage(pages.get(i) , t);
		}
		
		t.setPages(pages);
		t.serializeTable(t);
		
		
		for (int i = 0 ; i < pages.size() ; i++) {
			System.out.println("Inserted values: " + pages.get(i).getTuplesInPage() );
		}
	}
	
	public Node deserializeIndex (String indexName) throws DBAppException {
		Node n = null;
		
		try {
			FileInputStream fileIn = new FileInputStream("resources/" + indexName + ".ser");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			n = (Node) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBAppException("Error in Deserializing");
		}
		
		return n;
	}
	
	public ArrayList indexedColumns () throws DBAppException{
		ArrayList result = new ArrayList ();
		
		try(BufferedReader br = new BufferedReader (new FileReader("resources/metadata.csv"))){
			String line = "";
			
			while ( (line = br.readLine()) != null) {
				
				String [] row = line.split(",");
				
				if (row[4] == null) 
					result.add(row[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBAppException("Error");
		}
		
		return result;
	}

	
	public void updateTable(String strTableName ,String strClusteringKeyValue , 
			Hashtable<String,Object> htblColNameValue ) throws DBAppException {
		strTableName = strTableName.toLowerCase();
		strClusteringKeyValue = strClusteringKeyValue.toLowerCase();
		
		
		boolean exists = false;
		
		try ( BufferedReader b = new BufferedReader( new FileReader("resources/metadata.csv") ) ){
			String l = "";
			
			while ( (l = b.readLine()) != null ) {
				String [] data = l.split(",");
				
				if (data[0].equals(strTableName)) {
					exists = true;
					break;
				}
				
			}
		} catch (Exception w) {
			throw new DBAppException("Error");
		}
		
		if (!exists)
			throw new DBAppException("table Does Not Exist");
		
		
		if (strClusteringKeyValue == null)
			throw new DBAppException("Enter a Clustering Key Value");
		
		Table t = null;
		
		for (int i = 0 ; i < tables.size() ; i++) {
			if (tables.get(i).getTableName().equals(strTableName)) {
				t = tables.get(i);
				break;
			}
		}
		
		if (t == null)
			throw new DBAppException("Invalid Table");
		
		t.deserializeTable("resources/" + t.getTableName() + ".ser");
		
		String csvPath = "resources/metadata.csv";
	    String line = "";
	    String clusterKey = "";
	    String clusterKeyType = "";
	    Hashtable <String , String> colType = new Hashtable <> ();
	    
	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvPath));
		} catch (Exception e2) {
			throw new DBAppException("File Not Found");
		}
		
	    try {
			line = br.readLine();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
	    
	    while (line != null) {
	    	String [] row = line.split(",");
	    	
	    	if (row[3].equals("true") && row[0].equals(strTableName)) {
	    		clusterKey = row[1];
	    		clusterKeyType = row[2];
	    		colType.put(row[1], row[2]);
	    		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
	    	}
	    	else if (row[3].equals("false") && row[0].equals(strTableName)) {
	    		colType.put(row[1], row[2]);
	    		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
	    	} else
				try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
	    }
	    try {
			br.close();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
		
		Vector <Page> pages = t.getPages();
		

		Enumeration<String> e = htblColNameValue.keys();
		Vector <String> v = new Vector <String> ();
		
		while (e.hasMoreElements())
			v.add(e.nextElement().toLowerCase());
		
		if (v.contains(clusterKey))
			throw new DBAppException("Cannot Change Cluster Key");
					
		// checks if any violation in insertion happens regarding mins and maxes
		for (int k = 0 ; k < v.size() ; k++) {
						
			String type = colType.get(v.get(k));
						
			if ( type.equals("java.lang.Integer") ) {
				int n = Integer.parseInt( (String) htblColNameValue.get(v.get(k)) );
				if ( n > Integer.parseInt(t.getColNameMax().get(v.get(k)))) {
					throw new DBAppException("Integer Violates Max, Please Refer Back to MetaData File");
				}
				else if (n < Integer.parseInt(t.getColNameMin().get(v.get(k)))) {
					throw new DBAppException("Integer Violates Min, Please Refer Back to MetaData File");
				}
			}
						
			else if ( type.equals("java.lang.Double") ) {
				Double d = Double.parseDouble((String) htblColNameValue.get(v.get(k)));
				
				if ( d > Double.parseDouble(t.getColNameMax().get(v.get(k))) ) {
					throw new DBAppException("Double Violates Max, Please Refer Back to MetaData File");
				}
				else if (d < Double.parseDouble(t.getColNameMin().get(v.get(k)))) {
					throw new DBAppException("Double Violates Min, Please Refer Back to MetaData File");
				}
			}
						
			else if ( type.equals("java.lang.String") ) {
				if (((String) htblColNameValue.get(v.get(k))).toLowerCase().compareTo(t.getColNameMax().get(v.get(k)).toLowerCase()) > 0  ) { // left greater than right
					throw new DBAppException("String Violates Max, Please Refer Back to MetaData File");
				}
				else if ( ((String) htblColNameValue.get(v.get(k))).toLowerCase().compareTo(t.getColNameMin().get(v.get(k))) < 0 ) { // right greater than left
					throw new DBAppException("String Violates Min, Please Refer Back to MetaData File");
				}
			}
						
			else if (type.equals("java.lang.Date") ) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if ( ( (Date) htblColNameValue.get(v.get(k))).after( (Date) format.parse((t.getColNameMax().get(v.get(k))) )) ) {
						throw new DBAppException("Date Violates Max, Please Refer Back to MetaData File");
					} else
						try {
							if (  ( (Date) htblColNameValue.get(v.get(k))).before( (Date) format.parse((t.getColNameMin().get(v.get(k))) ))  ) {
								throw new DBAppException("Date Violates Min, Please Refer Back to MetaData File");
							}
						} catch (Exception e1) {
							throw new DBAppException("Error");
						}
				} catch (Exception e1) {
					throw new DBAppException("Error");
				}
			}
						
			else {
				throw new DBAppException("Invalid Type Found");
			}
		}
					
	
	    
	    // no violations , start searching for correct page
	    for (int i = 0 ; i < pages.size() ; i++) {
	    	
	    	Tuple tmpTuple = pages.get(i).getTuplesInPage().lastElement();
	    	
	    	if (clusterKeyType.equals("java.lang.Integer")) {
	    		int clusterKeyValue = Integer.parseInt(strClusteringKeyValue);
	    		int valueInLastTuple = (int) tmpTuple.get(clusterKey);
	    		
	    		if (clusterKeyValue < valueInLastTuple) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			// binary search
	    			int low = 0;
	    			int high = tuplesInPage.size() - 1;
	    			int mid = 0;
	    			int index = -1;
	    			
	    			while (low <= high) {
	    				mid = (low + high) / 2;
	    				
	    				if ( (int) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValue )
	    					low = mid + 1;
	    				else if ( (int) tuplesInPage.get(mid).get(clusterKey) > clusterKeyValue )
	    					high = mid - 1;
	    				else {
	    					index = mid;
	    				}
	    			}
	    			
	    			if (index == -1)
	    				throw new DBAppException("Not Found");
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.get(index).getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.get(index).setData(dataInTuple);
	    			
	    		}
	    		
	    		
	    		else if (clusterKeyValue == valueInLastTuple) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.lastElement().getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.lastElement().setData(dataInTuple);
	    		}
	    		pages.get(i).serializePage(pages.get(i), t);
	    	}
	    	
	    	else if (clusterKeyType.equals("java.lang.Double")) {

	    		Double clusterKeyValue = Double.parseDouble(strClusteringKeyValue);
	    		Double valueInLastTuple = (Double) tmpTuple.get(clusterKey);
	    		
	    		if (clusterKeyValue < valueInLastTuple) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			// binary search
	    			int low = 0;
	    			int high = tuplesInPage.size() - 1;
	    			int mid = 0;
	    			int index = -1;
	    			
	    			while (low <= high) {
	    				mid = (low + high) / 2;
	    				
	    				if ( (Double) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValue )
	    					low = mid + 1;
	    				else if ( (Double) tuplesInPage.get(mid).get(clusterKey) > clusterKeyValue )
	    					high = mid - 1;
	    				else {
	    					index = mid;
	    				}
	    			}
	    			
	    			if (index == -1)
	    				throw new DBAppException("Not Found");
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.get(index).getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.get(index).setData(dataInTuple);
	    			
	    		}
	    		
	    		
	    		else if (clusterKeyValue == valueInLastTuple) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.lastElement().getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.lastElement().setData(dataInTuple);
	    		}
	    		pages.get(i).serializePage(pages.get(i), t);
	    	
	    	}
	    	
	    	else if (clusterKeyType.equals("java.lang.String")) {
	    		
	    		String clusterKeyValue = strClusteringKeyValue;
	    		String valueInLastTuple = (String) tmpTuple.get(clusterKey);
	    		
	    		if (clusterKeyValue.compareTo(valueInLastTuple) < 0) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			// binary search
	    			int low = 0;
	    			int high = tuplesInPage.size() - 1;
	    			int mid = 0;
	    			int index = -1;
	    			
	    			while (low <= high) {
	    				mid = (low + high) / 2;
	    				
	    				if ( ((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValue) < 0)
	    					low = mid + 1;
	    				else if ( ((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValue) > 0)
	    					high = mid - 1;
	    				else {
	    					index = mid;
	    				}
	    			}
	    			
	    			if (index == -1)
	    				throw new DBAppException("Not Found");
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.get(index).getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.get(index).setData(dataInTuple);
	    			
	    		}
	    		
	    		
	    		else if (clusterKeyValue.equals(valueInLastTuple)) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.lastElement().getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.lastElement().setData(dataInTuple);
	    		}
	    		pages.get(i).serializePage(pages.get(i), t);
	    		
	    	}
	    	
	    	else if (clusterKeyType.equals("java.lang.Date")) {
	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    		Date clusterKeyValue = null;
				try {
					clusterKeyValue = (Date) format.parse(strClusteringKeyValue);
				} catch (Exception e1) {
					throw new DBAppException("Parse Error");
				}
				
	    		Date valueInLastTuple = null;
				try {
					valueInLastTuple = (Date) format.parse(clusterKey);
				} catch (Exception e1) {
					throw new DBAppException("Parse Error");
				}
	    		
	    		if (clusterKeyValue.before(valueInLastTuple)) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			// binary search
	    			int low = 0;
	    			int high = tuplesInPage.size() - 1;
	    			int mid = 0;
	    			int index = -1;
	    			
	    			while (low <= high) {
	    				mid = (low + high) / 2;
	    				
	    				if ( ((Date) tuplesInPage.get(mid).get(clusterKey)).before(clusterKeyValue) ) 
	    					low = mid + 1;
	    				else if ( ((Date) tuplesInPage.get(mid).get(clusterKey)).after(clusterKeyValue) )
	    					high = mid - 1;
	    				else {
	    					index = mid;
	    				}
	    			}
	    			
	    			if (index == -1)
	    				throw new DBAppException("Not Found");
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.get(index).getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.get(index).setData(dataInTuple);
	    			
	    		}
	    		
	    		
	    		else if (clusterKeyValue.equals(valueInLastTuple)) {
	    			pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
	    			Vector <Tuple> tuplesInPage = pages.get(i).getTuplesInPage();
	    			
	    			Hashtable <String , Object> dataInTuple = tuplesInPage.lastElement().getData();
	    			
	    			Enumeration <String> enumhtbl = htblColNameValue.keys();
	    			Vector <String> vechtbl = new Vector<>();
	    			while (enumhtbl.hasMoreElements())
	    				vechtbl.add(enumhtbl.nextElement().toLowerCase());
	    			
	    			Enumeration <String> enumData = dataInTuple.keys();
	    			Vector <String> vecdata = new Vector<>();
	    			
	    			while (enumData.hasMoreElements())
	    				vecdata.add(enumData.nextElement().toLowerCase());
	    			
	    			for (int j = 0 ; j < vechtbl.size() ; j++) {
	    				for (int k = 0 ; k < vecdata.size() ; k++) {
	    					if (vechtbl.get(j).equals(vecdata.get(k))) 
	    						dataInTuple.put(vechtbl.get(j), htblColNameValue.get(vechtbl.get(j)));
	    				}
	    			}
	    			
	    			tuplesInPage.lastElement().setData(dataInTuple);
	    		}
	    		pages.get(i).serializePage(pages.get(i), t);
	    	
	    	
	    		
	    	}
	    	
	    	else {
	    		throw new DBAppException("Invalid Type Entered");
	    	}
	    	
	    }
	    for (Page p : pages) {
	    	System.out.println("Updated: " + p.getTuplesInPage());
	    }
	}
	
	
	public void deleteFromTable(String strTableName , Hashtable<String,Object> htblColNameValue) throws DBAppException {strTableName = strTableName.toLowerCase();
	
		boolean exists = false;
		
		try ( BufferedReader b = new BufferedReader( new FileReader("resources/metadata.csv") ) ){
			String l = "";
			
			while ( (l = b.readLine()) != null ) {
				String [] data = l.split(",");
				
				if (data[0].equals(strTableName)) {
					exists = true;
					break;
				}
				
			}
		} catch (Exception w) {
			throw new DBAppException("Error");
		}
		
		if (!exists)
			throw new DBAppException("table Does Not Exist");
		
		
		Table t = null;
		
		for (int i = 0 ; i < tables.size() ; i++) {
			if (tables.get(i).getTableName().equals(strTableName))
				t = tables.get(i);
		}
		
		if (t == null)
			throw new DBAppException("Table Not Found");
		
		t.deserializeTable("resources/" + t.getTableName() + ".ser");
		
		
		String csvPath = "resources/metadata.csv";
	    String line = "";
	    String clusterKey = "";
	    String clusterKeyType = "";
	    Hashtable <String , String> colType = new Hashtable <> ();
	    
	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvPath));
		} catch (Exception e2) {
			throw new DBAppException("File Not Found");
		}
	    try {
			line = br.readLine();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
	    
	    while (line != null) {
	    	String [] row = line.split(",");
	    	
	    	if (row[3].equals("true") && row[0].equals(strTableName)) {
	    		clusterKey = row[1];
	    		clusterKeyType = row[2];
	    		colType.put(row[1], row[2]);
	    		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
	    	}
	    	else if (row[3].equals("false") && row[0].equals(strTableName)) {
	    		colType.put(row[1], row[2]);
	    		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
	    	} else
				try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
	    }
	    try {
			br.close();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
	    
	    Enumeration<String> e = htblColNameValue.keys();
		Vector <String> v = new Vector<>();
		
		while (e.hasMoreElements())
			v.add(e.nextElement().toLowerCase());
		
		boolean indexed = false;
		int count = 0;
		ArrayList arr = indexedColumns();
		
		for (int i = 0 ; i < arr.size() ; i++) {
			if (v.contains(arr.get(i))) {
				count++;
			}
		}
		
		if (count >= 3)
			indexed = true;
		
		if (indexed) {
			String indexName = "";
			
			for (int i = 0 ; i < arr.size() ; i++) {
				indexName += capitalize((String) arr.get(i));
			}
			indexName += "Index";
			
			
			Node n = deserializeIndex(indexName);
			
			
			String l = "";
			ArrayList a = new ArrayList();
			
			try (BufferedReader b = new BufferedReader(new FileReader("resources/metadata.csv"))){
				
				while ( (l = br.readLine()) != null) {
					String [] data = l.split(",");
					
					if (data[0].equals(strTableName) && data[4] != null) {
						if (htblColNameValue.containsKey(data[1])) {
							a.add(htblColNameValue.get(data[1]));
							break;
						}
					}
					
					
				}
				
				
				b.close();
				
			} catch (Exception z) {
				z.printStackTrace();
				throw new DBAppException("Error");
			}
			
			Object x = a.get(0);
			Object y = a.get(1);
			Object z = a.get(2);
			
			OctPoint octPt = new OctPoint(x , y , z , 0);
			
			n.delete(octPt);
			
		}
		
		
		else {
			boolean binarySearch = false;
			
			if ( v.contains(clusterKey) )
				binarySearch = true;
			
			Vector <Page> pages = t.getPages();
			
			if (binarySearch) {
				
				for (int i = 0 ; i < pages.size() ; i++) {
					Page page = pages.get(i);
					
					Tuple tmpTuple = pages.get(i).getTuplesInPage().lastElement();
					
					if ( clusterKeyType.equals("java.lang.Integer") ) {
						int clusterKeyValueDelete = Integer.parseInt((String) (htblColNameValue.get(clusterKey)));
						int valueInLastTupleInPage = (int) tmpTuple.get(clusterKey);
						Vector <Tuple> tuplesInPage = page.getTuplesInPage();
						
						if (clusterKeyValueDelete < valueInLastTupleInPage) {
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							
							
							
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = -1;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if ( (int) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValueDelete ) {
									low = mid + 1;
								}
								else if ( (int) tuplesInPage.get(mid).get(clusterKey) > clusterKeyValueDelete ) {
									high = mid - 1;
								}
								else {
									index = mid;
									break;
								}
							}
							
							if (index == -1) {
								return;
							}
							
							if (page.getTuplesInPage().size() == 1) {
								t.pages.remove(i);
								File file = new File ("resources/" + strTableName + " Page " + i + ".ser");
								file.delete();
								break;
							}
							
							else {
								tuplesInPage.remove(index);
								page.serializePage(page, t);
								break;
							}
							
							
							
						}
						
						else if (clusterKeyValueDelete == valueInLastTupleInPage) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							tuplesInPage.remove((int) tmpTuple.get(clusterKey) - 1);
							page.serializePage(page, t);
							break;
						}
						
						else if (clusterKeyValueDelete > valueInLastTupleInPage && (i + 1) > (pages.size() - 1)) {
							return;
						}
						
					}
					
					else if ( clusterKeyType.equals("java.lang.Double") ) {
						
		
						Double clusterKeyValueDelete = Double.parseDouble((String) (htblColNameValue.get(clusterKey)));
						Double valueInLastTupleInPage = (Double) tmpTuple.get(clusterKey);
						Vector <Tuple> tuplesInPage = page.getTuplesInPage();
						
						if (clusterKeyValueDelete < valueInLastTupleInPage) {
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							
							
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = -1;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if ( (Double) tuplesInPage.get(mid).get(clusterKey) < clusterKeyValueDelete ) {
									low = mid + 1;
								}
								else if ( (Double) tuplesInPage.get(mid).get(clusterKey) > clusterKeyValueDelete ) {
									high = mid - 1;
								}
								else {
									index = mid;
									break;
								}
							}
							
							if (index == -1) {
								return;
							}
							
							if (page.getTuplesInPage().size() == 1) {
								t.pages.remove(i);
								File file = new File ("resources/" + strTableName + " Page " + i + ".ser");
								file.delete();
								break;
							}
							
							else {
								tuplesInPage.remove(index);
								page.serializePage(page, t);
								break;
							}
							
							
							
						}
						
						else if (clusterKeyValueDelete == valueInLastTupleInPage) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							tuplesInPage.remove((int) tmpTuple.get(clusterKey) - 1);
							page.serializePage(page, t);
							break;
						}
						
						else if (clusterKeyValueDelete > valueInLastTupleInPage && (i + 1) > (pages.size() - 1)) {
							return;
						}
						
					
						
					}
					
					else if ( clusterKeyType.equals("java.lang.String") ) {
					
						String clusterKeyValueDelete = (String) (htblColNameValue.get(clusterKey));
						String valueInLastTupleInPage = (String) tmpTuple.get(clusterKey);
						Vector <Tuple> tuplesInPage = page.getTuplesInPage();
						
						if (clusterKeyValueDelete.compareTo(valueInLastTupleInPage) < 0) {
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							
							
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = -1;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if (((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValueDelete) < 0) {
									low = mid + 1;
								}
								else if (((String) tuplesInPage.get(mid).get(clusterKey)).compareTo(clusterKeyValueDelete) > 0) {
									high = mid - 1;
								}
								else {
									index = mid;
									break;
								}
							}
							
							if (index == -1) {
								return;
							}
							
							if (page.getTuplesInPage().size() == 1) {
								t.pages.remove(i);
								File file = new File ("resources/" + strTableName + " Page " + i + ".ser");
								file.delete();
								break;
							}
							
							else {
								tuplesInPage.remove(index);
								page.serializePage(page, t);
								break;
							}
							
							
							
						}
						
						else if (clusterKeyValueDelete == valueInLastTupleInPage) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							tuplesInPage.remove((int) tmpTuple.get(clusterKey) - 1);
							page.serializePage(page, t);
							break;
						}
						
						else if (clusterKeyValueDelete.compareTo(valueInLastTupleInPage) > 0 && (i + 1) > (pages.size() - 1)) {
							return;
						}
						
					}
					
					else if ( clusterKeyType.equals("java.lang.Date") ) {
						
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date clusterKeyValueDelete = null;
						try {
							clusterKeyValueDelete = (Date) format.parse((String) htblColNameValue.get(clusterKey));
						} catch (Exception e1) {
							throw new DBAppException("Error");
						}
						Date valueInLastTupleInPage = null;
						try {
							valueInLastTupleInPage = (Date) format.parse((String) tmpTuple.get(clusterKey));
						} catch (Exception e1) {
							throw new DBAppException("Error");
						}
						Vector <Tuple> tuplesInPage = page.getTuplesInPage();
						
						if (clusterKeyValueDelete.before(valueInLastTupleInPage)) {
							
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							
							
							// binary search
							int low = 0;
							int high = tuplesInPage.size() - 1;
							int mid = 0;
							int index = -1;
							
							// find correct index in tuplesInPage
							while (low <= high) {
								mid = (low + high) / 2;
								
								
								if (((Date) tuplesInPage.get(mid).get(clusterKey)).before(clusterKeyValueDelete)) {
									low = mid + 1;
								}
								else if (((Date) tuplesInPage.get(mid).get(clusterKey)).after(clusterKeyValueDelete)) {
									high = mid - 1;
								}
								else {
									index = mid;
									break;
								}
							}
							
							if (index == -1) {
								return;
							}
							
							if (page.getTuplesInPage().size() == 1) {
								t.pages.remove(i);
								File file = new File ("resources/" + strTableName + " Page " + i + ".ser");
								file.delete();
								break;
							}
							
							else {
								tuplesInPage.remove(index);
								page.serializePage(page, t);
								break;
							}
						}
						
						else if (clusterKeyValueDelete.equals(valueInLastTupleInPage)) {
							pages.get(i).deserializePage("resources/" + strTableName + " Page " + i + ".ser");
							tuplesInPage.remove((int) tmpTuple.get(clusterKey) - 1);
							page.serializePage(page, t);
							break;
						}
						
						else if (clusterKeyValueDelete.after(valueInLastTupleInPage) && (i + 1) > (pages.size() - 1)) {
							return;
						}
					}
					
				}
				
			}
			
			else {
				if (v.size() == 1) {
					for (int i = 0 ; i < pages.size() ; i++) {
						Page page = pages.get(i);
						page.deserializePage("resources/" + strTableName + " Page " + i + ".ser");
						Vector <Tuple> tuplesInPage = page.getTuplesInPage();
						
						String column = v.firstElement();
						String columnType = colType.get(v.firstElement());
						
						for (int j = 0 ; j < tuplesInPage.size() ; j++) {
							
							if (columnType.equals("java.lang.Integer")) {
								if ( ((int) (tuplesInPage.get(j).getData().get(column))) == Integer.parseInt( (String) (htblColNameValue.get(column))) )
									tuplesInPage.remove(j);
								
							}
							else if (columnType.equals("java.lang.Double")) {
								if ( ((Double) (tuplesInPage.get(j).getData().get(column))) == Double.parseDouble( (String) (htblColNameValue.get(column))) )
									tuplesInPage.remove(j);
							}
							else if (columnType.equals("java.lang.String")) {
								if ( ( (String) (tuplesInPage.get(j).getData().get(column)) ).equals((String) (htblColNameValue.get(column))) )
									tuplesInPage.remove(j);
							}
							else if (columnType.equals("java.lang.Date")) {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
								try {
									if ( format.parse( (String) (tuplesInPage.get(j).getData().get(column)) ).equals( format.parse((String) (htblColNameValue.get(column)))) )
										tuplesInPage.remove(j);
								} catch (Exception e1) {
									throw new DBAppException("Error");
								}
							}
						}
						
						page.serializePage(page, t);
					}
				}
				else {
					for (int i = 0 ; i < pages.size() ; i++) {
						Page page = pages.get(i);
						page.deserializePage("resources/" + strTableName + " Page " + i + ".ser");
						Vector <Tuple> tuplesInPage = page.getTuplesInPage();
						
						for (int j = 0 ; j < tuplesInPage.size() ; j++) {
							System.out.println("Hena: " + tuplesInPage.get(j).getData());
							if ( htblColNameValue.contains(tuplesInPage.get(j)) )
								tuplesInPage.remove(j);
						}
						page.serializePage(page, t);
					}
				}
				
			}
			
			for (int i = 0 ; i < pages.size() ; i++) {
				System.out.println("Updated after delete: " + pages.get(i).getTuplesInPage());
			}
	}
		t.serializeTable(t);
	}
	
	
	public Iterator selectFromTable(SQLTerm [] arrSQLTerms, String[] strarrOperators) throws DBAppException {
		
		
		
		for (int i = 0; i < arrSQLTerms.length; i++) {
			
			arrSQLTerms[i]._strTableName = arrSQLTerms[i]._strTableName.toLowerCase();
			arrSQLTerms[i]._strColumnName = arrSQLTerms[i]._strColumnName.toLowerCase();
			
			if(arrSQLTerms[i]._objValue instanceof String)
				arrSQLTerms[i]._objValue = ((String) arrSQLTerms[i]._objValue).toLowerCase();
		}
		
		for (int i = 0; i < strarrOperators.length; i++) {
			strarrOperators[i]=strarrOperators[i].toUpperCase();
		}
		
		for (int i = 0 ; i < arrSQLTerms.length ; i++) {
			String tableName = arrSQLTerms[i]._strTableName;
			String colName = arrSQLTerms[i]._strColumnName;
			boolean tableExists;
			boolean columnExists;
			
			try (BufferedReader br = new BufferedReader( new FileReader("resources/metadata.csv") )) {
				String line = "";
				
				tableExists = false;
				columnExists = false;
				
				while ( (line = br.readLine()) != null ) {
					String [] row = line.split(",");
					
					if (row[0].equals(tableName)) {
						tableExists = true;
						if (row[1].equals(colName)) {
							columnExists = true;
						}
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new DBAppException("error");
			}
			
			if (tableExists == false || columnExists == false)
				throw new DBAppException("Invalid Entries");
		}
		
		Vector <Vector> vctrSQLTermsResult = new Vector <> ();
		
		if (strarrOperators.length == 0 || strarrOperators == null)
			return vctrSelectFromTableHelper(arrSQLTerms[0]).iterator();
		
		for (SQLTerm term : arrSQLTerms)
			vctrSQLTermsResult.add( vctrSelectFromTableHelper(term) );
		
		
		Iterator itrResultSet = (mergeSQLTerms(vctrSQLTermsResult , strarrOperators)).iterator() ;
		
		
		return itrResultSet;
	}
	
	
	public Vector vctrSelectFromTableHelper (SQLTerm arrSQLTerms) throws DBAppException {
		Vector vctrResultSet = new Vector <> ();
		
		Table t = retrieveTable(arrSQLTerms._strTableName);
		String columnName = arrSQLTerms._strColumnName;
		String operator = arrSQLTerms._strOperator;
		Object value = arrSQLTerms._objValue;
		boolean columnHasIndex = false;
		
		String csvPath = "resources/metadata.csv";
        String line = "";
        String columnType = "";
        Hashtable <String , String> colType = new Hashtable <> ();
        
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvPath));
		} catch (Exception e2) {
			throw new DBAppException("File Not Found");
		}
        try {
			line = br.readLine();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        while (line != null) {
        	String [] row = line.split(",");
        	
        	if (row[1].equals(columnName) && row[4].equals("null") ) {
        		columnType = row[2];
        		colType.put(row[1], row[2]);
        		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
        	}
        	else if (row[1].equals(columnName) && ! row[4].equals("null") ) {
        		columnType = row[2];
        		columnHasIndex = true;
        		colType.put(row[1], row[2]);
        		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
        	}
        	else
				try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("IO Problem");
				}
        }
        try {
			br.close();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
		
		
		t.deserializeTable(getTablePath(t));
		
		Vector <Page> pagesInTable = t.getPages();
		
		if (! columnHasIndex) {
			for (int i = 0 ; i < pagesInTable.size() ; i++) {
				Page curPage = pagesInTable.get(i);
				
				curPage.deserializePage("resources/" + t.getTableName() + " Page " + i + ".ser");
				
				Vector <Tuple> tuplesInPage = curPage.getTuplesInPage();
				
				for (int k = 0 ; k < tuplesInPage.size() ; k++) {
					
					switch (operator) {
					
					case ("="):
						if (tuplesInPage.get(k).get(columnName).equals(value)) 
							vctrResultSet.add(tuplesInPage.get(k));
						break;
						
					case ("!="):
						if (! tuplesInPage.get(k).get(columnName).equals(value)) 
							vctrResultSet.add(tuplesInPage.get(k));
						break;
					
					case ("<"):
						
						switch (columnType) {
						case ("java.lang.Integer"):
							if ( (int) tuplesInPage.get(k).get(columnName) < (int) value )
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Double"):
							if ( (Double) (tuplesInPage.get(k).get(columnName)) <   (Double) value)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.String"):
							if ( ((String) tuplesInPage.get(k).get(columnName)).compareTo((String) value) < 0)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Date"):
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							
							try {
								if ( ((Date) format.parse( (String) tuplesInPage.get(k).get(columnName))).before( (Date) format.parse( (String) value) ) ) {
									vctrResultSet.add(tuplesInPage.get(k));
								}
							} catch (Exception e) {
								throw new DBAppException("Date Cast Error");
							}
							break;
						}
					
						break;
					
					case (">"):
						
						switch (columnType) {
						case ("java.lang.Integer"):
							if ( (int) tuplesInPage.get(k).get(columnName) > (int) value )
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Double"):
							if ( (Double) (tuplesInPage.get(k).get(columnName)) >   (Double) value) 
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.String"):
							if ( ((String) tuplesInPage.get(k).get(columnName)).compareTo((String) value) > 0)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Date"):
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							
							try {
								if ( ((Date) format.parse( (String) tuplesInPage.get(k).get(columnName))).after( (Date) format.parse( (String) value) ) ) {
									vctrResultSet.add(tuplesInPage.get(k));
								}
							} catch (Exception e) {
								throw new DBAppException("Date Cast Error");
							}
							break;
						}
					
						break;
					
					case (">="):
						
						switch (columnType) {
						case ("java.lang.Integer"):
							if ( (int) tuplesInPage.get(k).get(columnName) >= (int) value )
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Double"):
							if ( (Double) (tuplesInPage.get(k).get(columnName)) >=   (Double) value)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.String"):
							if ( ((String) tuplesInPage.get(k).get(columnName)).compareTo((String) value) >= 0)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Date"):
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							
							try {
								if ( ((Date) format.parse( (String) tuplesInPage.get(k).get(columnName))).after( (Date) format.parse( (String) value) )  && 
										((Date) format.parse( (String) tuplesInPage.get(k).get(columnName))).equals( (Date) format.parse( (String) value) ) ) {
									vctrResultSet.add(tuplesInPage.get(k));
								}
							} catch (Exception e) {
								throw new DBAppException("Date Cast Error");
							}
							break;
						}
						
						break;
					
					case ("<="):
						
						switch (columnType) {
						case ("java.lang.Integer"):
							if ( (int) tuplesInPage.get(k).get(columnName) <= (int) value )
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Double"):
							if ( (Double) (tuplesInPage.get(k).get(columnName)) <=   (Double) value)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.String"):
							if ( ((String) tuplesInPage.get(k).get(columnName)).compareTo((String) value) <= 0)
								vctrResultSet.add(tuplesInPage.get(k));
							break;
						
						case ("java.lang.Date"):
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							
							try {
								if ( ((Date) format.parse( (String) tuplesInPage.get(k).get(columnName))).before( (Date) format.parse( (String) value) )  && 
										((Date) format.parse( (String) tuplesInPage.get(k).get(columnName))).equals( (Date) format.parse( (String) value) ) ) {
									vctrResultSet.add(tuplesInPage.get(k));
								}
							} catch (Exception e) {
								throw new DBAppException("Date Cast Error");
							}
							break;
						}
						
						break;
						
					
					default:
						throw new DBAppException("Invalid Operator");
					}
				}
			}
		}
		
		else { // has index
			
		}
		return vctrResultSet;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector mergeSQLTerms(Vector vectorsToMerge, String [] strarrOperators) {
		
		Vector resultOfTwoVectors = new Vector <>();
		Vector temp1;
		Vector temp2;
		int d = 0;
		
		for(int i = 0 ; i < strarrOperators.length ; i++) {
			
			for(; d < vectorsToMerge.size() ; d++) {
			
				temp1 = (Vector) vectorsToMerge.elementAt(d);
				
				if(! ( (d + 1) >= vectorsToMerge.size()) ) {
				
					temp2 = (Vector) vectorsToMerge.elementAt(d + 1);
					
					if (strarrOperators[i].equals("OR")) {
						temp1.addAll(temp2);
						resultOfTwoVectors = temp1;
						vectorsToMerge.remove(d + 1);
						vectorsToMerge.add(d + 1, resultOfTwoVectors);
						d++;
						break;
					}
					else if (strarrOperators[i].equals("AND")) {
						temp1.retainAll(temp2);
						resultOfTwoVectors = temp1;
						vectorsToMerge.remove(d + 1);
						vectorsToMerge.add(d + 1, resultOfTwoVectors);
						d++;
						break;
					}
					else if (strarrOperators[i].equals("XOR")) {
						
					}
				}
				else 
					break;
				
			}
		}
		return resultOfTwoVectors;
	}
	
	
	public Table retrieveTable (String strTableName) {
		Table t = null;
		
		for (int i = 0; i < tables.size(); i++) 
			if (strTableName.equals(tables.get(i).getTableName())) {
				t = tables.get(i);
				break;
			}
		return t;
	}
	
	
	public String getTablePath(Table t) {
		return "resources/" + t.getTableName() + ".ser";
	}
	
	
	public Object getMax (String columnName , String tableName) throws DBAppException {

		
		String csvPath = "resources/metadata.csv";
        String line = "";
        
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvPath));
		} catch (Exception e2) {
			throw new DBAppException("File Not Found");
		}
        try {
			line = br.readLine();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        while (line != null) {
        	String [] row = line.split(",");
        	
        	if (columnName.equals(row[1]) && tableName.equals(row[0])) {
        		return row[7];
        	} else {
        		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("Error in Buffered Reader");
				}
        	}
        }
        
        try {
			br.close();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        throw new DBAppException("Column Not Found");
        
        
	}
	
	
	public Object getMin (String columnName , String tableName) throws DBAppException {
		
		String csvPath = "resources/metadata.csv";
        String line = "";
        
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvPath));
		} catch (Exception e2) {
			throw new DBAppException("File Not Found");
		}
        try {
			line = br.readLine();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        while (line != null) {
        	String [] row = line.split(",");
        	
        	if (columnName.equals(row[1]) && tableName.equals(row[0])) {
        		return row[6];
        	} else {
        		try {
					line = br.readLine();
				} catch (Exception e) {
					throw new DBAppException("Error in Buffered Reader");
				}
        	}
        }
        
        try {
			br.close();
		} catch (Exception e2) {
			throw new DBAppException("IO Problem");
		}
        
        throw new DBAppException("Column Not Found");
        
	}
	
	
	public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
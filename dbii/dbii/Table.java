package dbii;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import dbapp.exceptions.DBAppException;

public class Table implements Serializable {
	
	private static final long serialVersionUID = 1L;
	transient String tableName;
	transient String clusterKey;
	transient Hashtable <String , String> colNameType;
	transient Hashtable <String, String> colNameMin;
	transient Hashtable <String , String> colNameMax;
	transient int tableSize;
	transient int noTotalPages;
	Vector <Page> pages;
	
	
	public Table (String strTableName , String strClusteringKeyColumn, Hashtable <String,String> htblColNameType, 
			Hashtable <String,String> htblColNameMin, Hashtable <String,String> htblColNameMax) {
		
		this.tableName = strTableName;
		this.clusterKey = strClusteringKeyColumn;
		this.colNameType = htblColNameType;
		this.colNameMin = htblColNameMin;
		this.colNameMax = htblColNameMax;
		this.tableSize = 0;
		this.noTotalPages = -1;
		this.pages = new Vector <Page> ();
	}
	
	public void addPageIntoTotalPages () {
		noTotalPages++;
	}
	
	public Vector <Page> getPages () {
		return this.pages;
	}
	
	
	
	public void setPages(Vector<Page> pages) {
		this.pages = pages;
	}


	public void addRowIntoTableSize () {
		this.tableSize++;
	}
	
	public void serializeTable (Table t) throws DBAppException {
		try {
			FileOutputStream fileOut = new FileOutputStream("resources/" + t.getTableName() + ".ser");
			ObjectOutputStream out = new ObjectOutputStream (fileOut);
			out.writeObject(t);
			out.close();
			fileOut.close();
			
		} catch (Exception e) {
			throw new DBAppException("Serialize Failed");
		}
	}
	
	public Table deserializeTable (String fileName) throws DBAppException {
		Table t = null;
		
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			t = (Table) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (Exception e) {
			throw new DBAppException("Error in Deserializing");
		}
		
		return t;
	}


	public Hashtable<String, String> getColNameMin() {
		return colNameMin;
	}


	public void setColNameMin(Hashtable<String, String> colNameMin) {
		this.colNameMin = colNameMin;
	}


	public Hashtable<String, String> getColNameMax() {
		return colNameMax;
	}


	public void setColNameMax(Hashtable<String, String> colNameMax) {
		this.colNameMax = colNameMax;
	}


	public String getTableName() {
		return tableName;
	}


	public String getClusterKey() {
		return clusterKey;
	}


	public Hashtable<String, String> getColNameType() {
		return colNameType;
	}


	public int getTableSize() {
		return tableSize;
	}


	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}


	public int getNoTotalPages() {
		return noTotalPages;
	}


	public void setNoTotalPages(int noTotalPages) {
		this.noTotalPages = noTotalPages;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public void setClusterKey(String clusterKey) {
		this.clusterKey = clusterKey;
	}


	public void setColNameType(Hashtable<String, String> colNameType) {
		this.colNameType = colNameType;
	}
	
	
	
	
}

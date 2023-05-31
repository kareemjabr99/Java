package dbii;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import dbapp.exceptions.DBAppException;

public class Page extends Table implements Serializable {
	
	private static final long serialVersionUID = 1L;
	transient int maxRowsPerPage;
	transient int maxEntriesOctTree;
	transient int noOfEntriesInPage;
	Vector <Tuple> tuplesInPage = new Vector <Tuple> ();
	

	public Page (Tuple tuples , Table t) {
		super( t.getTableName() , t.getClusterKey() , t.getColNameType() , t.getColNameMin() , t.getColNameMax() );
		maxRowsPerPage = readMaxRowsPerPage();
		maxEntriesOctTree = readMaxEntriesOctTree();
		tuplesInPage.add(tuples);
		this.noOfEntriesInPage = 0;
	}
	
	public void serializePage (Page p , Table t) throws DBAppException {
		try {
			int index = -1;
			for (int i = 0 ; i < t.getPages().size() ; i++) {
				if (t.getPages().get(i) == p) {
					index = i;
					break;
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream("resources/" + p.getTableName() + " Page " + index + ".ser");
			ObjectOutputStream out = new ObjectOutputStream (fileOut);
			out.writeObject(p);
			out.close();
			fileOut.close();
			
			
			
			
		} catch (Exception e) {
			throw new DBAppException("Serialize Failed");
		}
	}
	
	public Page deserializePage (String fileName) throws DBAppException {
		Page p = null;
		
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			p = (Page) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (Exception e) {
			throw new DBAppException("Deserialize Failed");
		}
		
		return p;
	}
	
	public void setMax () {
		super.colNameMax = super.colNameMax;
	}
	
	
	public Vector <Tuple> getTuplesInPage() {
		return tuplesInPage;
	}



	public void setTuplesInPage(Vector<Tuple> tuplesInPage) {
		this.tuplesInPage = tuplesInPage;
	}



	public boolean isFull () {
		if (noOfEntriesInPage == maxRowsPerPage)
			return true;
		else
			return false;
	}
	
	public void addRowInPage () {
		this.noOfEntriesInPage++;
	}
	
	public int readMaxRowsPerPage () {
		ConfigReader reader = new ConfigReader();
        String configFileContents = reader.readConfigFile("DBApp.config");
        String [] configFileContentsArr = configFileContents.split("[ = ,\n]");
        int rowsPerPage = Integer.parseInt(configFileContentsArr[3]);
        
		return rowsPerPage;
	}
	
	public int readMaxEntriesOctTree () {
		ConfigReader reader = new ConfigReader();
        String configFileContents = reader.readConfigFile("DBApp.config");
        String [] configFileContentsArr = configFileContents.split("[ = ,\n]");
        int entriesPerOct = Integer.parseInt(configFileContentsArr[7]);
        
		return entriesPerOct;
	}
}

package dbii;

import java.io.Serializable;
import java.util.Hashtable;

public class Tuple implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String primaryKeyValue;
	Hashtable <String, Object> data;
	String tableName;

	public Tuple(String strTableName , String primaryKeyValue, Hashtable <String,Object> dataInRow) {
		this.primaryKeyValue = primaryKeyValue;
		this.data = dataInRow;
		this.tableName = strTableName;
	}
	
	
	
	public Hashtable<String, Object> getData() {
		return data;
	}

	public void setData(Hashtable<String, Object> data) {
		this.data = data;
	}



	public Object get(String columnName) {
		return data.get(columnName);
	}
	
	public Object primaryKeyColumn() {
		return this.get(primaryKeyValue);
	}
	
	public void put(String columnName, Object columnValue) {
		data.put(columnName, columnValue);
	}
	
	public String toString() {
		String res = "";
		
		res += tableName + " ,, " + primaryKeyValue + " ,, " + data;
		
		return res;
	}
	
}

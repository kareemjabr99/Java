package dbii;

import java.io.*;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Vector;

import dbapp.exceptions.DBAppException;

@SuppressWarnings("all")
public class Node implements Serializable{
	
	boolean leaf;
	
	Vector <OctPoint> values = new Vector<>();
	Vector <Node> children = new Vector<>();
	
	int pageID;
	
	Range r;
	
	int maxEntries;
	
	
	public Node (Range r) {
		this.r = r;
		this.maxEntries = getMaxEntriesOctTree();
		
	}
	
	public boolean isLeaf() {
		if(this.children.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public int getMaxEntriesOctTree () {
		ConfigReader reader = new ConfigReader();
        String configFileContents = reader.readConfigFile("DBApp.config");
        String [] configFileContentsArr = configFileContents.split("[ = ,\n]");
        int entriesPerOct = Integer.parseInt(configFileContentsArr[7]);
        
		return entriesPerOct;
	}
	
	public void delete(OctPoint point) {
		
		if(this.isLeaf()) {
			
			if(pointWithinBounds(point, this.getRange())) {
				
				if(!(this.values.isEmpty())) {
					
					for(int j = 0 ; j < this.values.size(); j++) {
						
						if(point.equals(this.values.get(j))) {
							
							this.values.remove(j);
							if(!(this.checkChildren(this.children)))
									this.children = null;
							return;
							
						}
					}
				}
			}
		}
		else {
			for(int i = 0 ; i < this.children.size() ; i++) {
				Node node = this.children.get(i);
				node.delete(point);
			}
		}
	}
	
	public void update(OctPoint oldPoint, OctPoint newPoint) {
		if(this.isLeaf()) {
			
			if(pointWithinBounds(oldPoint, this.getRange())) {
				
				if(!(this.values.isEmpty())) {
					
					for(int j = 0 ; j < this.values.size(); j++) {
						
						if(oldPoint.equals(this.values.get(j))) {
							
							this.values.remove(j);
							this.insert(newPoint, this);
							
							return;
						}
					}
				}
			}
		}
		else {
			for(int i = 0 ; i < this.children.size() ; i++) {
				Node node = this.children.get(i);
				node.update(oldPoint, newPoint);
			}
		}
	}
	
	public boolean checkChildren(Vector c) {
		Boolean flag = true;
		for(int i = 0 ; i < c.size() ; i++) {
			Node tmpNode = (Node) c.get(i);
			
			if(!(tmpNode.values.isEmpty())) {
				flag = false;
			}
		}
		return flag;
			
	}
	
	public void initializeRanges() {
		
		
		for (int i = 0 ; i < 8 ; i++) {
			Range ra = new Range();
			
			if (i % 2 == 0) {
				ra.minX = r.minX;
				ra.maxX = getMid(this.r.minX , this.r.maxX);
			}
			else {
		        ra.minX = getMid(r.minX, r.maxX);
		        ra.maxX = r.maxX;
		    }
			
			if (i % 4 == 2) {
				ra.minY = this.r.minY;
				ra.maxY = getMid(this.r.minY , this.r.maxY);
			}
			else {
		        ra.minY = getMid(r.minY, r.maxY);
		        ra.maxY = r.maxY;
		    }
			
			if (i > 4) {
				ra.minZ = this.r.minZ;
				ra.maxZ = getMid(this.r.minZ , this.r.maxZ);
			}
			else {
		        ra.minZ = getMid(r.minZ, r.maxZ);
		        ra.maxZ = r.maxZ;
		    }
			
			children.add(new Node(ra));
		}
		
		for(int i = 0 ; i < children.size() ; i++) {
			
			Node tempNode = children.get(i);
		
			for (int j = 0 ; j < values.size() ; j++) {
				
				OctPoint point = values.get(i);
				insert(point, tempNode);
				
			}
		}
		
		this.values = null;
	}
	
	public void insertInNewlyCreatedIndex (OctPoint point, Vector node) {
		
		for(int i = 0 ; i < node.size() ; i++) {
			
			Node tempNode = (Node) node.get(i);
			insert(point, tempNode);
			
		}
		
	}
	
	public void insert (OctPoint point, Node node) {
		
		if(node.isLeaf()){
			
			if(node.values.size() <= node.getMaxEntriesOctTree()) {
				
				if (pointWithinBounds(point, node.getRange())) { 
					node.values.add(point);
	            }
				
			}
			else {
				node.initializeRanges();
				insert(point, node);
			}
			
			
		}
		else {
				
			for(int i = 0 ; i < children.size() ; i++) {
				
				Node tempNode = children.get(i);
				insert(point, tempNode);
			}
		}
	}
	
	public boolean pointWithinBounds(OctPoint point, Range r) {
		
		Object xCorrPoint = point.getX();
	    Object yCorrPoint = point.getY();
	    Object zCorrPoint = point.getZ();

	    Object minX = r.minX;
	    Object minY = r.minY;
	    Object minZ = r.minZ;
	    Object maxX = r.maxX;
	    Object maxY = r.maxY;
	    Object maxZ = r.maxZ;

	    return compareValues(xCorrPoint, minX, maxX) && compareValues(yCorrPoint, minY, maxY) && compareValues(zCorrPoint, minZ, maxZ);
	
		
//		if (point.getX() instanceof Integer) {
//			
//			Integer x = (Integer) point.getX();
//			Integer rangeMin = (Integer) r.minX;
//			Integer rangeMax = (Integer) r.maxX;
//			
//		}
//		else if(point.getX() instanceof Double) {
//			Double x = (Double) point.getX();
//			Double rangeMin = (Double) r.minX;
//			Double rangeMax = (Double) r.maxX;
//		}
//		else if(point.getX() instanceof Date) {
//			Date x = (Date) point.getX();
//			Date rangeMin = (Date) r.minX;
//			Date rangeMax = (Date) r.maxX;
//		}
//		else if(point.getX() instanceof String) {
//			String y = (String) point.getX();
//			String rangeMin = (String) r.minX;
//			String rangeMax = (String) r.maxX;
//		}
//		
//		if (point.getY() instanceof Integer) {
//			
//			Integer y = (Integer) point.getX();
//			Integer rangeMin = (Integer) r.minX;
//			Integer rangeMax = (Integer) r.maxX;
//			
//		}
//		else if(point.getY() instanceof Double) {
//			Double y = (Double) point.getY();
//			Double rangeMin = (Double) r.minX;
//			Double rangeMax = (Double) r.maxX;
//		}
//		else if(point.getY() instanceof Date) {
//			Date y = (Date) point.getY();
//			Date rangeMin = (Date) r.minX;
//			Date rangeMax = (Date) r.maxX;
//		}
//		else if(point.getY() instanceof String) {
//			String y = (String) point.getY();
//			String rangeMin = (String) r.minX;
//			String rangeMax = (String) r.maxX;
//		}
//		
//		if (point.getZ() instanceof Integer) {
//			
//			Integer z = (Integer) point.getZ();
//			Integer rangeMin = (Integer) r.minX;
//			Integer rangeMax = (Integer) r.maxX;
//			
//		}
//		else if(point.getZ() instanceof Double) {
//			Double z = (Double) point.getY();
//			Double rangeMin = (Double) r.minX;
//			Double rangeMax = (Double) r.maxX;
//		}
//		else if(point.getZ() instanceof Date) {
//			Date z = (Date) point.getZ();
//			Date rangeMin = (Date) r.minX;
//			Date rangeMax = (Date) r.maxX;
//		}
//		else if(point.getZ() instanceof String) {
//			String z = (String) point.getZ();
//			String rangeMin = (String) r.minX;
//			String rangeMax = (String) r.maxX;
//		}
		

	}
	
	private boolean compareValues(Object pointValue, Object minValue, Object maxValue) {
	    if (pointValue instanceof Integer) {
	        int pointInt = (int) pointValue;
	        int minInt = (int) minValue;
	        int maxInt = (int) maxValue;
	        return pointInt >= minInt && pointInt <= maxInt;
	    } else if (pointValue instanceof Double) {
	        double pointDouble = (double) pointValue;
	        double minDouble = (double) minValue;
	        double maxDouble = (double) maxValue;
	        return pointDouble >= minDouble && pointDouble <= maxDouble;
	    } else if (pointValue instanceof Date) {
	        Date pointDate = (Date) pointValue;
	        Date minDate = (Date) minValue;
	        Date maxDate = (Date) maxValue;
	        return !pointDate.before(minDate) && !pointDate.after(maxDate);
	    } else if (pointValue instanceof String) {
	        String pointString = (String) pointValue;
	        String minString = (String) minValue;
	        String maxString = (String) maxValue;
	        return pointString.compareTo(minString) >= 0 && pointString.compareTo(maxString) <= 0;
	    }

	    return false; // Return false if the point value is not of a supported type
	}
	
	public Range getRange() {
		return this.r;
	}
	
	public Object getMid (Object min , Object max) {
		if (min instanceof Integer && max instanceof Integer) {
			Integer minInt = (Integer) min;
			Integer maxInt = (Integer) max;
			return (minInt + maxInt) / 2;
		}
		
		else if (min instanceof Double && max instanceof Double) {
			Double minDob = (Double) min;
			Double maxDob = (Double) max;
			return (minDob + maxDob) / 2;
		}
		
		else if (min instanceof Date && max instanceof Date) {
			long minDate = ((Date) min).getTime();
			long maxDate = ((Date) max).getTime();
			return new Date((minDate + maxDate) / 2);
		}
		
		else {
			return getMiddleString((String) min , (String) max);
		}
	}
	
	public void serializeIndex (Node n) throws DBAppException {
		try {
			FileOutputStream fileOut = new FileOutputStream("resources/Index.ser");
			ObjectOutputStream out = new ObjectOutputStream (fileOut);
			out.writeObject(n);
			out.close();
			fileOut.close();
			
		} catch (Exception e) {
			throw new DBAppException("Serialize Failed");
		}
	}
	
	public Node deserializeIndex (String fileName) throws DBAppException {
		Node n = null;
		
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			n = (Node) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (Exception e) {
			throw new DBAppException("Error in Deserializing");
		}
		
		return n;
	}
	
	public Object getMiddleString(String S, String T) {
		
		int N;
		
		if (S.length() > T.length())
			N = S.length();
		else
			N = T.length();
		
		if(S.length() != T.length()) {
			if(S.length()>T.length()) {
				
				int difference = S.length() - T.length();
				
				for(int i = 0 ; i < difference ; i++) {
					T = T + S.charAt(T.length());
				}
			}
			
			else {
				int difference = T.length() - S.length();
				for(int i = 0 ; i < difference ; i++) {
					S = S + T.charAt(S.length());
				}
			}
		}
		
		
        // Stores the base 26 digits after addition
        int[] a1 = new int[N + 1];
 
        for (int i = 0; i < N; i++) {
            a1[i + 1] = (int) S.charAt(i) - 97
                        + (int)T.charAt(i) - 97;
        }
 
        // Iterate from right to left
        // and add carry to next position
        for (int i = N; i >= 1; i--) {
            a1[i - 1] += (int)a1[i] / 26;
            a1[i] %= 26;
        }
 
        // Reduce the number to find the middle
        // string by dividing each position by 2
        for (int i = 0; i <= N; i++) {
 
            // If current value is odd,
            // carry 26 to the next index value
            if ((a1[i] & 1) != 0) {
 
                if (i + 1 <= N) {
                    a1[i + 1] += 26;
                }
            }
 
            a1[i] = (int)a1[i] / 2;
        }
        
        String result = "";
         
        for (int i = 1; i <= N; i++) {
            result += (char)(a1[i] + 97);
        }
        
        return result;
    }
	
	public static void main(String[] args) {
		
	}
	
}
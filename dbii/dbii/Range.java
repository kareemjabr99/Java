package dbii;

public class Range {
	
	Object minX;
	Object maxX;
	Object minY;
	Object maxY;
	Object minZ;
	Object maxZ;
	
	public Range() {
		
	}
	
	
	public Range(Object minX , Object maxX , Object minY , Object maxY , Object minZ , Object maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}

}

package dbii;

public class OctPoint {

    private Object x;
    private Object y;
    private Object z;
    private int pageID;

	private boolean nullify = false;

    public OctPoint(Object x, Object y, Object z, int pageID){
        this.x = x;
        this.y = y;
        this.z = z;
        this.pageID = pageID;
    }
    
    public int getPageID() {
		return pageID;
	}

	public void setPageID(int pageID) {
		this.pageID = pageID;
	}

    public OctPoint(){
        nullify = true;
    }

    public Object getX(){
        return x;
    }

    public Object getY(){
        return y;
    }

    public Object getZ(){
        return z;
    }

    public boolean isNullified(){
        return nullify;
    }
    
    public boolean equals(OctPoint p) {
    	return this.x.equals(p.getX()) && this.y.equals(p.getY()) && this.z.equals(p.getZ());
    }
   
}

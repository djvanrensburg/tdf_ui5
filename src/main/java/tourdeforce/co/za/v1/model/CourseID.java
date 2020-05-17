package tourdeforce.co.za.v1.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

public class CourseID implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="coursename", nullable=false)
	private String coursename;

	@Id
	@Column(name="country", nullable=false)
	private String country;
	
	 @Override
	    public boolean equals(Object obj) {
		    if (this == obj)
			    return true;
		    if (obj == null)
			    return false;
		    if (getClass() != obj.getClass())
			    return false;
		    CourseID other = (CourseID) obj;
		    if (coursename == null) {
			    if (other.coursename != null)
				    return false;
		    } else if (!coursename.equals(other.coursename))
			    return false;
		    if (country == null) {
			    if (other.country != null)
				    return false;
		    } else if (!country.equals(other.country))
			    return false;
		    return true;
	    }

	    @Override
	    public int hashCode() {
		    final int prime = 31;
		    int result = 1;
		    result = prime * result + ((coursename == null) ? 0 : coursename.hashCode());
		    result = prime * result + ((country == null) ? 0 : country.hashCode());
		    return result;
	    }
}

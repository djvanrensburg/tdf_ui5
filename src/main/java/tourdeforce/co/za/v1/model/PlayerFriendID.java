package tourdeforce.co.za.v1.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class PlayerFriendID implements Serializable{


		private static final long serialVersionUID = 1L;
		
		@Id
		@Column(name="friendEmail", nullable=false)
		private String friendEmail;

		@Id
		@Column(name="myEmail", nullable=false)
		private String myEmail;

		
		  @Override
		    public boolean equals(Object obj) {
			    if (this == obj)
				    return true;
			    if (obj == null)
				    return false;
			    if (getClass() != obj.getClass())
				    return false;
			    PlayerFriendID other = (PlayerFriendID) obj;
			    if (friendEmail == null) {
				    if (other.friendEmail != null)
					    return false;
			    } else if (!friendEmail.equals(other.friendEmail))
				    return false;
			    if (myEmail == null) {
				    if (other.myEmail != null)
					    return false;
			    } else if (!myEmail.equals(other.myEmail))
				    return false;
			    return true;
		    }

		    @Override
		    public int hashCode() {
			    final int prime = 31;
			    int result = 1;
			    result = prime * result + ((friendEmail == null) ? 0 : friendEmail.hashCode());
			    result = prime * result + ((myEmail == null) ? 0 : myEmail.hashCode());
			    return result;
		    }
}

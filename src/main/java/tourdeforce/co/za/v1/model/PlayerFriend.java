package tourdeforce.co.za.v1.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import tourdeforce.co.za.v1.model.Player;

@IdClass(PlayerFriendID.class)
@Entity(name = "PlayerFriend")
@Table(name = "PlayerFriend")
@NamedQuery(name = "PlayerFriend.findAll", query = "SELECT p FROM PlayerFriend p")
public class PlayerFriend implements Serializable {
	private static final long serialVersionUID = 1L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Column(name="id", unique=true, nullable=false)
//	private Long id;

	@Id
	@Column(name="friendEmail", nullable=false)
	private String friendEmail;

	@Id
	@Column(name="myEmail", nullable=false)
	private String myEmail;

	@Column
	private Date friendshipDate;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "friendEmail", insertable = false, updatable = false)
	private Player friend;
	
	public PlayerFriend() {

	}

//	public Long getId() {
//		return id;
//	}
	public Player getFriend() {
		return this.friend;
	}

	public void setFriend(Player friend) {
		this.friend = friend;
	}
	
	public String getMyEmail() {
		return this.myEmail;
	}

	public void setMyEmail(String myEmail) {
		this.myEmail = myEmail;
	}

	public String getFriendEmail() {
		return friendEmail;
	}

	public void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}

	public Date getFriendshipDate() {
		return friendshipDate;
	}

	public void setFriendshipDate(Date friendshipDate) {
		this.friendshipDate = friendshipDate;
	}
}

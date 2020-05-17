package tourdeforce.co.za.v1.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@IdClass(FavouriteCourseID.class)
@Entity(name = "FavouriteCourse")
@Table(name = "FavouriteCourse")
@NamedQuery(name = "FavouriteCourse.findAll", query = "SELECT p FROM FavouriteCourse p")
public class FavouriteCourse implements Serializable {
	private static final long serialVersionUID = 1L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Column(name="id", unique=true, nullable=false)
//	private Long id;

	@Id
	@Column(name = "myEmail", nullable = false)
	private String myEmail;

	@Id
	@Column(name = "coursename", nullable = false)
	private String coursename;

	@Id
	@Column(name = "country", nullable = false)
	private String country;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "myEmail", insertable = false, updatable = false)
	private Player friend;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "coursename", referencedColumnName="coursename", insertable = false, updatable = false),
			@JoinColumn(name = "country", referencedColumnName="country", insertable = false, updatable = false) })
	private Course course;

	public FavouriteCourse() {

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

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	public String getMyEmail() {
		return this.myEmail;
	}

	public void setMyEmail(String myEmail) {
		this.myEmail = myEmail;
	}

	public String getCoursename() {
		return this.coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}

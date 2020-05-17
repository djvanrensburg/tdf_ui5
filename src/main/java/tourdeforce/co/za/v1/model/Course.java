package tourdeforce.co.za.v1.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.config.CacheIsolationType;

@IdClass(CourseID.class)
@Entity(name = "Course")
@Table(name="Course")
@NamedQuery(name="Course.findAll", query="SELECT p FROM Course p")
@Cache(isolation=CacheIsolationType.ISOLATED)
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="coursename", nullable=false)
	private String coursename;

	@Id
	@Column(name="country", nullable=false)
	private String country;
	
	@Column
	private int parmen1;
		
	@Column
	private int strokemen1;
	
	@Column
	private int parmen2;
	
	@Column
	private int strokemen2;
	
	@Column
	private int parmen3;
	
	@Column
	private int strokemen3;
	
	@Column
	private int parmen4;
	
	@Column
	private int strokemen4;
	
	@Column
	private int parmen5;
	
	@Column
	private int strokemen5;
	
	@Column
	private int parmen6;
	
	@Column
	private int strokemen6;
	
	@Column
	private int parmen7;
	
	@Column
	private int strokemen7;
	
	@Column
	private int parmen8;
	
	@Column
	private int strokemen8;
		
	@Column
	private int parmen9;
	
	@Column
	private int strokemen9;
	
	@Column
	private int parmen10;
	
	@Column
	private int strokemen10;
	
	@Column
	private int parmen11;
	
	@Column
	private int strokemen11;
	
	@Column
	private int parmen12;
	
	@Column
	private int strokemen12;
	
	@Column
	private int parmen13;
	
	@Column
	private int strokemen13;
	
	@Column
	private int parmen14;
	
	@Column
	private int strokemen14;
	
	@Column
	private int parmen15;
	
	@Column
	private int strokemen15;
	
	@Column
	private int parmen16;
	
	@Column
	private int strokemen16;
	
	@Column
	private int parmen17;
	
	@Column
	private int strokemen17;
	
	@Column
	private int parmen18;
	
	@Column
	private int strokemen18;
		
	@Lob
	@Column(length=100000000)
	private byte [] photo;
	
	@Column
	private String photoMimeType = null;
	
	public Course() {
		
	}
	
	public String getCoursename() {
		return coursename;
	}
	
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getPhotoMimeType() {
		return photoMimeType;
	}

	public void setPhotoMimeType(String photoMimeType) {
		this.photoMimeType = photoMimeType;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	//get pars
	public int getParmen1() {
		return this.parmen1;
	}	
	public int getParmen2() {
		return this.parmen2;
	}
	public int getParmen3() {
		return this.parmen3;
	}
	public int getParmen4() {
		return this.parmen4;
	}
	public int getParmen5() {
		return this.parmen5;
	}
	public int getParmen6() {
		return this.parmen6;
	}
	public int getParmen7() {
		return this.parmen7;
	}
	public int getParmen8() {
		return this.parmen8;
	}
	public int getParmen9() {
		return this.parmen9;
	}
	public int getParmen10() {
		return this.parmen10;
	}
	public int getParmen11() {
		return this.parmen11;
	}
	public int getParmen12() {
		return this.parmen12;
	}
	public int getParmen13() {
		return this.parmen13;
	}
	public int getParmen14() {
		return this.parmen14;
	}
	public int getParmen15() {
		return this.parmen15;
	}
	public int getParmen16() {
		return this.parmen16;
	}
	public int getParmen17() {
		return this.parmen17;
	}
	public int getParmen18() {
		return this.parmen18;
	}
	//set pars
	public void setParmen1(int parmen1) {
		this.parmen1 = parmen1;
	}
	public void setParmen2(int parmen2) {
		this.parmen2 = parmen2;
	}
	public void setParmen3(int parmen3) {
		this.parmen3 = parmen3;
	}
	public void setParmen4(int parmen4) {
		this.parmen4 = parmen4;
	}
	public void setParmen5(int parmen5) {
		this.parmen5 = parmen5;
	}
	public void setParmen6(int parmen6) {
		this.parmen6 = parmen6;
	}
	public void setParmen7(int parmen7) {
		this.parmen7 = parmen7;
	}
	public void setParmen8(int parmen8) {
		this.parmen8 = parmen8;
	}
	public void setParmen9(int parmen9) {
		this.parmen9 = parmen9;
	}
	public void setParmen10(int parmen10) {
		this.parmen10 = parmen10;
	}
	public void setParmen11(int parmen11) {
		this.parmen11 = parmen11;
	}
	public void setParmen12(int parmen12) {
		this.parmen12 = parmen12;
	}
	public void setParmen13(int parmen13) {
		this.parmen13 = parmen13;
	}
	public void setParmen14(int parmen14) {
		this.parmen14 = parmen14;
	}
	public void setParmen15(int parmen15) {
		this.parmen15 = parmen15;
	}
	public void setParmen16(int parmen16) {
		this.parmen16 = parmen16;
	}
	public void setParmen17(int parmen17) {
		this.parmen17 = parmen17;
	}
	public void setParmen18(int parmen18) {
		this.parmen18 = parmen18;
	}

	//get strokes
	public int getStrokemen1() {
		return this.strokemen1;
	}	
	public int getStrokemen2() {
		return this.strokemen2;
	}
	public int getStrokemen3() {
		return this.strokemen3;
	}
	public int getStrokemen4() {
		return this.strokemen4;
	}
	public int getStrokemen5() {
		return this.strokemen5;
	}
	public int getStrokemen6() {
		return this.strokemen6;
	}
	public int getStrokemen7() {
		return this.strokemen7;
	}
	public int getStrokemen8() {
		return this.strokemen8;
	}
	public int getStrokemen9() {
		return this.strokemen9;
	}
	public int getStrokemen10() {
		return this.strokemen10;
	}
	public int getStrokemen11() {
		return this.strokemen11;
	}
	public int getStrokemen12() {
		return this.strokemen12;
	}
	public int getStrokemen13() {
		return this.strokemen13;
	}
	public int getStrokemen14() {
		return this.strokemen14;
	}
	public int getStrokemen15() {
		return this.strokemen15;
	}
	public int getStrokemen16() {
		return this.strokemen16;
	}
	public int getStrokemen17() {
		return this.strokemen17;
	}
	public int getStrokemen18() {
		return this.strokemen18;
	}
	//set strokes
	public void setStrokemen1(int strokemen1) {
		this.strokemen1 = strokemen1;
	}
	public void setStrokemen2(int strokemen2) {
		this.strokemen2 = strokemen2;
	}
	public void setStrokemen3(int strokemen3) {
		this.strokemen3 = strokemen3;
	}
	public void setStrokemen4(int strokemen4) {
		this.strokemen4 = strokemen4;
	}
	public void setStrokemen5(int strokemen5) {
		this.strokemen5 = strokemen5;
	}
	public void setStrokemen6(int strokemen6) {
		this.strokemen6 = strokemen6;
	}
	public void setStrokemen7(int strokemen7) {
		this.strokemen7 = strokemen7;
	}
	public void setStrokemen8(int strokemen8) {
		this.strokemen8 = strokemen8;
	}
	public void setStrokemen9(int strokemen9) {
		this.strokemen9 = strokemen9;
	}
	public void setStrokemen10(int strokemen10) {
		this.strokemen10 = strokemen10;
	}
	public void setStrokemen11(int strokemen11) {
		this.strokemen11 = strokemen11;
	}
	public void setStrokemen12(int strokemen12) {
		this.strokemen12 = strokemen12;
	}
	public void setStrokemen13(int strokemen13) {
		this.strokemen13 = strokemen13;
	}
	public void setStrokemen14(int strokemen14) {
		this.strokemen14 = strokemen14;
	}
	public void setStrokemen15(int strokemen15) {
		this.strokemen15 = strokemen15;
	}
	public void setStrokemen16(int strokemen16) {
		this.strokemen16 = strokemen16;
	}
	public void setStrokemen17(int strokemen17) {
		this.strokemen17 = strokemen17;
	}
	public void setStrokemen18(int strokemen18) {
		this.strokemen18 = strokemen18;
	}

}

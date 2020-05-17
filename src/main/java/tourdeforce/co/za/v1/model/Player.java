/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package tourdeforce.co.za.v1.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.config.CacheIsolationType;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.config.CacheIsolationType;

@Entity(name = "Player")
@Table(name="Player")
@NamedQuery(name="Player.findAll", query="SELECT p FROM Player p")
@Cache(isolation=CacheIsolationType.ISOLATED)
public class Player  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="email", unique=true, nullable=false)
	private String email;

	@Column
	private String name;
	@Column
	private String country;
	@Column
	private double handicap;
	@Lob
	@Column(length=100000000)
	private byte [] photo;
	@Column
	private String photoMimeType = null;
//	@ManyToOne
//    private Player friend;
	
//	@OneToMany(mappedBy="friend", cascade={CascadeType.ALL})
//	private List<Player> friends;
//	@OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
//	private Car car;
//
//	@Temporal(TemporalType.DATE)
//	private Calendar birthday;

	public Player() {

	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public double getHandicap() {
		return handicap;
	}
	
	public void setHandicap(double handicap) {
		this.handicap = handicap;
	}

	public String getPhotoMimeType() {
		return photoMimeType;
	}

	public void setPhotoMimeType(String photoMimeType) {
		this.photoMimeType = photoMimeType;
	}

//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
//	public Car getCar() {
//		return car;
//	}
//
//	public void setCar(Car car) {
//		this.car = car;
//	}
//
//	public Calendar getUpdated() {
//		return birthday;
//	}
//
//	public void setBirthday(Calendar birthday) {
//		this.birthday = birthday;
//	}

//	public Calendar getBirthday() {
//		return this.birthday;
//	}
}
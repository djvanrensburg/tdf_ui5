/*global location history */
sap.ui.define([
	"co/za/tourdeforce/TDF_app/controller/BaseController",
	"sap/ui/model/json/JSONModel",
	"co/za/tourdeforce/TDF_app/model/formatter",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
], function(BaseController, JSONModel, formatter, Filter, FilterOperator) {
	"use strict";

	return BaseController.extend("co.za.tourdeforce.TDF_app.controller.Worklist", {

		formatter: formatter,

		/* =========================================================== */
		/* lifecycle methods                                           */
		/* =========================================================== */

		/**
		 * Called when the worklist controller is instantiated.
		 * @public
		 */
		onInit: function() {
			var oViewModel,
				iOriginalBusyDelay,
				oTable = this.byId("table");

			// Put down worklist table's original value for busy indicator delay,
			// so it can be restored later on. Busy handling on the table is
			// taken care of by the table itself.
			// iOriginalBusyDelay = oTable.getBusyIndicatorDelay();
			// keeps the search state
			this._aTableSearchState = [];

			// Model used to manipulate control states
			oViewModel = new JSONModel({
				signedIn: false,
				isProfileEdit: false,
				worklistTableTitle: this.getResourceBundle().getText("worklistTableTitle"),
				shareOnJamTitle: this.getResourceBundle().getText("worklistTitle"),
				shareSendEmailSubject: this.getResourceBundle().getText("shareSendEmailWorklistSubject"),
				shareSendEmailMessage: this.getResourceBundle().getText("shareSendEmailWorklistMessage", [location.href]),
				tableNoDataText: this.getResourceBundle().getText("tableNoDataText"),
				tableBusyDelay: 0,
				showCourseInput: false,
				newCourseLinkText:"Course not found? Help us maintain our Database"
			});
			this.setModel(oViewModel, "ViewModel");
			var that = this;
			gapi.load('auth2', function() {
				// Retrieve the singleton for the GoogleAuth library and set up the client.
				that.auth2 = gapi.auth2.init({
					client_id: '512836479600-orbnrohsmbrfvn96h6ut06vi9sp93f4t.apps.googleusercontent.com',
					cookiepolicy: 'single_host_origin'
						// Request scopes in addition to 'profile' and 'email'
						//scope: 'additional_scope'
				});
				that.attachSignin(that.getView().byId("signin").getDomRef());
			});

			this.getView().setModel(this.getOwnerComponent().getModel());
			// Make sure, busy indication is showing immediately so there is no
			// break after the busy indication for loading the view's meta data is
			// ended (see promise 'oWhenMetadataIsLoaded' in AppController)
			// oTable.attachEventOnce("updateFinished", function(){
			// 	// Restore original busy indicator delay for worklist's table
			// 	oViewModel.setProperty("/tableBusyDelay", iOriginalBusyDelay);
			// });
		},

		/* =========================================================== */
		/* event handlers                                              */
		/* =========================================================== */
		maxFileSizeExceed: function (oEvent) {
			sap.m.MessageToast.show("The Maximum file size of " + oEvent.getParameters("fileSize") + " was exceeded");
		},

		setFileUploaderParams: function (oEvent) {
			var oFileUploader = oEvent.getSource();
			oFileUploader.addHeaderParameter(new sap.ui.unified.FileUploaderParameter({
				name: "slug",
				value: oFileUploader.getValue()
			}));
			oFileUploader.addHeaderParameter(new sap.ui.unified.FileUploaderParameter({
				name: "x-csrf-token",
				value: this.getOwnerComponent().getModel().getSecurityToken()
			}));
			oFileUploader.setSendXHR(true);
		},

		handleUploadComplete: function (oEvent) {
			if (oEvent.getParameter("status") === 204) {
				sap.m.MessageToast.show("New Profile Photo uploaded");
				this.oPhotoSelectView.close();
				// var sKey = this.getView().byId("profilePhoto").getBindingContext().sPath;
				// this.getView().byId("profilePhoto").unbindProperty("src", false);
				// this.getView().byId("profilePhoto").bindProperty("src",sKey+"/__metadata/media_src");
				window.location.reload();
			} else {
				sap.m.MessageToast.show("Error Uploading Photo");
			}
		},

		handleFileUpload: function (oEvent) {
			var oFileUploader = sap.ui.core.Fragment.byId("photofragment", "fileUploader"); //oEvent.getSource().getParent().getAggregation("content")[0];
			// oFileUploader.addHeaderParameter(new sap.ui.unified.FileUploaderParameter({
			// 	name: "slug",
			// 	value: oFileUploader.getValue()
			// }));
			oFileUploader.addHeaderParameter(new sap.ui.unified.FileUploaderParameter({
				name: "x-csrf-token",
				value: this.getOwnerComponent().getModel().getSecurityToken()
			}));
			oFileUploader.setSendXHR(true);
			oFileUploader.upload();
		},

		onPhotoDialog: function (oEvent) {
			if (!this.getView().getModel("ViewModel").getProperty("/signedIn")) {
				// sap.m.MessageToast.show(this.getOwnerComponent().getModel("i18n").getResourceBundle().getText("errorNotYourProfile"));
				return;
			}
			var oAvatar = oEvent.getSource();

			if (!this.oPhotoSelectView) {
				this.oPhotoSelectView = sap.ui.xmlfragment("photofragment", "co.za.tourdeforce.TDF_app.view.fragments.PhotoSelectFragment", this);
				this.getView().addDependent(this.oPhotoSelectView);
				var oFileUploader = sap.ui.core.Fragment.byId("photofragment", "fileUploader");
				var sURL = this.getOwnerComponent().getModel().sServiceUrl + "/Players('" + this.sMyEmailAddress + "')/$value";
				oFileUploader.setUploadUrl(sURL);
			}
			this.oPhotoSelectView.openBy(oAvatar);
		},
		
		//Courses
		handleAddCourseDialog: function(oEvent) {
			//Open the popover and set it to busy.
			if (!this.addCoursePopover) {
				this.addCoursePopover = sap.ui.xmlfragment("addCourseDialog", "co.za.tourdeforce.TDF_app.view.fragments.CourseSearchPopOver",
					this);
				this.addCoursePopover.setModel(this.getView().getModel());
				
				this.addCoursePopover.setModel(this.getModel("ViewModel"),"ViewModel");
			}
			this.addCoursePopover.openBy(oEvent.getSource());
		},
		
		onShowNewCourseInput: function(oEvent) {
			if(this.addCoursePopover.getModel("ViewModel").getProperty("/showCourseInput") === false){
				this.addCoursePopover.getModel("ViewModel").setProperty("newCourseLinkText","Cancel Course creation");
				this.addCoursePopover.getModel("ViewModel").setProperty("/showCourseInput",true);
				var oNewCourseModel = new JSONModel({
					country:"",
					coursename:"",
					holes:[
						{holeNumber:1,visible:true,strokemen:null,parmen:null},
						{holeNumber:2,visible:false,strokemen:null,parmen:null},
						{holeNumber:3,visible:false,strokemen:null,parmen:null},
						{holeNumber:4,visible:false,strokemen:null,parmen:null},
						{holeNumber:5,visible:false,strokemen:null,parmen:null},
						{holeNumber:6,visible:false,strokemen:null,parmen:null},
						{holeNumber:7,visible:false,strokemen:null,parmen:null},
						{holeNumber:8,visible:false,strokemen:null,parmen:null},
						{holeNumber:9,visible:false,strokemen:null,parmen:null},
						{holeNumber:10,visible:false,strokemen:null,parmen:null},
						{holeNumber:11,visible:false,strokemen:null,parmen:null},
						{holeNumber:12,visible:false,strokemen:null,parmen:null},
						{holeNumber:13,visible:false,strokemen:null,parmen:null},
						{holeNumber:14,visible:false,strokemen:null,parmen:null},
						{holeNumber:15,visible:false,strokemen:null,parmen:null},
						{holeNumber:16,visible:false,strokemen:null,parmen:null},
						{holeNumber:17,visible:false,strokemen:null,parmen:null},
						{holeNumber:18,visible:false,strokemen:null,parmen:null}
					]
				});
				var oNewCoursePanel = sap.ui.core.Fragment.byId("addCourseDialog", "newCoursePanel");
				oNewCoursePanel.setModel(oNewCourseModel);
				var oHoleRow = sap.ui.core.Fragment.byId("addCourseDialog", "rowHole");
				oHoleRow.bindElement("/holes/0");
			}else{
				this.addCoursePopover.getModel("ViewModel").setProperty("newCourseLinkText","Course not found? Help us maintain our Database");
				this.addCoursePopover.getModel("ViewModel").setProperty("/showCourseInput",false);				
			}			
		},
		
		onSearchCourse: function(oEvent) {
			var oItem = oEvent.getParameter("suggestionItem");
			this.sSelectedCourse = oItem.getKey();
		},
		
		onSuggestCourse: function(oEvent) {
			var oSource = oEvent.getSource();
			var sValue = oEvent.getParameter("suggestValue"),
				aFilters = [];
			if (sValue && sValue !== "") {
				aFilters = [
					new Filter("coursename", FilterOperator.Contains, sValue, "")
				];
			}
			var oBinding = oEvent.getSource().getBinding("suggestionItems");
			oBinding.attachEventOnce("dataReceived", function() {
				oSource.suggest();
			});
			oEvent.getSource().suggest();
		},
		
		onSelectCourse: function(oEvent) {
			
		},
		
		onEnterStroke: function(oEvent) {
			oEvent.getSource().getParent().getItems()[2].focus();
		},
		
		onEnterPar: function(oEvent) {
			var sPath = oEvent.getSource().getBindingInfo("value").binding.getContext().sPath;
			var iHoleIndex = parseInt(sPath.substring(7,sPath.length)) + 1;
			if(iHoleIndex === 18){
				sap.m.MessageToast.show("Please press done if you are happy with the input");
			}else{
				var sNextPath = "/holes/" + iHoleIndex;
				var oHoleRow = sap.ui.core.Fragment.byId("addCourseDialog", "rowHole");
				oHoleRow.bindElement(sNextPath);
				oEvent.getSource().getParent().getItems()[1].focus();
//				this.addCoursePopover.getModel("CourseInputModel").setProperty(sNextPath + "/visible",true);
//				
			}
//			var that = this;
//			jQuery.sap.delayedCall(500, this, function () {
//				that.addCoursePopover.getModel("CourseInputModel").setProperty(sPath + "/visible",false);
//			});
			
		},
		
		onCancelCourseSearch: function(oEvent) {
			this.addCoursePopover.close();
		},
		
		onSaveFavouriteCourse: function(oEvent) {
			//save
			if (this.sSelectedCourse) {
				var aKey = this.sSelectedCourse.split("|");
				this.getView().getModel().create("/FavouriteCourses", {
					myEmail: this.sMyEmailAddress,
					country: aKey[1],
					coursename: aKey[0]
				}, {
					success: function(oData) {
						sap.m.MessageToast.show("Course linked");
					},
					error: function(oError) {
						sap.m.MessageToast.show("Could not link Course in Database");
					}
				});
				this.addCoursePopover.close();
			} else {
				sap.m.MessageToast.show("First Select a Course");
			}
		},

		onSaveCourse: function(oEvent) {
			var oCourse = oEvent.getSource().getModel().getProperty("/");
			var that = this;
			//Save course
			this.getView().getModel().create("/Courses", {
				country: oCourse.country,
				coursename: oCourse.coursename,
				parmen1: parseInt(oCourse.holes[0].parmen),
				strokemen1: parseInt(oCourse.holes[0].strokemen),
				parmen2: parseInt(oCourse.holes[1].parmen),
				strokemen2: parseInt(oCourse.holes[1].strokemen),
				parmen3: parseInt(oCourse.holes[2].parmen),
				strokemen3: parseInt(oCourse.holes[2].strokemen),
				parmen4: parseInt(oCourse.holes[3].parmen),
				strokemen4: parseInt(oCourse.holes[3].strokemen),
				parmen5: parseInt(oCourse.holes[4].parmen),
				strokemen5: parseInt(oCourse.holes[4].strokemen),
				parmen6: parseInt(oCourse.holes[5].parmen),
				strokemen6: parseInt(oCourse.holes[5].strokemen),
				parmen7: parseInt(oCourse.holes[6].parmen),
				strokemen7: parseInt(oCourse.holes[6].strokemen),
				parmen8: parseInt(oCourse.holes[7].parmen),
				strokemen8: parseInt(oCourse.holes[7].strokemen),
				parmen9: parseInt(oCourse.holes[8].parmen),
				strokemen9: parseInt(oCourse.holes[8].strokemen),
				parmen10: parseInt(oCourse.holes[9].parmen),
				strokemen10: parseInt(oCourse.holes[9].strokemen),
				parmen11: parseInt(oCourse.holes[10].parmen),
				strokemen11: parseInt(oCourse.holes[10].strokemen),
				parmen12: parseInt(oCourse.holes[11].parmen),
				strokemen12: parseInt(oCourse.holes[11].strokemen),
				parmen13: parseInt(oCourse.holes[12].parmen),
				strokemen13: parseInt(oCourse.holes[12].strokemen),
				parmen14: parseInt(oCourse.holes[13].parmen),
				strokemen14: parseInt(oCourse.holes[13].strokemen),
				parmen15: parseInt(oCourse.holes[14].parmen),
				strokemen15: parseInt(oCourse.holes[14].strokemen),
				parmen16: parseInt(oCourse.holes[15].parmen),
				strokemen16: parseInt(oCourse.holes[15].strokemen),
				parmen17: parseInt(oCourse.holes[16].parmen),
				strokemen17: parseInt(oCourse.holes[16].strokemen),
				parmen18: parseInt(oCourse.holes[17].parmen),
				strokemen18: parseInt(oCourse.holes[17].strokemen),
			}, {
				success: function(oData) {
					that.addCoursePopover.getModel("ViewModel").setProperty("newCourseLinkText","Course not found? Help us maintain our Database");
					that.addCoursePopover.getModel("ViewModel").setProperty("/showCourseInput",false);	
					sap.m.MessageToast.show("Thanks for your contribution");
				},
				error: function(oError) {
					sap.m.MessageToast.show("Could not Course in Database");
				}
			});
			
		},
		
		//Friends
		handleAddFriendDialog: function(oEvent) {
			//Open the popover and set it to busy.
			if (!this.addFriendPopover) {
				this.addFriendPopover = sap.ui.xmlfragment("addFriendsDialog", "co.za.tourdeforce.TDF_app.view.fragments.FriendSearchPopOver",
					this);
				this.addFriendPopover.setModel(this.getView().getModel());
			}
			this.addFriendPopover.openBy(oEvent.getSource());
		},

		onSearchFriend: function(oEvent) {
			var oItem = oEvent.getParameter("suggestionItem");
			this.sSelectedFriendEmail = this.getView().getModel().getProperty(oItem.getBindingContext().sPath + "/email");
		},

		onSuggestFriend: function(oEvent) {
			var oSource = oEvent.getSource();
			var sValue = oEvent.getParameter("suggestValue"),
				aFilters = [];
			if (sValue && sValue !== "") {
				aFilters = [
					new Filter("name", FilterOperator.Contains, sValue, "")
				];
			}
			var oBinding = oEvent.getSource().getBinding("suggestionItems");
//			oBinding.filter(aFilters); For some reason this doesn't work?! It includes a duplicate $filter parameter
			oBinding.attachEventOnce("dataReceived", function() {
				oSource.suggest();
			});
			oEvent.getSource().suggest();
		},

		onCancelFriendSearch: function(oEvent) {
			this.addFriendPopover.close();
		},

		onSaveFriend: function(oEvent) {
			//save
			if (this.sSelectedFriendEmail) {
				this.getView().getModel().create("/PlayerFriends", {
					myEmail: this.sMyEmailAddress,
					friendEmail: this.sSelectedFriendEmail,
					friendshipDate: new Date()
				}, {
					success: function(oData) {
						// that._fetchDBUser(sEmail, sName);
						sap.m.MessageToast.show("Friend linked");
					},
					error: function(oError) {
						sap.m.MessageToast.show("Could not link Friend in Database");
					}
				});
				this.addFriendPopover.close();
			} else {
				sap.m.MessageToast.show("First Select a Friend");
			}
		},

		onEditProfile: function(oEvent) {
			this.getView().getModel("ViewModel").setProperty("/isProfileEdit", oEvent.getSource().getPressed());
			if (!oEvent.getSource().getPressed()) {
				//Save changes
				this.getView().getModel().submitChanges({
					success: function(oData) {
						sap.m.MessageToast.show("Profile updated");
					},
					error: function(oError) {
						sap.m.MessageToast.show("Error updating Profile");
					}
				});
			}
		},

		attachSignin: function(element) {
			var that = this;
			this.auth2.attachClickHandler(element, {},
				function(googleUser) {
					that._fetchDBUser(googleUser.getBasicProfile().getEmail(), googleUser.getBasicProfile().getName());
					// document.getElementById('name').innerText = "Signed in: " +
					// 	googleUser.getBasicProfile().getName();
				},
				function(error) {
					//set Dummy user
					that._fetchDBUser("djvanrensburgtest@gmail.com", "Dummy");
					sap.m.MessageToast.show(JSON.stringify(error, undefined, 2));
				});
		},

		/**
		 * Triggered by the table's 'updateFinished' event: after new table
		 * data is available, this handler method updates the table counter.
		 * This should only happen if the update was successful, which is
		 * why this handler is attached to 'updateFinished' and not to the
		 * table's list binding's 'dataReceived' method.
		 * @param {sap.ui.base.Event} oEvent the update finished event
		 * @public
		 */
		onUpdateFinished: function(oEvent) {
			// update the worklist's object counter after the table update
			var sTitle,
				oTable = oEvent.getSource(),
				iTotalItems = oEvent.getParameter("total");
			// only update the counter if the length is final and
			// the table is not empty
			if (iTotalItems && oTable.getBinding("items").isLengthFinal()) {
				sTitle = this.getResourceBundle().getText("worklistTableTitleCount", [iTotalItems]);
			} else {
				sTitle = this.getResourceBundle().getText("worklistTableTitle");
			}
			this.getModel("worklistView").setProperty("/worklistTableTitle", sTitle);
		},

		/**
		 * Event handler when a table item gets pressed
		 * @param {sap.ui.base.Event} oEvent the table selectionChange event
		 * @public
		 */
//		onPress: function(oEvent) {
//			// The source is the list item that got pressed
//			this._showObject(oEvent.getSource());
//		},

		/**
		 * Event handler for navigating back.
		 * We navigate back in the browser historz
		 * @public
		 */
		onNavBack: function() {
			history.go(-1);
		},

		onSearch: function(oEvent) {
			if (oEvent.getParameters().refreshButtonPressed) {
				// Search field's 'refresh' button has been pressed.
				// This is visible if you select any master list item.
				// In this case no new search is triggered, we only
				// refresh the list binding.
				this.onRefresh();
			} else {
				var aTableSearchState = [];
				var sQuery = oEvent.getParameter("query");

				if (sQuery && sQuery.length > 0) {
					aTableSearchState = [new Filter("name", FilterOperator.Contains, sQuery)];
				}
				this._applySearch(aTableSearchState);
			}

		},

		/**
		 * Event handler for refresh event. Keeps filter, sort
		 * and group settings and refreshes the list binding.
		 * @public
		 */
		onRefresh: function() {
			var oTable = this.byId("table");
			oTable.getBinding("items").refresh();
		},

		/* =========================================================== */
		/* internal methods                                            */
		/* =========================================================== */
		_fetchDBUser: function(sEmail, sName) {
			var that = this;
			that.sMyEmailAddress = sEmail;
			this.getView().bindElement({
				path: "/Players('" + sEmail + "')",
				events: {
					dataReceived: function(oData) {
						if(!oData.getParameters().data){
							//create the User
							that.getView().getModel().create("/Players", {
								email: sEmail,
								country: "",
								handicap: 0,
								name: sName,
								photo: null,
								photoMimeType: null
							}, {
								success: function(oData) {
									// that._fetchDBUser(sEmail, sName);
									that._buildView(that);
								},
								error: function(oError) {
									sap.m.MessageToast.show("Could not create user in Database");
								}
							});

						} else {
							that._buildView(that);
						}
					}
				}

			});
		},
		
		_buildView: function(that) {
			that.getView().getModel("ViewModel").setProperty("/signedIn", true);
			
			//Friend List
			var oTemplate = new sap.m.StandardListItem({title:"{friend/name}",description:"{friend/country}",info:"{friend/handicap}",icon:"{friend/__metadata/uri}/$value"});
			that.getView().byId("friendlist").bindItems({
					path:"/PlayerFriends",
					parameters: { "expand":"friend"},
					template: oTemplate,
					filters: [new Filter("myEmail", FilterOperator.EQ, that.sMyEmailAddress)],
					events:{
						dataReceived: function(oData) {
							that.getView().byId("friendtab").setCount(oData.results.length);
						}
					}
			});
			//encodeURIComponent(
			//Course List
			var oTemplate = new sap.m.StandardListItem({title:"{course/coursename}",description:"{course/country}"});
			that.getView().byId("courselist").bindItems({
					path:"/FavouriteCourses",
					parameters: { "expand":"course"},
					template: oTemplate,
					filters: [new Filter("myEmail", FilterOperator.EQ, that.sMyEmailAddress)],
					events:{
						dataReceived: function(oData) {
							that.getView().byId("coursetab").setCount(oData.results.length);
						}
					}
			});
		},
		
		
		/**
		 * Shows the selected item on the object page
		 * On phones a additional history entry is created
		 * @param {sap.m.ObjectListItem} oItem selected Item
		 * @private
		 */
		_showObject: function(oItem) {
			this.getRouter().navTo("object", {
				objectId: oItem.getBindingContext().getProperty("id")
			});
		},

		/**
		 * Internal helper method to apply both filter and search state together on the list binding
		 * @param {sap.ui.model.Filter[]} aTableSearchState An array of filters for the search
		 * @private
		 */
		_applySearch: function(aTableSearchState) {
			var oTable = this.byId("table"),
				oViewModel = this.getModel("worklistView");
			oTable.getBinding("items").filter(aTableSearchState, "Application");
			// changes the noDataText of the list in case there are no filter results
			if (aTableSearchState.length !== 0) {
				oViewModel.setProperty("/tableNoDataText", this.getResourceBundle().getText("worklistNoDataWithSearchText"));
			}
		}

	});
});
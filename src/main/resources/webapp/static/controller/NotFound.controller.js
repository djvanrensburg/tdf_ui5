sap.ui.define([
		"co/za/tourdeforce/TDF_app/controller/BaseController"
	], function (BaseController) {
		"use strict";

		return BaseController.extend("co.za.tourdeforce.TDF_app.controller.NotFound", {

			/**
			 * Navigates to the worklist when the link is pressed
			 * @public
			 */
			onLinkPressed : function () {
				this.getRouter().navTo("worklist");
			}

		});

	}
);
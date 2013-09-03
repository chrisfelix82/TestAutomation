define([ "dojo/_base/declare","dijit/Dialog","dojo/_base/lang","doh/DOHTest" ], function(declare,Dialog,lang,DOHTest) {
	return declare([DOHTest], {
		// Place comma-separated class attributes here. Note, instance attributes 
		// should be initialized in the constructor. Variables initialized here
		// will be treated as 'static' class variables.
        dialog : null,
		// Constructor function. Called when instance of this class is created
		constructor : function() {
			this.registerTests("dialogTests",["testAddingTitle","testSomething"]);
		},
		
		setup : function(){
			this.dialog = new Dialog();
		},
		
		tearDown : function(){
			this.dialog.destroyRecursive();
		},
		
		testAddingTitle : function(){
			this.dialog.set("title","A new title");
			this.dialog.show();
			doh.assertEqual(this.dialog.get("title"),"A new title");
		},
		
		testSomething : function(){
			log("1. hiding dialog");
			this.dialog.hide();
			log("2. showing dialog");
			this.dialog.show();
			doh.t(1 == 2,"1 is equal to 2");
		}
		 		
	});
});
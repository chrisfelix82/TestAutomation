define(["dojo/_base/declare",
        "example/TestWidget"], function(declare,TestWidget) {
	return declare([],{
		// Constructor function. Called when instance of this class is created
		constructor : function() {
			new TestWidget();
		}
		
	});
});
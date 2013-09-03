define([ "dojo/_base/declare","dojo/_base/lang" ], function(declare,lang) {
	return declare([], {
		// Place comma-separated class attributes here. Note, instance attributes 
		// should be initialized in the constructor. Variables initialized here
		// will be treated as 'static' class variables.
        
		// Constructor function. Called when instance of this class is created
		constructor : function() {
			
		},
		
		setup : function(){
			//override
		},
		
		tearDown : function(){
			//override
		},
		
		registerTests : function(groupName,tests){
			for(var x = 0; x < tests.length; x++){
				doh.register(groupName,{
				    name: tests[x],
				    setUp: lang.hitch(this,this.setup),
				    runTest: lang.hitch(this,this[tests[x]]),
				    tearDown: lang.hitch(this,this.tearDown)
				  });
			}//end for
		}
		 		
	});
});
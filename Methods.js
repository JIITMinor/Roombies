var assert = require('assert');

var collection = "credentials";
var room_collection = "roomDetails";
var room_mate_collection="roomMateDetails";
var characterstics_collection="characterstics";

module.exports ={

findOneUser : function(db, details , res , callback) {
	console.log("In findOneUser !!")
   db.collection(collection).findOne(details, function(err,item){
   	assert.equal(null, err);
   	console.log("doc:    ",item);
   	if(item!=null)
    {
       	console.log("crendentials Matched ,  User Login Successfully!!");
       	//res.setHeader('Content-Type', 'application/json');
        res.send("true");		
   	}
   	else{
       	console.log("crendentials doesn't Matched ,  User Login Failed!!")
       	//res.setHeader('Content-Type', 'application/json');
        res.send("false");		
   	}
   	db.close();
   });
},


insertUser : function(db, details , res , callback){
	console.log("In insertUser!! ");
	db.collection(collection).insertOne( details, function(err, result) {
    assert.equal(err, null);
    console.log("Inserted a document into the crendentials collection.");
    res.send("true");		
    callback();
  });
},


insertRoomDetails : function(db,details,res,callback){
	console.log("In insertRoomDetails !! ");
	db.collection(room_collection).insertOne(details, function(err, result) {
    assert.equal(err, null);
    console.log("Inserted a document into the roomDetails collection.");
    res.send("true");
    callback();
  });
},


findotp : function(db, details , res , callback) {
  console.log("In findOtp !!")
   db.collection(collection).findOne(details, function(err,item){
    assert.equal(null, err);
    console.log("doc:    ",item);
    if(item!=null)
    {
        console.log("Otp found Successfully!!");
        res.send("true");   
    }
    else
    {
        console.log("Otp Not Found!!")
        //res.setHeader('Content-Type', 'application/json');
        res.send("false");    
    }
    db.close();
   });
},


updateDocument : function(db, details ,res, callback) {
      console.log("In Update document function !! ");
      console.log("details to be updated are:");
      console.log(details);
      db.collection(collection).updateOne(
      { "email":details.email},
      {
        $set: { "password":details.password,
                "otp":details.otp
      }
      }, function(err, results) {  
      console.log("Updated a document into the crendentials collection.");
      res.send("true");
      callback();
   });
},


saveRoomMateCharacterstics : function(db,details,res,callback){
    console.log("In save roommate characterstics function!! ");
    db.collection(characterstics_collection).insertOne( details, function(err, result) {
      assert.equal(err, null);
      console.log("Inserted a document into the characterstics collection.");
      res.send("true");   
      callback();
    });
},


insertRoomMateDetails : function(db,details,res,callback){
	console.log("In insertRoomMateDetails !! ");

	db.collection(room_mate_collection).insertOne(details, function(err, result) {
    assert.equal(err, null);
    console.log("Inserted a document into the roomMateDetails collection.");
    res.send("true");
    callback();
  });
},


getRoomDetails : function(db,details,res,callback){

	console.log("In getRoomDetails !! ");
	db.collection(room_collection).find().toArray(function(err,item){
   	assert.equal(null,err);
	res.send(item);
   });
},


sortByRent : function(db,details,res,callback){

	console.log("In sortByRent !! ");
	db.collection(room_collection).find({}, { sort:[['Expected Rent',1]]}).toArray(function (err, items){ 
	res.send(items);
	});
},


sortByRentDesc : function(db,details,res,callback){

	console.log("In sortByRentDesc !! ");
	db.collection(room_collection).find({}, { sort:[['Expected Rent',-1]]}).toArray(function (err, items){ 
	res.send(items);
	});
},


getProfiles : function(db,details,res,callback){

  console.log("In Profile get !! ");
  console.log("Details!!    ",details);
  db.collection(characterstics_collection).find(
  {
    $or: [
         {"Nature": details.nature}, {"Sleep":details.sleep},{"Eating":details.eat},{"Alcohol":details.alcohol},{"Smoking":details.smoke}
	],
	$and: [
		{"usermail": { $ne : details.mymail } }
	]
  }
  ).toArray(function(err,item){
    assert.equal(null,err);
    if(item!=null)
    {
        var ii=new Array()
        //console.log(item.length);
        for(i=0;i<item.length;i++)
          ii.push(item[i].usermail);
        res.send(ii);
        //console.log("item to send is:",ii);
    }
    else
    {
        res.send("false");
    }
   });
},


lookingroommates : function(db, details , res , callback) {
  console.log("In lookingroommates !!")
  //console.log("details.email is:" ,  details.email)
   db.collection(room_collection).findOne(details, function(err,item){
    assert.equal(null, err);
    //console.log("doc:    ",item);
    if(item!=null)
    {
        console.log("crendentials Matched , Successfully!!");
        //res.setHeader('Content-Type', 'application/json');
        var item_to_send={"profilePhoto":item.profilePhoto,
        "Name":item.Name,
        "Email":item.email
        }
       // console.log("item to send is:    ",item_to_send);
        res.send(item_to_send);   
    }
    else
    {
        console.log("crendentials doesn't Matched , Failed!!")
        //res.setHeader('Content-Type', 'application/json');
        res.send("false");    
    }
    db.close();
   });
},


getmyprofile : function(db, details , res , callback) {
  console.log("In get my profile !!")
  console.log("details.email is:" ,  details.email)
   db.collection(room_collection).findOne(
    {"email":details.email}, function(err,item){
    assert.equal(null, err);
    console.log("doc:    ",item);
    if(item!=null)
    {
        console.log("crendentials Matched , Successfully!!");
        //res.setHeader('Content-Type', 'application/json');
        res.send(item);   
    }
    else
    {
        console.log("crendentials doesn't Matched , Failed!!")
        //res.setHeader('Content-Type', 'application/json');
        res.send("false");    
    }
    db.close();
   });
},


UpdateOtpMethod : function(db, details ,res, callback) {
      console.log("In Update document function !! ");
      console.log("details to be updated are:");
      console.log(details);
      db.collection(collection).updateOne(
      { "email":details.email},
      {
        $set: { "otp":details.otp
      }
      }, function(err, results) {  
      console.log("Updated a document into the crendentials collection.");
      res.send("true");
      callback();
   });
},
getlocation : function(db, details , res , callback) {
  console.log("In get location !!")
  console.log("details.email is:" ,  details.email)
   db.collection(room_collection).findOne(
    {"email":details.email}, function(err,item){
    assert.equal(null, err);
    console.log("doc:    ",item);
    if(item!=null)
    {
        console.log("crendentials Matched , Successfully!!");
        //res.setHeader('Content-Type', 'application/json');
        res.send(item);   
    }
    else
    {
        console.log("crendentials doesn't Matched , Failed!!")
        //res.setHeader('Content-Type', 'application/json');
        res.send("false");    
    }
    db.close();
   });
}


};
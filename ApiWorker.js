var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');

var request = require("request");
var express = require('express');
var app = express();
var bodyParser = require("body-parser");
var methods = require('./Methods');
app.use(bodyParser.urlencoded({limit: '50mb',extended : true}));
app.use(bodyParser.json({limit: '50mb'}));

var url = 'mongodb://yoururl'



app.get('/login', function ( req, res) {
	var email = req.query["email"]
	var password = req.query["password"]
	var details = {
		"email":email,
		"password":password
	}
	console.log("Input Parameters and values are : ")
	console.log("email: ",email)
	console.log("password: ",password)

	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.findOneUser(db, details , res , function() {});
	  db.close();
	});

});

app.post('/notify',function(req,res){
	var deviceId = req.body.deviceid;
	var message = req.body.message;
	
	request({
    url: 'https://fcm.googleapis.com/fcm/send',
    method: 'POST',
    headers: {
      'Content-Type' :' application/json',
      'Authorization': 'key=AIzaSyAETmWtugZVctvLjoGrs_UUfsP-mTVk60E'
    },
    body: JSON.stringify(
      { "data": {
        "message": message
      },
        "to" : deviceId
      }
    )
  }, 
  function(error, response, body) {
    if (error) { 
      console.error(error, response, body); 
    }
    else if (response.statusCode >= 400) { 
      console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage+'\n'+body); 
	  console.error('HTTP Error: '+deviceId+' - '+message+'\n'+body); 
    }
    else {
      console.log('Done! yay')
	  res.send("true"); 
    }
  });
	
});
app.get('/verifyotpLogin',function(req,res){
	var email=req.query["email"]
	//var password=req.query["password"]
	var otp=req.query["otp"];
	var details={
		"email":email,
		//"password":password,
		"otp":otp
	}
	console.log("in verify otp function!!");
	console.log("email:",email)
	//console.log("password:",password)
	console.log("otp:",otp)
	MongoClient.connect(url,function(err,db){
		assert.equal(null,err);
		console.log("Connected Correctly to server.");
		methods.findotp(db,details,res,function(){});
		db.close();
	});
	
});


app.get('/verifyotp',function(req,res){
	var email=req.query["email"]
	var password=req.query["password"]
	var otp=req.query["otp"];
	var details={
		"email":email,
		"password":password,
		"otp":otp
	}
	console.log("in verify otp function!!");
	console.log("email:",email)
	console.log("password:",password)
	console.log("otp:",otp)
	MongoClient.connect(url,function(err,db){
		assert.equal(null,err);
		console.log("Connected Correctly to server.");
		methods.findotp(db,details,res,function(){});
		db.close();
	});
	
});


app.post('/register', function ( req, res) {
	var details = req.body;
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var email = req.body.email;
	var password = req.body.password;
	var otp=req.body.otp;
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.insertUser(db, details , res , function() {});
	  db.close();
	});
	
});


app.post('/saveRoomMateDetails', function ( req, res) {
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.insertRoomMateDetails(db,details,res,function(){});
	  db.close();
	});
	
});


app.post('/saveRoomDetails', function ( req, res) {
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.insertRoomDetails(db,details,res,function(){});
	  db.close();
	});
	
});

app.post('/savecharacterstics', function ( req, res) {
	console.log("in save Characterstics Api!!")
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.saveRoomMateCharacterstics(db,details,res,function(){});
	  db.close();
	});
	
});

app.get('/getRoomDetails', function ( req, res) {
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.getRoomDetails(db,details,res,function(){});
	  db.close();
	});
	
});


app.post('/UpdatePassword',function(req,res){
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body;
	var email = req.query["email"];
	var password = req.query["password"];
	//var otp = req.query["otp"];
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
      methods.updateDocument(db,details,res,function(){});
      db.close();
	});
});



app.get('/sortByRent', function ( req, res) {
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.sortByRent(db,details,res,function(){});
	  db.close();
	});
	
});


app.get('/sortByRentDesc', function ( req, res) {
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.sortByRentDesc(db,details,res,function(){});
	  db.close();
	});
	
});

app.get('/findMates', function ( req, res) {
	console.log("in findMates!!!");
	var nature = req.query["nature"]
	var smoke = req.query["smoke"]
	var alcohol=req.query["alcohol"]
	var sleep=req.query["sleep"]
	var eat=req.query["eat"]
	var mymail = req.query["myemail"]
	var details = {
		"nature":nature,
		"eat":eat,
		"sleep":sleep,
		"smoke":smoke,
		"alcohol":alcohol,
		"mymail":mymail
	}
	console.log("Input Parameters and values are : ",details)

	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.getProfiles(db, details , res , function() {});
	  db.close();
	});

});

app.get('/getroommates', function ( req, res) {
	console.log("in get roommates!!!");
	var email = req.query["email"]
	var details = {
		"email":email
	}
	console.log("Input Parameters and values are : ")
	console.log("email: ",email)

	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.lookingroommates(db, details , res , function() {});
	  db.close();
	});

});

app.get('/getprofile', function ( req, res) {
	console.log("in get my profile!!!");
	var email = req.query["email"]
	var details = {
		"email":email
	}
	console.log("Input Parameters and values are : ")
	console.log("email: ",email)

	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.getmyprofile(db, details , res , function() {});
	  db.close();
	});

});


app.listen(5000, function () {
	  console.log('Login app listening on port ',5000,'!');
	});
	
app.get('/getlocation', function ( req, res) {
	console.log("in get location!!!");
	var email = req.query["email"]
	var details = {
		"email":email
	}
	console.log("Input Parameters and values are : ")
	console.log("email: ",email)

	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
	  methods.getlocation(db, details , res , function() {});
	  db.close();
	});

});
	
	app.post('/UpdateOtp',function(req,res){
	var data = JSON.stringify(req.body);
	console.log("Data:   ",data);
	var details = req.body;
	var email = req.query["email"];
	//var password = req.query["password"];
	var otp = req.query["otp"];
	MongoClient.connect(url, function(err, db) {
	  assert.equal(null, err);
	  console.log("Connected correctly to server.");
      methods.UpdateOtpMethod(db,details,res,function(){});
      db.close();
	});
});

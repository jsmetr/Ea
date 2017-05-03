/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var unameField;
var pswField;
var req;


function init(){
    document.getElementById('login-form');
    unameField = document.getElementById('form2');
    pswField = document.getElementById('form3');
    populateUsers();
}

function initRequest() {
    if(window.XMLHttpRequest){
        if(navigator.userAgent.indexOf('MSIE') !== -1){
            isIE=true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject){
        isIE=true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}
 
function login(){
    var url= "http://localhost:8080/EasyFix/webresources/Users/Login/"+unameField.value+"/"+pswField.value;
    console.log((unameField.value)+"/"+(pswField.value));
    req=initRequest();
    req.open("PUT",url,true);
    req.onreadystatechange = loginCallback;
    req.send(null);
    unameField.value="";
    pswField.value="";
}

function loginCallback(){
    
    if (req.readyState == 4){
        if(req.status == 200){
            if(req.responseText!=="FAILURE"){
                localStorage.setItem("sessionId",req.responseText);
                window.location.replace("manager.html");
            }else {
                alert("Your username and password are invalid.");                
            }
        }
    }
}

function populateUsers(){
    console.log("pop");
    var url= "http://localhost:8080/EasyFix/webresources/Testing/populate";
    req=initRequest();
    req.open("GET",url,true);
    req.send(null);
}

function showntell(){
    console.log((unameField.value)+"/"+(pswField.value));    
}

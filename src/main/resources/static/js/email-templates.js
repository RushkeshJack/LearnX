/*let option1 = document.getElementById('option1');
let option2 = document.getElementById('option2');
let option3 = document.getElementById('option3');
let option4 = document.getElementById('option4');
let option5 = document.getElementById('option5');
let option6 = document.getElementById('option6');
let option7 = document.getElementById('option7');
let option8 = document.getElementById('option8');*/



/*option1.addEventListener("click", display1);
option2.addEventListener("click", display2);
option3.addEventListener("click", display3);
option4.addEventListener("click", display4);
option5.addEventListener("click", display5);
option6.addEventListener("click", display6);
option7.addEventListener("click", display7);
option8.addEventListener("click", display8);*/


let to = document.getElementById('toText');
let cc = document.getElementById('ccText');
let subject = document.getElementById('subjectText');
let docs = document.getElementById('attachedDocs');
let header = document.getElementById('header');
let emailHeader = document.getElementById('emailHeader');
let content = document.getElementById('content');

const options = {1:	'API Requirement gathering',2:'Request and Response copybooks',
3:'Solution sketch',4:'Demo request for API testing',5:'Request and Response validation',6:'Deliverables to consuming team',
7:'Production checkout request data',8:'Access expiry and Extension',9:'Effots allocation for project',
10:'Webeng request for new transaction id defination'};

const li = document.getElementById('left-li');

Object.keys(options).forEach(key => { 
	        const a = document.createElement('a');
            a.textContent = options[key];
            a.id = 'id'+key;
            //console.log(a.id);
            li.appendChild(a);
});      


// Get references to all anchor tags and the content div
const links = document.querySelectorAll("a");
//console.log(links)

// Add click event listener to each anchor tag
links.forEach(link => {
    link.addEventListener("click", () => changeContent(link));
});

// Function to change the content
function changeContent(clickedLink) {
    const linkId = clickedLink.id;
    
    //Use switch case to change the data
    
    switch(linkId){
		case "id1":
			header.innerText = "API Requirement gathering";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "API Requirement sheet";
	        
	        docs.innerText = "API Requirement Excel sheet";
	        
	        emailHeader.innerText = "\n Hi project_manager_names, Backend_Team,";
	        content.innerText = "Please find attachment of API requirement sheet. Below is the color coding in column A"
	        + " is given for them who can provide the information."
	        + "\n\n Project manager -  Red \n architect - Gray \n Backend Developer - Violet\n\n";
	        
	        break;
	    
	    case "id2":
			header.innerText = "Request and Response Copybooks";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Request and Response copybooks for development";

	        docs.innerText = "None";

	        emailHeader.innerText = "\n Hi Backend_Team ";
	        content.innerText = "Please share us the request and response copybooks to start the development.";
		        
		     break;
		      
		case "id3":
			header.innerText = "Solution sketch";
	        to.innerText = "Include project manager's, Solution Architect's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Latest solution sketch for API API_Name";

	        docs.innerText = "None";

	        emailHeader.innerText = "\n Hi Solution Architect's ";
	        content.innerText = "Please share us the solution sketch for the API API_Name. So that we can understand API flow and" 
	        +" connect with other teams.";
		        
		     break;
	     
	     case "id4":
			header.innerText = "Demo request and response for API testing";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Demo request for API testing";

	        docs.innerText = "Request & Response File";

	        emailHeader.innerText = "\n Hi Backend_Team ";
	        content.innerText = "Please share 1-2 demo request and response data for testing the API API_Name.";
		        
		     break;
		     
		case "id5":
			header.innerText = "Request and Response validation";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Request and Response Validation for API_Name";

	        docs.innerText = "Request & Response File";

	        emailHeader.innerText = "\n Hi Backend_Team ";
	        content.innerText = "Please find attachment of Request and Response for API_Name in DEV/QA path 1/2/3. "
		        + " Please let us know if the response looks good.";
		        
		     break;
		
		 case "id6":
			header.innerText = "Deliverables to consuming team";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Deliverables to consuming team";

	        docs.innerText = "Swagger file + DEV/QA Request and Response";

	        emailHeader.innerText = "\n Hi Consumer Team ";
	        content.innerText = "Please find attachment of Request and Response for API_Name in DEV/QA path 1/2/3. "
		        + " Also attached is the swagger file. Below are the DEV and QA path 1/2/3 URL's from Java connect team."
		        + "\n\n\n DEV path 1: http://DEV_1_API_Endpoint_Here/api/v1/Employee/getEmployee/{id}"
		        + "\n DEV path 2: http://DEV_2_API_Endpoint_Here/api/v1/Employee/getEmployee/{id}"
		        + "\n DEV path 3: http://DEV_3_API_Endpoint_Here/api/v1/Employee/getEmployee/{id}"
		        + "\n QA path 1: http://QA_1_API_Endpoint_Here/api/v1/Employee/getEmployee/{id}"
		        + "\n QA path 2: http://QA_2_API_Endpoint_Here/api/v1/Employee/getEmployee/{id}"
		        + "\n QA path 3: http://QA_3_API_Endpoint_Here/api/v1/Employee/getEmployee/{id}";
		        
		    break;
		     
		  
		     
		  case "id7":
			header.innerText = "Production checkout request data";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Production checkout request data for API API_name";

	        docs.innerText = "Request & Response copybooks for reference";

	        emailHeader.innerText = "\n Hi Backend_Team ";
	        content.innerText = "Production deployment is on 'Date & Time of deployment' EST . For that please share us the checkout data for production deployment."
	        +" Response validation will happen on deployment date.";
		        
		     break;
		     
		  case "id8":
			header.innerText = "Access expiry and Extension";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Access expiry and Extension for server server_name";

	        docs.innerText = "Excel sheet for access submission and extension";

	        emailHeader.innerText = "\n Hi john doe, ";
	        content.innerText = "Attached is excel sheet for access extension for employee Employee-Number. Please extend the access for mentioned server in attached sheet for 6 months.";
		        
		     break;   
		     
		     
		  case "id9":
			header.innerText = "Effots allocation for project";
	        to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	        cc.innerText = "Include Team members, other developers connecting main Application";
	        subject.innerText = "Effots allocation for project Project_Name";

	        docs.innerText = "None";

	        emailHeader.innerText = "\n Hi Project_Manager ";
	        content.innerText = "Please allocate Allocation_Hours to the project Project_Name for development and testing.";
		        
		     break;
		     
		     
		  case "id10":
			header.innerText = "Webeng request for new transaction id defination";
	        to.innerText = "Webeng team";
	        cc.innerText = "Include project manager's, Architect Designer's, Backend Team, Team members, other developers";
	        subject.innerText = "Webeng request for new transaction id defination";

	        docs.innerText = "Request submission emails";

	        emailHeader.innerText = "\n Hi Webeng team ";
	        content.innerText = "Attached are the request submmitted for the new transaction defination in both DEV and QA path 1/2/3."
	        +" Please let us know once the request gets completed.";
		        
		     break;
	}
    
    
}

/*

function display1() {
	header.innerText = "API Requirement gathering";

	to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	cc.innerText = "Include Team members, other developers connecting main Application";
	subject.innerText = "API Requirement sheet";

	docs.innerText = "API Requirement Excel sheet";

	emailHeader.innerText = "\n Hi project_manager_names, Backend_Team,";
	content.innerText = "Please find attachment of API requirement sheet. Below is the color coding in column A"
		+ " is given for them who can provide the information."
		+ "\n\n Project manager -  Red \n architect - Gray \n Backend Developer - Violet\n\n";

}
function display2() {
	header.innerText = "Request and Response validation";

	to.innerText = "Include project manager's, Architect Designer's, Application developer's, Backend Team";
	cc.innerText = "Include Team members, other developers connecting main Application";
	subject.innerText = "Request and Response Validation for API_Name";

	docs.innerText = "Request & Response File";

	emailHeader.innerText = "\n Hi Backend_Team/Backend_Team, ";
	content.innerText = "Please find attachment of Request and Response for API_Name in DEV/QA path 1/2/3. "
		+ " Please let us know if the response looks good.";
}
function display3() {

}
function display4() {

}
function display5() {

}
function display6() {

}
function display7() {

}
function display8() {

}*/
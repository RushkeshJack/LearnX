const options = {1:	'Java introduction',2:'Lets create Java project',
3:'What is springMVC',4:'What is Spring Boot',
5:'Git commands and Github',6:'Rest API in Springboot',7:'Introduction to HTML',8:'Introduction to CSS',9:'Introduction to Javascript',10:'React Frontend framework'};

const li = document.getElementById('left-li');

Object.keys(options).forEach(key => { 
	const a = document.createElement('a');
            a.textContent = options[key];
            a.href = "/LearnX/home/kt-sessions/"+key; 
            li.appendChild(a);
  
});



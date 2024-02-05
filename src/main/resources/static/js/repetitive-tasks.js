
//Code for left menu options with anchor tags
const options = {1:	'Create Java project',2:'Create Spring MVC project',
3:'How to add Maven dependancies in project',
4:'How to install postman for API testing',5:'Test API in postman',
6:'How to install SoapUI for API testing',7:'Test API in SoapUI',
8:'How to install Github',9:'Update latest code to Github'};

const li = document.getElementById('left-li');

Object.keys(options).forEach(key => { 
	        const a = document.createElement('a');
            a.textContent = options[key];
            //a.id = 'id'+key;
            a.href = "/LearnX/home/repetitive-tasks/"+key;
            li.appendChild(a);
});  



//Below code is for image slide show present on right container
let slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
	showSlides((slideIndex += n));
}

function currentSlide(n) {
	showSlides((slideIndex = n));
}

function showSlides(n) {
	let i;
	let slides = document.getElementsByClassName("mySlides");
	//let dots = document.getElementsByClassName("dot");
	if (n > slides.length) {
		slideIndex = 1;
	}
	if (n < 1) {
		slideIndex = slides.length;
	}
	for (i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
	}
	/*for (i = 0; i < dots.length; i++) {
		dots[i].className = dots[i].className.replace(" active", "");
	}*/
	slides[slideIndex - 1].style.display = "block";
	//dots[slideIndex - 1].className += " active";
}



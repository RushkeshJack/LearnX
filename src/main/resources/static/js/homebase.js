 // Get the current URL
 var currentUrl = window.location.pathname;
 
 //console.log(currentUrl);

 // Find the corresponding navigation link and add the 'active' class
 $('.nav-link').each(function () {
        var linkUrl = $(this).attr('href');
        
        //console.log(linkUrl);
        
        if (currentUrl === linkUrl) {
            $(this).addClass('active');
        } else {
            $(this).removeClass('active');
        }
 });